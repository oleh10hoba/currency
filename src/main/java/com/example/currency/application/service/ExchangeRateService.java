package com.example.currency.application.service;

import com.example.currency.ui.dto.response.ExchangeRateResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateService {
    
    private final RestTemplate restTemplate;
    private final String apiUrl;

    public ExchangeRateService(RestTemplate restTemplate, @Value("${exchangeratesapi.baseurl}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public ExchangeRateResponseDto exchangeRateList(String symbol) {
        String url = apiUrl + "/latest/" + symbol;
        return restTemplate.getForObject(url, ExchangeRateResponseDto.class);
    }
}
