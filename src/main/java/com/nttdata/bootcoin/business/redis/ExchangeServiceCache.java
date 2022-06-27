package com.nttdata.bootcoin.business.redis;

import com.nttdata.bootcoin.business.impl.ExchangeServiceImpl;
import com.nttdata.bootcoin.model.mongo.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
public class ExchangeServiceCache extends ExchangeServiceImpl {

    private final Logger log = LoggerFactory.getLogger(ExchangeServiceCache.class);

    private static final String KEY_CACHE = "exchanges";

    @Autowired
    private ReactiveHashOperations<String, String, Exchange> hashOperations;

    @Override
    public Mono<Exchange> getExchange(String id) {
        return hashOperations.get(KEY_CACHE, id)
                .switchIfEmpty(this.getExchangeSaveCacheRedis(id));
    }

    @Override
    public Mono<Exchange> insertExchange(Exchange exchange) {
        return super.insertExchange(exchange)
                .flatMap(this::saveCacheRedis);
    }

    @Override
    public Mono<Exchange> updateExchange(Exchange exchange, String id) {
        return this.hashOperations.remove(KEY_CACHE, id)
                .then(super.updateExchange(exchange, id))
                .flatMap(this::saveCacheRedis);
    }

    @Override
    public Mono<Void> deleteExchange(String id) {
        return this.hashOperations.remove(KEY_CACHE, id)
                .then(super.deleteExchange(id));
    }

    private Mono<Exchange> getExchangeSaveCacheRedis(String id) {
        return super.getExchange(id)
                .flatMap(this::saveCacheRedis);
    }

    private Mono<Exchange> saveCacheRedis(Exchange exchange) {
        log.info("REDIS CACHE EXCHANGE: {}", exchange);
        return this.hashOperations.put(KEY_CACHE,
                        exchange.getId(),
                        exchange)
                .thenReturn(exchange);
    }


}
