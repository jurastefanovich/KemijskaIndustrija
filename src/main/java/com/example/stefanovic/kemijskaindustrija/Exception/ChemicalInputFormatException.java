package com.example.stefanovic.kemijskaindustrija.Exception;

public class ChemicalInputFormatException extends Exception{
    public ChemicalInputFormatException() {
    }

    public ChemicalInputFormatException(String message) {
        super(message);
    }

    public ChemicalInputFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChemicalInputFormatException(Throwable cause) {
        super(cause);
    }


}
