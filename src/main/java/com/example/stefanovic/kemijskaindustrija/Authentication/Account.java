package com.example.stefanovic.kemijskaindustrija.Authentication;

import java.io.Serializable;

public record Account(String email, String password, String userName, AccessLevel accessLevel) implements Serializable {

    private static final long serialVersionUID = 649655804818955045L;

    public String toString() {
        return email + " " + password + " " + userName + " " +accessLevel;
    }

}
