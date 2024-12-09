package com.zc.cryptohelper.crypto_helper.controller;


import com.zc.cryptohelper.crypto_helper.models.Coin;
import com.zc.cryptohelper.crypto_helper.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/coins")
public class CoinController {
    private final CoinService coinService;

    @Autowired
    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    // Get all roles
    @GetMapping
    public ResponseEntity<List<Coin>> getAllCoins() {
        return ResponseEntity.ok(coinService.getAllCoins());
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Coin>> findCoins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String order
    ) {
        Page<Coin> coins = coinService.findCoins(page, size, search, sort, order);
        return ResponseEntity.ok(coins);
    }

    // Get role by ID
    @GetMapping("/{id}")
    public ResponseEntity<Coin> getRoleById(@PathVariable Long id) {
        return coinService.getCoinById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new role
    @PostMapping
    public ResponseEntity<Coin> createRole(@RequestBody Coin role) {
        return ResponseEntity.ok(coinService.saveCoin(role));
    }

    // Update an existing role
    @PutMapping("/{id}")
    public ResponseEntity<Coin> updateRole(@PathVariable Long id, @RequestBody Coin updatedRole) {
        return coinService.getCoinById(id)
                .map(existingCoin -> {
                    existingCoin.setName(updatedRole.getName());
                    existingCoin.getHrefName().equals(updatedRole.getHrefName());
                    existingCoin.setUpload(updatedRole.getUpload());
                    return ResponseEntity.ok(coinService.saveCoin(existingCoin));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a role by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        if (coinService.getCoinById(id).isPresent()) {
            coinService.deleteCoin(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
