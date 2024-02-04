package com.example.stefanovic.kemijskaindustrija.Authentication;

public enum AuthMessages {
    EMPTY_INPUT("Field can't be empty"),
    NOT_EMAIL("Email not valid"),
    NO_MATCH_PASSWORD("Passwords do not match"),
    USERNAME_TAKEN("Username already taken!"),
    EMAIL_TAKEN("An account is already linked to this email address"),
    NO_ACCOUNT("This account doesn't exist"),
    WRONG_PASSWORD("Wrong password"),
    PASSWORD_LENGTH("Password must be at least 8 characters long"),
    SUCCESSFUL_LOGIN("User loggedi in"),
    SUCCESFFUL_ACCOUNT_CREATION("Account created successfully"),
    BAN("This account is temporerally banned");
    private final String message;

    AuthMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
