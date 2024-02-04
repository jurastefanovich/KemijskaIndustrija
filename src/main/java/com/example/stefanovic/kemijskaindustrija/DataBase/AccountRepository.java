package com.example.stefanovic.kemijskaindustrija.DataBase;

import com.example.stefanovic.kemijskaindustrija.Authentication.AccessLevel;
import com.example.stefanovic.kemijskaindustrija.Authentication.Account;
import com.example.stefanovic.kemijskaindustrija.Model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public interface AccountRepository {
    static Account getSingleAccount(ResultSet rs){
        try {
            String email = rs.getString("email");
            String password = rs.getString("password");
            String username = rs.getString("username");
            AccessLevel accessLevel = AccessLevel.valueOf(rs.getString("access_level"));
            return  new Account(email, password, username, accessLevel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
