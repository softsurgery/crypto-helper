package com.zc.cryptohelper.crypto_helper.service;

import com.zc.cryptohelper.crypto_helper.models.Coin;
import com.zc.cryptohelper.crypto_helper.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoinService {
    private final CoinRepository coinRepository;

    @Autowired
    public CoinService(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public Page<Coin> findCoins(int page, int size, String searchTerm, String sortKey, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortKey));
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return coinRepository.findByNameContainingIgnoreCase(searchTerm, pageRequest);
        }
        return coinRepository.findAll(pageRequest);
    }

    public List<Coin> getAllCoins() {
        return coinRepository.findAll();
    }

    public Optional<Coin> getCoinById(Long id) {
        return coinRepository.findById(id);
    }

    public Optional<Coin> getCoinByHrefName(String hrefName) {
        return coinRepository.findByHrefName(hrefName);
    }

    public Coin saveCoin(Coin permission) {
        return coinRepository.save(permission);
    }

    public void deleteCoin(Long id) {
        coinRepository.deleteById(id);
    }
}