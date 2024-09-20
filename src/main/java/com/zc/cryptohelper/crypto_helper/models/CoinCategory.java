package com.zc.cryptohelper.crypto_helper.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@Data
public class CoinCategory {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 1024)
    private String url;
}
