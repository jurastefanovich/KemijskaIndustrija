package com.example.stefanovic.kemijskaindustrija.Exception;

public class ServiceBookedForDateException extends RuntimeException{
    public ServiceBookedForDateException() {
    }

    public ServiceBookedForDateException(String message) {
        super(message);
    }

    public ServiceBookedForDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceBookedForDateException(Throwable cause) {
        super(cause);
    }
}
