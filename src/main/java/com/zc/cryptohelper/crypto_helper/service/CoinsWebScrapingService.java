package com.zc.cryptohelper.crypto_helper.service;

import com.zc.cryptohelper.crypto_helper.models.Coin;
import com.zc.cryptohelper.crypto_helper.payload.CryptoData;
import com.zc.cryptohelper.crypto_helper.storage.Upload;
import com.zc.cryptohelper.crypto_helper.storage.UploadService;
import com.zc.cryptohelper.crypto_helper.utils.CustomMultipartFile;
import com.zc.cryptohelper.crypto_helper.utils.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CoinsWebScrapingService {
    private static final Logger logger = LoggerFactory.getLogger(CoinsWebScrapingService.class);
    private static final String TABLE_CLASS_NAME = "cmc-table";
    private static final int SCROLL_PAUSE_TIME = 100;
    private static final int SCROLL_INCREMENT = 1700;
    private static final int WEB_DRIVER_WAIT_TIME = 2;
    private static final List<String> CHROME_OPTIONS = List.of(
            "--headless",
            "--disable-gpu",
            "--no-sandbox",
            "--disable-dev-shm-usage"
    );

    @Autowired
    private final CoinService coinService;

    @Autowired
    private final UploadService uploadService;

    //initialize the dependencies
    public CoinsWebScrapingService(CoinService coinService, UploadService uploadService) {
        this.coinService = coinService;
        this.uploadService = uploadService;
    }

    //initialize a webdriver with options
    public WebDriver getWebDriverWithOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(CHROME_OPTIONS);
        return new ChromeDriver(options);
    }

    //this method will be called by a recurrent job
    public void saveCoinData(List<WebElement> cells) throws IOException {
        String name = cells.get(2).getText().replaceAll("\\s+[^\\s]+$", "");
        WebElement imgElement = cells.get(2).findElement(By.tagName("img"));
        String imageUrl = imgElement.getAttribute("src");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            String fileName = name + ".png";

            // Check if the image already exists in the upload service
            Optional<Upload> existingUploadOpt = uploadService.findByFileName(fileName);
            Upload upload;

            synchronized (this) {
                // Double-check if the image was added by another thread/process
                existingUploadOpt = uploadService.findByFileName(fileName);
                if (existingUploadOpt.isEmpty()) {
                    // Download and store image if not already present
                    File imageFile = FileUtils.downloadFile(imageUrl, name, ".png");
                    MultipartFile multipartFile = new CustomMultipartFile(imageFile);

                    // Store the new upload
                    upload = uploadService.store(multipartFile);
                    uploadService.save(upload);  // Ensure upload is saved
                } else {
                    upload = existingUploadOpt.get();
                }
            }

            // Create or update coin with the upload
            Optional<Coin> existingCoinOpt = coinService.findCoinByName(name);
            if (existingCoinOpt.isEmpty()) {
                coinService.createCoin(name, upload);
            }
        }

    }

    //this function will stall the page until it finishes loading all the data
    public WebElement getCapCoinMarketCMCTableBody(WebDriver driver, String url, int page) {
        driver.get(url + "?page=" + page);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WEB_DRIVER_WAIT_TIME));

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Incremental scrolling
        boolean endReached = false;
        long previousHeight = (Long) jsExecutor.executeScript("return document.body.scrollHeight");

        while (!endReached) {
            jsExecutor.executeScript("window.scrollBy(0, " + SCROLL_INCREMENT + ");");
            logger.info("Scrolling down by " + SCROLL_INCREMENT + " pixels...");

            try {
                Thread.sleep(SCROLL_PAUSE_TIME);
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for page to load: " + e.getMessage());
            }

            long newHeight = (Long) jsExecutor.executeScript("return document.body.scrollHeight");
            if (newHeight == previousHeight) {
                endReached = true;
            } else {
                previousHeight = newHeight;
            }
        }
        logger.info("Reached the end of the page.");

        WebElement table = driver.findElement(By.cssSelector("table." + TABLE_CLASS_NAME));
        WebElement tbody = table.findElement(By.tagName("tbody"));

        wait.until(driver1 -> {
            try {
                WebElement row = tbody.findElements(By.tagName("tr")).get(99);
                List<WebElement> cells = row.findElements(By.tagName("td"));
                return cells.size() > 9 ? cells.get(9) : null;
            } catch (Exception e) {
                return null;
            }
        });
        logger.info("Cell in the 100th row and 10th column is visible.");
        return tbody;
    }

    //this method will fetch the current data
    public List<CryptoData> fetchCoinMarketCap(String url, int page) {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        WebDriver driver = this.getWebDriverWithOptions();
        List<CryptoData> cryptoDataList = new ArrayList<>();

        try {
            WebElement tbody = getCapCoinMarketCMCTableBody(driver, url, page);
            List<WebElement> rows = tbody.findElements(By.tagName("tr"));
            if (rows.isEmpty()) {
                logger.warn("No rows found in the table body.");
            } else {
                logger.info("Found " + rows.size() + " rows.");
                for (WebElement row : rows) {
                    try {
                        List<WebElement> cells = row.findElements(By.tagName("td"));
                        if (cells.size() > 1) {
                            CryptoData cryptoData = new CryptoData();
                            cryptoData.setRank(Integer.parseInt(cells.get(1).getText()));
                            //logger.info(cells.get(1).getText());

                            //Name, Code & Picture
                            cryptoData.setCode(cells.get(2).getText().trim().split("\\s+")[cells.get(2).getText().trim().split("\\s+").length - 1]);
                            cryptoData.setName(cells.get(2).getText().replaceAll("\\s+[^\\s]+$", ""));
                            WebElement imgElement = cells.get(2).findElement(By.tagName("img"));
                            cryptoData.setPictureURL(imgElement.getAttribute("src")); // Assuming CryptoData has a setImageUrl method
                            //Price
                            cryptoData.setPrice(Float.parseFloat(cells.get(3).getText().replace("$", "").replace(",", "")));

                            //Raise1Hour
                            boolean hasIconCaretDownForRaise1Hour = !cells.get(4).findElements(By.cssSelector("span.icon-Caret-down")).isEmpty();
                            cryptoData.setRaise1Hour(Float.parseFloat(cells.get(4).getText().replace("%", "").replace(",", "")));
                            if (hasIconCaretDownForRaise1Hour) cryptoData.setRaise1Hour(-cryptoData.getRaise1Hour());
                            //Raise24Hour
                            boolean hasIconCaretDownForRaise24Hours = !cells.get(5).findElements(By.cssSelector("span.icon-Caret-down")).isEmpty();
                            cryptoData.setRaise24Hours(Float.parseFloat(cells.get(5).getText().replace("%", "").replace(",", "")));
                            if (hasIconCaretDownForRaise24Hours)
                                cryptoData.setRaise24Hours(-cryptoData.getRaise24Hours());
                            //Raise7Days
                            boolean hasIconCaretDownForRaise7Days = !cells.get(6).findElements(By.cssSelector("span.icon-Caret-down")).isEmpty();
                            cryptoData.setRaise7Days(Float.parseFloat(cells.get(6).getText().replace("%", "").replace(",", "")));
                            if (hasIconCaretDownForRaise7Days) cryptoData.setRaise7Days(-cryptoData.getRaise7Days());
                            //MarketCap
                            cryptoData.setMarketCap(cells.get(7).getText().replace("$", "").replace(",", ""));
                            //Volume24hours
                            cryptoData.setVolume24Hour(cells.get(8).getText().split("\\s+")[0].replace("$", "").replace(",", ""));
                            //CirculatingSupply
                            cryptoData.setCirculatingSupply(cells.get(9).getText().split("\\s+")[0].replace("$", "").replace(",", ""));

                            //save coin data
                            saveCoinData(cells);

                            cryptoDataList.add(cryptoData);
                        }
                    } catch (Exception e) {
                        logger.error("Error extracting data from row: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error during web scraping: " + e.getMessage());
        } finally {
            driver.quit();
        }

        return cryptoDataList;
    }
}