package com.testproject.oril.services;

import com.testproject.oril.domain.Cryptocurrency;

import java.util.List;

public interface PaginatedService {
    List<Cryptocurrency> getRecordsWithPagination(String cryptoName, Integer page, Integer size);
}
