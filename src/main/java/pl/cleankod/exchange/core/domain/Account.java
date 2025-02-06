package pl.cleankod.exchange.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import pl.cleankod.util.Preconditions;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;
import java.util.regex.Pattern;

public record Account(@JsonUnwrapped Id id, @JsonUnwrapped Number number, @JsonUnwrapped Money balance) {

    @JsonCreator
    public Account(@JsonProperty("Id") UUID id,
                   @JsonProperty("number") String number,
                   @JsonProperty("amount") BigDecimal amount,
                   @JsonProperty("currency") Currency currency) {
        this(new Id(id), new Number(number), new Money(amount, currency));
    }

    public record Id(UUID value) {
        @JsonCreator
        public Id(@JsonProperty("Id") UUID value) {
            Preconditions.requireNonNull(value);
            this.value = value;
        }

        public static Id of(UUID value) {
            return new Id(value);
        }

        public static Id of(String value) {
            Preconditions.requireNonNull(value);
            return new Id(UUID.fromString(value));
        }
    }

    public record Number(String value) {
        private static final Pattern PATTERN =
                Pattern.compile("\\d{2}[ ]?\\d{4}[ ]?\\d{4}[ ]?\\d{4}[ ]?\\d{4}[ ]?\\d{4}[ ]?\\d{4}");

        @JsonCreator
        public Number {
            Preconditions.requireNonNull(value);
            if (! PATTERN.matcher(value).matches()) {
                throw new IllegalArgumentException("The account number does not match the pattern: " + PATTERN);
            }
        }

        public static Number of(String value) {
            return new Number(value);
        }
    }
}
