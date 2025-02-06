package pl.cleankod.exchange.core.config;

import feign.Feign;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import pl.cleankod.exchange.core.gateway.AccountMapper;
import pl.cleankod.exchange.core.gateway.AccountRepository;
import pl.cleankod.exchange.core.gateway.CurrencyConversionService;
import pl.cleankod.exchange.core.usecase.FindAccountAndConvertCurrencyUseCase;
import pl.cleankod.exchange.core.usecase.FindAccountUseCase;
import pl.cleankod.exchange.entrypoint.AccountController;
import pl.cleankod.exchange.entrypoint.ExceptionHandlerAdvice;
import pl.cleankod.exchange.provider.*;
import pl.cleankod.exchange.provider.nbp.ExchangeRatesNbpClient;

import java.util.Currency;

@Configuration
public class CurrencyRateConvertorConfig {
    @Bean
    AccountRepository accountRepository() {
        return new AccountInMemoryRepository();
    }

    @Bean
    ExchangeRatesNbpClient exchangeRatesNbpClient(Environment environment) {
        String nbpApiBaseUrl = environment.getRequiredProperty("provider.nbp-api.base-url");
        return Feign.builder()
                .client(new ApacheHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(ExchangeRatesNbpClient.class, nbpApiBaseUrl);
    }

    @Bean
    ExchangeRateService exchangeRateService(ExchangeRatesNbpClientWithRetry exchangeRatesNbpClientWithReTry) {
        return new ExchangeRateService(exchangeRatesNbpClientWithReTry);
    }

    @Bean
    ExchangeRatesNbpClientWithRetry exchangeRatesNbpClientWithRetry(ExchangeRatesNbpClient exchangeRatesNbpClient) {
        return new ExchangeRatesNbpClientWithRetry(exchangeRatesNbpClient);
    }

    @Bean
    CurrencyConversionService currencyConversionService(ExchangeRateService exchangeRateService) {
        return new CurrencyConversionNbpService(exchangeRateService);
    }

    @Bean
    FindAccountUseCase findAccountUseCase(AccountRepository accountRepository) {
        return new FindAccountUseCase(accountRepository);
    }

    @Bean
    FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase(
            AccountRepository accountRepository,
            CurrencyConversionService currencyConversionService,
            Environment environment
    ) {
        Currency baseCurrency = Currency.getInstance(environment.getRequiredProperty("app.base-currency"));
        return new FindAccountAndConvertCurrencyUseCase(accountRepository, currencyConversionService, baseCurrency);
    }

    @Bean
    AccountController accountController() {
        return new AccountController();
    }

    @Bean
    AccountServiceImpl accountServiceImpl(FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase,
                                          FindAccountUseCase findAccountUseCase, AccountMapper accountMapper) {
        return new AccountServiceImpl(findAccountAndConvertCurrencyUseCase, findAccountUseCase, accountMapper);
    }

    @Bean
    ExceptionHandlerAdvice exceptionHandlerAdvice() {
        return new ExceptionHandlerAdvice();
    }



}
