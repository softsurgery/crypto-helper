package com.zc.cryptohelper.crypto_helper.controller;

import com.zc.cryptohelper.crypto_helper.payload.CryptoData;
import com.zc.cryptohelper.crypto_helper.service.CoinsWebScrapingService;
import com.zc.cryptohelper.crypto_helper.service.CryptoCategoryWebScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/web-data")
public class WebScrapingController {
    private static final String BASE_URL = "https://coinmarketcap.com/";
    private static final String MOST_VISITED_BASE_URL = "https://coinmarketcap.com/most-viewed-pages/";
    private static final String TRENDING_BASE_URL = "https://coinmarketcap.com/trending-cryptocurrencies/";
    private static final String CATEGORIES_BASE_URL = "https://coinmarketcap.com/cryptocurrency-category/";


    @Autowired
    private CoinsWebScrapingService webScrapingService;

    @Autowired
    private CryptoCategoryWebScrapingService cryptoCategoryWebScrapingService;


    @GetMapping("/coins")
    public List<CryptoData> getCryptoDataBasic(@RequestParam int page) {
        return webScrapingService.fetchCoinMarketCap(BASE_URL,page);
    }

    @GetMapping("/most-visited-coins")
    public List<CryptoData> getCryptoDataMostVisited(@RequestParam int page) {
        return webScrapingService.fetchCoinMarketCap(MOST_VISITED_BASE_URL,page);
    }

    @GetMapping("/trending")
    public List<CryptoData> getCryptoDataTrending(@RequestParam int page) {
        return webScrapingService.fetchCoinMarketCap(TRENDING_BASE_URL,page);
    }

    @GetMapping("/by-category")
    public List<CryptoData> getCryptoDataCategory(@RequestParam String source, @RequestParam int page) {
        return webScrapingService.fetchCoinMarketCap(source,page);
    }

    @GetMapping("/scrape-categories")
    public List<CryptoData> scrapeCategories() {
        return cryptoCategoryWebScrapingService.fetchCoinMarketCapCategories(CATEGORIES_BASE_URL);
    }
}
