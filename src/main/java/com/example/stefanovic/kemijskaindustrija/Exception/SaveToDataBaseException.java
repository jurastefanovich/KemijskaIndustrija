package com.example.stefanovic.kemijskaindustrija.Exception;

public class SaveToDataBaseException extends Exception{
    public SaveToDataBaseException() {
    }

    public SaveToDataBaseException(String message) {
        super(message);
    }

    public SaveToDataBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveToDataBaseException(Throwable cause) {
        super(cause);
    }
}
