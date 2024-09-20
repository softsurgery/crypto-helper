package com.zc.cryptohelper.crypto_helper.repository;

import com.zc.cryptohelper.crypto_helper.models.CoinCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CoinCategoryRepository extends CrudRepository<CoinCategory, Long> {
    List<CoinCategory> findAll();
}
