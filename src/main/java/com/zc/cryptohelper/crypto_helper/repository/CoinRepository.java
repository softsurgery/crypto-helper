package com.zc.cryptohelper.crypto_helper.repository;

import com.zc.cryptohelper.crypto_helper.models.Coin;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CoinRepository extends CrudRepository<Coin, Long> {
    List<Coin> findAll();
    Optional<Coin> findByName(String name);
}
