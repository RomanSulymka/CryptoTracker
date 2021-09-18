package com.testproject.oril.services;

import com.testproject.oril.domain.Cryptocurrency;

import java.util.List;

public interface CryptoService {
    List<Cryptocurrency> getAll();

    Cryptocurrency getCryptocurrencyWithHighPrice(String cryptoName);

    Cryptocurrency getCryptocurrencyWithMinPrice(String cryptoName);

    void save(Cryptocurrency cryptocurrency);


}
