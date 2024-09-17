package com.zc.cryptohelper.crypto_helper.controller;

import com.zc.cryptohelper.crypto_helper.payload.JwtAuthenticationResponse;
import com.zc.cryptohelper.crypto_helper.payload.LoginRequest;
import com.zc.cryptohelper.crypto_helper.payload.SignupRequest;
import com.zc.cryptohelper.crypto_helper.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {


    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticateUser(loginRequest));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signup(@Valid @RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService.registerUser(signUpRequest));
    }
}
