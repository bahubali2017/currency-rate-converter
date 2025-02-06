package pl.cleankod.exchange.provider;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeRateService {


    private final ExchangeRatesNbpClientWithRetry exchangeRatesNbpClientWithRetry;

    public ExchangeRateService(ExchangeRatesNbpClientWithRetry exchangeRatesNbpClientWithReTry) {
        this.exchangeRatesNbpClientWithRetry = exchangeRatesNbpClientWithReTry;
    }


    @Cacheable(value = "currencyConversionCache", key = "#targetCurrency", unless = "#result == null")
    public BigDecimal getMidExchangeRate(String table , String targetCurrency) {
        return exchangeRatesNbpClientWithRetry.fetch(table, targetCurrency).rates().get(0).mid();
    }
}
