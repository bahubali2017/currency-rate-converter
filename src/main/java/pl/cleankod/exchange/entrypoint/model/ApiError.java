package pl.cleankod.exchange.entrypoint.model;

public record ApiError(int statusCode, String details, String message) {

}
