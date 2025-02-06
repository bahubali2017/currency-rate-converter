package pl.cleankod.exchange.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.cleankod.exchange.core.domain.Account;
import pl.cleankod.exchange.core.gateway.AccountDto;
import pl.cleankod.exchange.core.gateway.AccountMapper;
import pl.cleankod.exchange.core.usecase.FindAccountAndConvertCurrencyUseCase;
import pl.cleankod.exchange.core.usecase.FindAccountUseCase;

import java.util.Currency;
import java.util.Optional;


public class AccountServiceImpl {

    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private final FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase;
    @Autowired
    private final FindAccountUseCase findAccountUseCase;

    private final AccountMapper accountMapper;



    public AccountServiceImpl(FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase,
                              FindAccountUseCase findAccountUseCase, AccountMapper accountMapper) {
        this.findAccountAndConvertCurrencyUseCase = findAccountAndConvertCurrencyUseCase;
        this.findAccountUseCase = findAccountUseCase;
        this.accountMapper = accountMapper ;
    }

    public ResponseEntity<AccountDto> findAccountById(String id, String currency) {

        logger.debug("AccountServiceImpl-findAccountById-{}-{}", id, currency);
        return Optional.ofNullable(currency)
                .map(s ->
                        findAccountAndConvertCurrencyUseCase.execute(Account.Id.of(id), Currency.getInstance(s))
                                .map(accountMapper::toAccountDto)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build())
                )
                .orElseGet(() ->
                        findAccountUseCase.execute(Account.Id.of(id))
                                .map(accountMapper::toAccountDto)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build())
                );
    }

    public ResponseEntity<AccountDto> findAccountByNumber(String currency, Account.Number accountNumber) {
        logger.debug("AccountServiceImpl-findAccountByNumber-{}-{}", accountNumber.value(), currency);
        return Optional.ofNullable(currency)
                .map(s ->
                        findAccountAndConvertCurrencyUseCase.execute(accountNumber, Currency.getInstance(s))
                                .map(accountMapper::toAccountDto)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build())
                )
                .orElseGet(() ->
                        findAccountUseCase.execute(accountNumber)
                                .map(accountMapper::toAccountDto)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build())
                );
    }
}
