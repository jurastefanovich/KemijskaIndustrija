package com.example.stefanovic.kemijskaindustrija.Exception;

public class IllegalAccessLevelChangeException extends RuntimeException{
    public IllegalAccessLevelChangeException() {
    }

    public IllegalAccessLevelChangeException(String message) {
        super(message);
    }

    public IllegalAccessLevelChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalAccessLevelChangeException(Throwable cause) {
        super(cause);
    }
}
