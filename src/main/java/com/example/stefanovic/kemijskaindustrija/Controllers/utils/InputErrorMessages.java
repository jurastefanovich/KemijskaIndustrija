package com.example.stefanovic.kemijskaindustrija.Controllers.utils;

public enum InputErrorMessages {
    EMPTY_FIELD("Field is required!");
    private String message;

    InputErrorMessages(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
