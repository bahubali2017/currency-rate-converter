package pl.cleankod.exchange.provider;

import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import pl.cleankod.exchange.core.domain.Money;
import pl.cleankod.exchange.core.gateway.CurrencyConversionService;
import pl.cleankod.exchange.provider.nbp.ExchangeRatesNbpClient;
import pl.cleankod.exchange.provider.nbp.model.RateWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public class CurrencyConversionNbpService implements CurrencyConversionService {

    private final Logger logger = LoggerFactory.getLogger(CurrencyConversionNbpService.class);

    private final ExchangeRatesNbpClient exchangeRatesNbpClient;

    public CurrencyConversionNbpService(ExchangeRatesNbpClient exchangeRatesNbpClient) {
        this.exchangeRatesNbpClient = exchangeRatesNbpClient;
    }

    @Override
    public Money convert(Money money, Currency targetCurrency) {

        RateWrapper rateWrapper = getExchangeRate(targetCurrency);
        BigDecimal midRate = rateWrapper.rates().get(0).mid();
        BigDecimal calculatedRate = money.amount().divide(midRate, 4, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        logger.debug("CurrencyConversionNbpService-convert-{}-{}-{}", money.amount(), targetCurrency.getCurrencyCode(), calculatedRate);
        return new Money(calculatedRate, targetCurrency);
    }

    @Retry(name = "nbpApi")
    @Cacheable(value = "currencyConversionCache", key = "#targetCurrency", unless = "#result == null")
    public RateWrapper getExchangeRate(Currency targetCurrency) {
        return exchangeRatesNbpClient.fetch("A", targetCurrency.getCurrencyCode());
    }


}
