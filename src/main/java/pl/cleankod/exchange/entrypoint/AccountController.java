package pl.cleankod.exchange.entrypoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.cleankod.exchange.core.domain.Account;
import pl.cleankod.exchange.entrypoint.model.AccountNotFoundException;
import pl.cleankod.exchange.entrypoint.model.ApiError;
import pl.cleankod.exchange.provider.AccountServiceImpl;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    public static final String SUCCESS_RESPONSE = "200";
    public static final String BAD_REQUEST_RESPONSE = "400";
    public static final String NOT_FOUND_RESPONSE = "404";
    @Autowired
    private AccountServiceImpl accountServiceImpl;

    public AccountController() {

    }

    @Operation(summary = "Find account by ID", description = "Retrieves an account using its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = SUCCESS_RESPONSE, description = "Successful response",
                    content = @Content(schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = BAD_REQUEST_RESPONSE, description = "Bad request - Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE, description = "Account not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> findAccountById(@PathVariable String id, @RequestParam(required = false) String currency) {
        ResponseEntity<Account> accountDetailsbyId = accountServiceImpl.findAccountById(id, currency);
        if (String.valueOf(accountDetailsbyId.getStatusCode().value()).equals(NOT_FOUND_RESPONSE)) {
            throw new AccountNotFoundException("Account not found");
        }
        return accountDetailsbyId;
    }

    @Operation(summary = "Find account by number", description = "Retrieves an account using its account number.")
    @ApiResponses({
            @ApiResponse(responseCode = SUCCESS_RESPONSE, description = "Successful response",
                    content = @Content(schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = BAD_REQUEST_RESPONSE, description = "Bad request - Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = NOT_FOUND_RESPONSE, description = "Account not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(path = "/number={number}")
    public ResponseEntity<Account> findAccountByNumber(@PathVariable String number, @RequestParam(required = false) String currency) {
        Account.Number accountNumber = Account.Number.of(URLDecoder.decode(number, StandardCharsets.UTF_8));
        ResponseEntity<Account> accountDetailsByNumber = accountServiceImpl.findAccountByNumber(currency, accountNumber);
        if (String.valueOf(accountDetailsByNumber.getStatusCode().value()).equals(NOT_FOUND_RESPONSE)) {
            throw new AccountNotFoundException("Account not found");
        }
        return accountDetailsByNumber;
    }


}

