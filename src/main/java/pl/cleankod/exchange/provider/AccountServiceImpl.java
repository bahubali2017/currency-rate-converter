package pl.cleankod.exchange.provider;

import java.util.Currency;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import pl.cleankod.exchange.core.domain.Account;
import pl.cleankod.exchange.core.usecase.FindAccountAndConvertCurrencyUseCase;
import pl.cleankod.exchange.core.usecase.FindAccountUseCase;

@Service
public class AccountServiceImpl {
	
	private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	@Autowired
	private FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase ;
	@Autowired
    private FindAccountUseCase findAccountUseCase ;
	
	public AccountServiceImpl(FindAccountAndConvertCurrencyUseCase findAccountAndConvertCurrencyUseCase, FindAccountUseCase findAccountUseCase) {
		this.findAccountAndConvertCurrencyUseCase = findAccountAndConvertCurrencyUseCase;
		this.findAccountUseCase = findAccountUseCase;
	}
    
	public  ResponseEntity<Account> findAccountById(String id, String currency) {
		
		logger.debug("AccountServiceImpl-findAccountById-{}-{}",id,currency);
		return Optional.ofNullable(currency)
                .map(s ->
                        findAccountAndConvertCurrencyUseCase.execute(Account.Id.of(id), Currency.getInstance(s))
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build())
                )
                .orElseGet(() ->
                        findAccountUseCase.execute(Account.Id.of(id))
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build())
                );
	}
	
	public ResponseEntity<Account> findAccountByNumber(String currency, Account.Number accountNumber) {
		return Optional.ofNullable(currency)
                .map(s ->
                        findAccountAndConvertCurrencyUseCase.execute(accountNumber, Currency.getInstance(s))
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build())
                )
                .orElseGet(() ->
                        findAccountUseCase.execute(accountNumber)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build())
                );
	}
}
