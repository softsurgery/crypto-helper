package com.zc.cryptohelper.crypto_helper.controller;

import com.zc.cryptohelper.crypto_helper.models.CoinCategory;
import com.zc.cryptohelper.crypto_helper.service.CoinCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/coin-categories")
public class CoinCategoryController {

    @Autowired
    private CoinCategoryService coinCategoryService;

    // Endpoint to create a new Coin Category
    @PostMapping("/create")
    public ResponseEntity<CoinCategory> createCoinCategory(@RequestBody CoinCategory coinCategory) {
        CoinCategory newCategory = coinCategoryService.createCoinCategory(coinCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    // Endpoint to get all Coin Categories
    @GetMapping("/all")
    public ResponseEntity<List<CoinCategory>> getAllCoinCategories() {
        List<CoinCategory> categories = coinCategoryService.findAllCoinCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
}