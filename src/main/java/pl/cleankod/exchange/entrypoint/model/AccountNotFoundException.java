package pl.cleankod.exchange.entrypoint.model;

public class AccountNotFoundException extends NbpApiException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
