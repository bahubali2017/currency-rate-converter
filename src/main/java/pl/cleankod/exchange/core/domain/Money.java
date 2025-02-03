package pl.cleankod.exchange.core.domain;

import java.math.BigDecimal;
import java.util.Currency;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import pl.cleankod.util.Preconditions;

public record Money( @JsonProperty("amount")BigDecimal amount,  @JsonProperty("currency") Currency currency) {
	
	 @JsonCreator
	  public Money(
	            @JsonProperty("amount") BigDecimal amount,
	            @JsonProperty("currency") Currency currency) {
	        this.amount =amount;
	        this.currency =currency;
	    }
    public static Money of(BigDecimal amount, Currency currency) {
        Preconditions.requireNonNull(amount);
        Preconditions.requireNonNull(currency);
        return new Money(amount, currency);
    }
    public static Money of(String amount, String currency) {
        Preconditions.requireNonNull(amount);
        Preconditions.requireNonNull(currency);
        return new Money(new BigDecimal(amount), Currency.getInstance(currency));
    }

}
