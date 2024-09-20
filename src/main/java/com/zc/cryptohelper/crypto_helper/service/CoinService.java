package com.zc.cryptohelper.crypto_helper.service;

import com.zc.cryptohelper.crypto_helper.exceptions.ResourceNotFoundException;
import com.zc.cryptohelper.crypto_helper.models.Coin;
import com.zc.cryptohelper.crypto_helper.repository.CoinRepository;
import com.zc.cryptohelper.crypto_helper.storage.Upload;
import com.zc.cryptohelper.crypto_helper.storage.UploadService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CoinService {
    @Autowired
    CoinRepository coinRepository;
    @Autowired
    private UploadService uploadService;

    @Transactional
    public Coin createCoin(String name, Upload upload) {
        Coin coin = new Coin();
        coin.setName(name);
        coin.setUpload(upload);
        // Save the coin
        return coinRepository.save(coin);
    }

    public List<Coin> findAllCoins(){
        return coinRepository.findAll();
    }

    public Optional<Coin> findCoinByName(String name){
        return coinRepository.findByName(name);
    }
}