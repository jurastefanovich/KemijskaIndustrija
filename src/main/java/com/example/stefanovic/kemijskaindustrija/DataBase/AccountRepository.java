package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Authentication.AccessLevel;
import com.example.stefanovic.kemijskaindustrija.Authentication.Account;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;

public interface AccountRepository {
    org.slf4j.Logger logger = LoggerFactory.getLogger(Main.class);
    static Account getSingleAccount(ResultSet rs){
        Account account = null;
        try {
            String email = rs.getString("email");
            String password = rs.getString("password");
            String username = rs.getString("username");
            AccessLevel accessLevel = AccessLevel.valueOf(rs.getString("access_level"));
            account = new Account(email, password, username, accessLevel);
        } catch (Exception e) {
            logger.error("An error occurred while trying to get account information " + e.getMessage());
        }
        return  account;
    }


}
