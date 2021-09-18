package com.testproject.oril.repository;

import com.testproject.oril.domain.Cryptocurrency;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PagingRepository extends PagingAndSortingRepository<Cryptocurrency, ObjectId> {
    Page<Cryptocurrency> getCryptocurrencyByCryptoName(Pageable page, String cryptoName);
}
