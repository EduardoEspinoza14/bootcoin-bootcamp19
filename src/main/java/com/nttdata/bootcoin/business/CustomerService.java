package com.nttdata.bootcoin.business;

import com.nttdata.bootcoin.model.dto.Customer;
import reactor.core.publisher.Mono;

/**
 * Interface CustomerService.
 */
public interface CustomerService {

    Mono<Customer> getCustomerById(String id);

}
