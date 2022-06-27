package com.nttdata.bootcoin.business.impl;

import com.nttdata.bootcoin.business.CustomerService;
import com.nttdata.bootcoin.model.dto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Class CustomerServiceImpl.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private static final String CIRCUIT_BREAKER_SERVICE_CUSTOMER = "cbServiceCustomer";

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${api.customer.baseUri}")
    private String baseUri;

    @Value("${api.customer.personUri}")
    private String personUri;

    @Value("${api.customer.companyUri}")
    private String companyUri;

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    ReactiveResilience4JCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @Override
    public Mono<Customer> getCustomerById(String id) {
        if (id == null || id.isEmpty() || id.trim().equals("")) {
            return Mono.empty();
        }
        return webClientBuilder.build()
                .get()
                .uri(baseUri + "/{customerId}", id)
                .retrieve()
                .bodyToMono(Customer.class)
                .transform(it ->
                        reactiveCircuitBreakerFactory.create(CIRCUIT_BREAKER_SERVICE_CUSTOMER)
                                .run(it, this::customerFallback)
                );
    }

    private Mono<Customer> customerFallback(Throwable e) {
        log.info("CUSTOMER SERVICE IS BREAKER - MONO");
        return Mono.empty();
    }

}
