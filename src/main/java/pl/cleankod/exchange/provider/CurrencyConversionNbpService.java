package pl.cleankod.exchange.provider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import pl.cleankod.exchange.core.domain.Money;
import pl.cleankod.exchange.core.gateway.CurrencyConversionService;
import pl.cleankod.exchange.entrypoint.model.NbpApiException;
import pl.cleankod.exchange.provider.nbp.ExchangeRatesNbpClient;
import pl.cleankod.exchange.provider.nbp.model.RateWrapper;

public class CurrencyConversionNbpService implements CurrencyConversionService {
	
	private final Logger logger = LoggerFactory.getLogger(CurrencyConversionNbpService.class);
	
    private final ExchangeRatesNbpClient exchangeRatesNbpClient;

    public CurrencyConversionNbpService(ExchangeRatesNbpClient exchangeRatesNbpClient) {
        this.exchangeRatesNbpClient = exchangeRatesNbpClient;
    }

    @CircuitBreaker(name = "nbpApi", fallbackMethod = "fallbackGetExchangeRates")
    public Money convert(Money money, Currency targetCurrency){
    	 	
        RateWrapper rateWrapper = getExchangeRate(targetCurrency);
        LocalDateTime localDateTime = null;
        BigDecimal midRate = rateWrapper.rates().get(0).mid();
        // we can choose RoundingMode.DOWN and scale =2  to avoid loosing money.
        BigDecimal calculatedRate = money.amount().divide(midRate, 2, RoundingMode.DOWN);
        logger.debug("CurrencyConversionNbpService-convert-{}-{}-{}",money.amount(),targetCurrency.getCurrencyCode(),calculatedRate);
        return new Money(calculatedRate, targetCurrency);
    }
    @Retry(name = "nbpApi")
    @Cacheable(value = "currencyConversionCache", key = "#targetCurrency", unless = "#result == null")
	public RateWrapper getExchangeRate(Currency targetCurrency) {
    	
		RateWrapper rateWrapper = exchangeRatesNbpClient.fetch("A", targetCurrency.getCurrencyCode());
		return rateWrapper;
	}
    
    
}
