package com.zc.cryptohelper.crypto_helper.repository;

import com.zc.cryptohelper.crypto_helper.models.Coin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long>, JpaSpecificationExecutor<Coin> {
    Page<Coin> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Coin> findByName(String name);
    Optional<Coin> findByHrefName(String hrefName);


}
