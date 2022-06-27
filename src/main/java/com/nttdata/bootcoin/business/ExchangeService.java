package com.nttdata.bootcoin.business;

import com.nttdata.bootcoin.model.mongo.Exchange;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeService {

    Flux<Exchange> getExchanges();

    Mono<Exchange> getExchange(String id);

    Mono<Exchange> insertExchange(Exchange exchange);

    Mono<Exchange> updateExchange(Exchange exchange, String id);

    Mono<Void> deleteExchange(String id);

}
