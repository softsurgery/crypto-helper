package com.zc.cryptohelper.crypto_helper.service;

import com.zc.cryptohelper.crypto_helper.models.CoinCategory;
import com.zc.cryptohelper.crypto_helper.repository.CoinCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoinCategoryService {
    @Autowired
    CoinCategoryRepository coinCategoryRepository;

    @Transactional
    public CoinCategory createCoinCategory(CoinCategory category) {
        return coinCategoryRepository.save(category);
    }

    public List<CoinCategory> findAllCoinCategories() {
        return coinCategoryRepository.findAll();
    }
}
