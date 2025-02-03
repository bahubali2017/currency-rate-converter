package pl.cleankod.exchange.entrypoint.model;

import java.time.LocalDateTime;

public record ApiError(int statusCode, String message, String details,LocalDateTime timestamp) {
	// Constructor to initialize timestamp with the current time
    public ApiError(int statusCode, String message, String details) {
        this(statusCode, message, details, LocalDateTime.now()); 
    }

        

}
