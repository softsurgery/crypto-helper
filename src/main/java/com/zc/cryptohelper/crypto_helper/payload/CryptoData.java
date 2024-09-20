package com.zc.cryptohelper.crypto_helper.payload;

import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
public class CryptoData {
    private int rank;
    private String code;
    private String name;
    private float price;
    private float raise1Hour;
    private float raise24Hours;
    private float raise7Days;
    private String marketCap;
    private String volume24Hour;
    private String circulatingSupply;
    private String pictureURL;
}
