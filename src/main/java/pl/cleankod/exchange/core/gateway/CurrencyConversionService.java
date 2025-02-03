package pl.cleankod.exchange.core.gateway;

import java.util.Currency;

import pl.cleankod.exchange.core.domain.Money;

public interface CurrencyConversionService {
    Money convert(Money money, Currency targetCurrency) ;
}
