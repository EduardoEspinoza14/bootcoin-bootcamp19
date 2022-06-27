package com.nttdata.bootcoin.business.impl;

import com.nttdata.bootcoin.business.CustomerService;
import com.nttdata.bootcoin.business.ProductService;
import com.nttdata.bootcoin.configuration.KafkaProducerConfiguration;
import com.nttdata.bootcoin.model.dto.Customer;
import com.nttdata.bootcoin.model.dto.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderOptions;

import java.util.Date;

/**
 * Class ProductServiceImpl.
 */
@Service
public class ProductServiceImpl implements ProductService {

  private static final String CIRCUIT_BREAKER_SERVICE_PRODUCT = "cbServiceProduct";

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  @Autowired
  CustomerService customerService;

  @Autowired
  private SenderOptions<String, Product> senderOptions;

  @Override
  public Mono<Void> registerWallet(Product product) {
    product.setType(Product.PRODUCT_TYPE_6);
    product.setTypeWallet(Product.WALLET_TYPE_2);
    product.setAmount(0.0);
    product.setStartDate(new Date());
    return Mono.justOrEmpty(product)
            .defaultIfEmpty(new Product(new Customer()))
            .map(Product::getCustomer)
            .flatMap(customer -> customerService.getCustomerById(customer.getId()))
            .doOnNext(product::setCustomer)
            .then(KafkaProducerConfiguration.senderCreate(senderOptions,
                            KafkaProducerConfiguration.insertRecord(product))
                    .then());
  }

}
