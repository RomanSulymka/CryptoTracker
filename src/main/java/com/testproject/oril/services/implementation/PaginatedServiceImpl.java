package com.testproject.oril.services.implementation;

import com.testproject.oril.domain.Cryptocurrency;
import com.testproject.oril.repository.PagingRepository;
import com.testproject.oril.services.PaginatedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaginatedServiceImpl implements PaginatedService {

    private final PagingRepository pagingRepository;

    public PaginatedServiceImpl(PagingRepository pagingRepository) {
        this.pagingRepository = pagingRepository;
    }

    @Override
    public List<Cryptocurrency> getRecordsWithPagination(String cryptoName, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("price"));
        Page<Cryptocurrency> cryptocurrencyPage = pagingRepository.getCryptocurrencyByCryptoName(pageable, cryptoName);
        if (cryptocurrencyPage.hasContent()) {
            return cryptocurrencyPage.getContent();
        }
        return new ArrayList<>();
    }
}
