package com.microservices.currencyconversionservice.controller;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "currency-exchange",url="localhost:8000")
@FeignClient(name = "currency-exchange") //This is for the eureka to load balance just by removing the URL.
public interface CurrencyExchangeProxy {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyConversion retreiveExchangeValue(@PathVariable String from, @PathVariable String to);
}
