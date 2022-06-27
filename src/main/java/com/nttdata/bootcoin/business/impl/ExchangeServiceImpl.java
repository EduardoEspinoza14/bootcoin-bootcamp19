package com.nttdata.bootcoin.business.impl;

import com.nttdata.bootcoin.business.ExchangeService;
import com.nttdata.bootcoin.model.mongo.Exchange;
import com.nttdata.bootcoin.repository.ExchangeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnProperty (name = "cache.enabled", havingValue = "false", matchIfMissing = true)
public class ExchangeServiceImpl implements ExchangeService {

    private ExchangeRepository exchangeRepository;

    @Override
    public Mono<Exchange> getExchange(String id) {
        return exchangeRepository.findById(id);
    }

    @Override
    public Mono<Exchange> insertExchange(Exchange exchange) {
        return exchangeRepository.insert(exchange);
    }

    @Override
    public Mono<Exchange> updateExchange(Exchange exchange, String id) {
        return exchangeRepository.findById(id)
                .map(exchangeMongo -> {
                    BeanUtils.copyProperties(exchange, exchangeMongo, "id");
                    return exchangeMongo;
                })
                .flatMap(exchangeRepository::save);
    }

    @Override
    public Mono<Void> deleteExchange(String id) {
        return exchangeRepository.findById(id)
                .flatMap(p -> exchangeRepository.deleteById(p.getId()));
    }
}
