package com.zc.cryptohelper.crypto_helper.controller;

import com.zc.cryptohelper.crypto_helper.models.Coin;
import com.zc.cryptohelper.crypto_helper.service.CoinService;
import com.zc.cryptohelper.crypto_helper.service.MapValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/coins")
public class CoinController {
    @Autowired
    CoinService coinService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createStudent(@Valid @RequestBody Coin coin, BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidations(result);
        if (errorMap!=null) return errorMap;

        Coin newCoin = coinService.saveOrUpdateCoin(coin);
        return new ResponseEntity<>(newCoin, HttpStatus.CREATED);
    }

    @GetMapping("")
    ResponseEntity<?> getAllStudents(){
        List<Coin> coins = coinService.findAllCoins();
        return new ResponseEntity<>(coins,HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getStudentByName(@PathVariable String name){
        Coin coin = coinService.findCoinByName(name);
        return new ResponseEntity<>(coin,HttpStatus.OK);
    }

}
