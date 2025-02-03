package pl.cleankod.exchange.entrypoint.model;

public class NbpApiException extends RuntimeException {
    public NbpApiException(String message) {
        super(message);
    }
}
