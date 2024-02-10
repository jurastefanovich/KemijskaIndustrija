package com.example.stefanovic.kemijskaindustrija.Authentication;

import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Model.User;

import java.util.List;
import java.util.Map;

public abstract non-sealed class Credentials implements AuthRepository {
    private final Map<String,String> loginCredentialMap;
    private final List<User> userList;

    public Credentials() {
        this.userList = UserRepository.getAllUsers();
        this.loginCredentialMap = getEmailsAndPasswordsFromFile();
    }

    public Map<String, String> getLoginCredentialMap() {
        return loginCredentialMap;
    }

    public List<User> getUserList() {
        return userList;
    }
}
