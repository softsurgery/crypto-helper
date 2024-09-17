package com.zc.cryptohelper.crypto_helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CryptoHelperApplication {
	public static void main(String[] args) {
		SpringApplication.run(CryptoHelperApplication.class, args);
	}
}
