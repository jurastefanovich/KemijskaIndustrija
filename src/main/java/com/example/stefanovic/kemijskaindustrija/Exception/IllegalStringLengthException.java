package com.example.stefanovic.kemijskaindustrija.Exception;

public class IllegalStringLengthException extends Exception{
    public IllegalStringLengthException() {
    }

    public IllegalStringLengthException(String message) {
        super(message);
    }

    public IllegalStringLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalStringLengthException(Throwable cause) {
        super(cause);
    }
}
