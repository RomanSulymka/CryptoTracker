package com.testproject.oril.repository;

import com.testproject.oril.domain.Cryptocurrency;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRepository extends MongoRepository<Cryptocurrency, ObjectId> {

    Cryptocurrency findFirstByCryptoNameOrderByPriceAsc(String cryptoName);

    Cryptocurrency findFirstByCryptoNameOrderByPriceDesc(String cryptoName);
}
