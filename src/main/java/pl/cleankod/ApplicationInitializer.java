package pl.cleankod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import pl.cleankod.exchange.core.config.CurrencyRateConvertorConfig;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableCaching
@Import(CurrencyRateConvertorConfig.class)
@ComponentScan(basePackages = {"pl.cleankod.exchange.core.gateway"})
public class ApplicationInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationInitializer.class, args);
    }
}
