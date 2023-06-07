package com.microservices.currencyconversionservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy currencyExchangeProxy;
    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateconversion(@PathVariable String from,
                                                  @PathVariable String to, @PathVariable BigDecimal quantity){
        HashMap<String,String> uriVariables = new HashMap<>();
        uriVariables.put("from",from);
        uriVariables.put("to",to);
        ResponseEntity<CurrencyConversion> forEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class,
                uriVariables);

        CurrencyConversion body = forEntity.getBody();

        return new CurrencyConversion (body.getId(), from, to,
                body.getConversionMultiple(), quantity,
                quantity.multiply(body.getConversionMultiple()), body.getEnvironment()+ "" + "Rest Template");
    }
    //Know the above is a call used to make to the other microservice and get data from it. now the above is quite complex,
    //hence spring cloud has introduced a FEIGN Rest Client
    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateconversionfeign(@PathVariable String from,
                                                  @PathVariable String to, @PathVariable BigDecimal quantity){

        CurrencyConversion body = currencyExchangeProxy.retreiveExchangeValue(from, to);

        return new CurrencyConversion (body.getId(), from, to, body.getConversionMultiple(),
                quantity,
                quantity.multiply(body.getConversionMultiple()), body.getEnvironment() + " " + "Feign");
    }
}
