package pl.cleankod.exchange.provider;

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

    private final ExchangeRateService exchangeRateService;

    public CurrencyConversionNbpService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public Money convert(Money money, Currency targetCurrency) {

        BigDecimal midExchangeRate = getMidExchangeRate(targetCurrency);
        BigDecimal calculatedRate = money.amount().divide(midExchangeRate, 4, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        logger.debug("CurrencyConversionNbpService-convert-{}-{}-{}", money.amount(), targetCurrency.getCurrencyCode(), calculatedRate);
        return new Money(calculatedRate, targetCurrency);
    }


    private BigDecimal getMidExchangeRate(Currency targetCurrency) {
        return exchangeRateService.getMidExchangeRate("A", targetCurrency.getCurrencyCode());
    }


}
