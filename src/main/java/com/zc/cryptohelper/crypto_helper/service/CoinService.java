package com.zc.cryptohelper.crypto_helper.service;

import com.zc.cryptohelper.crypto_helper.exceptions.ResourceNotFoundException;
import com.zc.cryptohelper.crypto_helper.models.Coin;
import com.zc.cryptohelper.crypto_helper.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoinService {
    @Autowired
    CoinRepository coinRepository;

    public Coin saveOrUpdateCoin(Coin coin){
        return coinRepository.save(coin);
    }

    public List<Coin> findAllCoins(){
        return coinRepository.findAll();
    }

    public Coin findCoinByName(String name){
        Coin coin = coinRepository.findByName(name).orElseThrow(
                ()-> new ResourceNotFoundException("Coin","name",name));
        return coin;
    }
}