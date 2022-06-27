package com.nttdata.bootcoin.controller;

import com.nttdata.bootcoin.business.ExchangeService;
import com.nttdata.bootcoin.model.mongo.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/exchange")
@CrossOrigin(origins = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
public class ExchangeController {

    @Autowired
    ExchangeService service;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Exchange> getAllProducts() {
        return service.getExchanges();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Exchange> getProduct(@PathVariable String id) {
        return service.getExchange(id);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Exchange> createProduct(@RequestBody Exchange exchange) {
        return service.insertExchange(exchange);
    }

    @PostMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Exchange> modifyProduct(@RequestBody Exchange exchange, @PathVariable String id) {
        return service.updateExchange(exchange, id);
    }

    @PostMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> removeProduct(@PathVariable String id) {
        return service.deleteExchange(id);
    }

}
