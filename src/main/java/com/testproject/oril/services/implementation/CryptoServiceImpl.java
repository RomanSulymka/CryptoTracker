package com.testproject.oril.services.implementation;

import com.testproject.oril.domain.Cryptocurrency;
import com.testproject.oril.repository.CryptoRepository;
import com.testproject.oril.services.CryptoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoServiceImpl implements CryptoService {

    private final CryptoRepository cryptoRepository;

    public CryptoServiceImpl(CryptoRepository cryptoRepository) {
        this.cryptoRepository = cryptoRepository;
    }

    @Override
    public List<Cryptocurrency> getAll() {
        return cryptoRepository.findAll();
    }

    @Override
    public Cryptocurrency getCryptocurrencyWithHighPrice(String name) {
        return cryptoRepository.findFirstByCryptoNameOrderByPriceDesc(name);
    }

    @Override
    public Cryptocurrency getCryptocurrencyWithMinPrice(String name) {
        return cryptoRepository.findFirstByCryptoNameOrderByPriceAsc(name);
    }

    @Override
    public void save(Cryptocurrency cryptocurrency) {
        if (cryptocurrency != null){
            cryptoRepository.insert(cryptocurrency);
        }
    }
}
