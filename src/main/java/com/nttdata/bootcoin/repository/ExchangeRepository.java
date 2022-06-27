package com.nttdata.bootcoin.repository;

import com.nttdata.bootcoin.model.mongo.Exchange;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends ReactiveMongoRepository<Exchange, String> {

}
