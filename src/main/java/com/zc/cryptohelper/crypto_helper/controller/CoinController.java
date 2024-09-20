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

@CrossOrigin
@RestController
@RequestMapping("/api/coins")
public class CoinController {
    @Autowired
    CoinService coinService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;


}
