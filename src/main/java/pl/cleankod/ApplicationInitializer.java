package pl.cleankod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

import pl.cleankod.exchange.core.config.CurrencyRateConvertorConfig;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableCaching
@Import(CurrencyRateConvertorConfig.class)
public class ApplicationInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializer.class, args);
    }
}
