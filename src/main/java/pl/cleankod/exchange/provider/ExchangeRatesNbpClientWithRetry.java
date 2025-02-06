package pl.cleankod.exchange.provider;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import pl.cleankod.exchange.provider.nbp.ExchangeRatesNbpClient;
import pl.cleankod.exchange.provider.nbp.model.RateWrapper;


public class ExchangeRatesNbpClientWithRetry implements ExchangeRatesNbpClient {
    private final ExchangeRatesNbpClient exchangeRatesNbpClient;

    public ExchangeRatesNbpClientWithRetry(ExchangeRatesNbpClient exchangeRatesNbpClient) {
        this.exchangeRatesNbpClient = exchangeRatesNbpClient;
    }


    @Retry(name = "nbpApi")
    public RateWrapper fetch(String table, String targetCurrency) {
        return exchangeRatesNbpClient.fetch(table, targetCurrency);
    }

}
