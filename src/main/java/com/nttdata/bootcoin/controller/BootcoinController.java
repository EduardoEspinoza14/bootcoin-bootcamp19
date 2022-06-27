package com.nttdata.bootcoin.controller;

import com.nttdata.bootcoin.business.ProductService;
import com.nttdata.bootcoin.model.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bootcoin")
@CrossOrigin(origins = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
public class BootcoinController {

    @Autowired
    ProductService productService;

    @PostMapping(value = "/register-wallet",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> registerWallet(@RequestBody Product product) {
        return productService.registerWallet(product);
    }

}
