package pl.cleankod.exchange.core.gateway;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;



public record AccountDto(UUID id, String number, BigDecimal amount, Currency currency) {
}
