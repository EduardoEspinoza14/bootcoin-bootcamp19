package com.nttdata.bootcoin.business;

import com.nttdata.bootcoin.model.dto.Product;
import reactor.core.publisher.Mono;

/**
 * Interface ProductService.
 */
public interface ProductService {

  Mono<Void> registerWallet(Product product);

}
