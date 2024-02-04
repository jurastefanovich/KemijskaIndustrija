package com.example.stefanovic.kemijskaindustrija.DataBase;

public enum DataBaseMessages {
    SUCCESSFUL_CONNECTION("Successfuly connected to DB"),
    ERROR_CONNECTING("Error connecting to database"),
    ERORR_GETTING_USERS("Error getting users"),
    ERROR_GETTING_SINGLE_USER("Error getting single user details"),
    ERROR_UPDATING("Error while trying to update entity"),
    ERROR_CREATING_ACCOUNT("Error creating account!"),
    SAVE_ERROR("Failed to save item!"),
    UPDATE_ERROR("Failed to update entity"),
    ERROR_GETTING_EQUIPMENT("Couldn't retrive data for equipment from database"),
    EQUIPMENT_ID_DB_ERROR("Couldn't fetch equipment data by id"),
            ;

    private String message;

    DataBaseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
