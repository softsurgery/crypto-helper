package com.zc.cryptohelper.crypto_helper.service;

import com.zc.cryptohelper.crypto_helper.models.CoinCategory;
import com.zc.cryptohelper.crypto_helper.payload.CryptoData;
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoCategoryWebScrapingService {
    private static final Logger logger = LoggerFactory.getLogger(CryptoCategoryWebScrapingService.class);
    private static final String TABLE_CLASS_NAME = "cmc-table";
    private static final int LAST_PAGE = 2;
    private static final int SCROLL_PAUSE_TIME = 200;
    private static final int SCROLL_INCREMENT = 1500;
    private static final int WEB_DRIVER_WAIT_TIME = 2;
    private static final List<String> CHROME_OPTIONS = List.of(
            "--headless",
            "--disable-gpu",
            "--no-sandbox",
            "--disable-dev-shm-usage"
    );

    @Autowired
    private final CoinCategoryService coinCategoryService;


    //initialize the dependencies
    public CryptoCategoryWebScrapingService(CoinCategoryService coinCategoryService) {
        this.coinCategoryService = coinCategoryService;
    }

    //initialize a webdriver with options
    public WebDriver getWebDriverWithOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(CHROME_OPTIONS);
        return new ChromeDriver(options);
    }

    //this function will stall the page until it finishes loading all the data
    public WebElement getCapCoinMarketCMCTableBody(WebDriver driver, String url) {
        driver.get(url);

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

        logger.info("Cell in the 100th row and 10th column is visible.");
        return tbody;
    }

    //this method will fetch the current data
    public List<CryptoData> fetchCoinMarketCapCategories(String url) {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        WebDriver driver = this.getWebDriverWithOptions();
        List<CryptoData> cryptoDataList = new ArrayList<>();

        try {
            WebElement tbody = getCapCoinMarketCMCTableBody(driver, url);
            List<WebElement> rows = tbody.findElements(By.tagName("tr"));
            if (rows.isEmpty()) {
                logger.warn("No rows found in the table body.");
            } else {
                logger.info("Found " + rows.size() + " rows.");
                for (WebElement row : rows) {
                    try {
                        List<WebElement> cells = row.findElements(By.tagName("td"));
                        if (cells.size() > 1) {
                            CoinCategory coinCategory = new CoinCategory();
                            coinCategory.setName(cells.get(1).getText());
                            coinCategory.setUrl(cells.get(1).findElement(By.tagName("a")).getAttribute("href"));  // Assuming there's a setLink method in CoinCategory
                            coinCategoryService.createCoinCategory(coinCategory);
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
