package com.example.stefanovic.kemijskaindustrija.Authentication;

import com.example.stefanovic.kemijskaindustrija.Controllers.AuthControllers.ForgotPassword;
import com.example.stefanovic.kemijskaindustrija.Controllers.AuthControllers.Login;
import com.example.stefanovic.kemijskaindustrija.Controllers.AuthControllers.SignUp;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public sealed interface AuthRepository permits Credentials, ForgotPassword, Login, SignUp {

    //    Might delete this if unused
    default Map<String,String> getEmailsAndPasswords(List<User> userList){
        Map<String, String> userMap = new HashMap<>();
        userList.forEach(user -> userMap.put(user.getAccount().email(), user.getAccount().password()));
        return userMap;
    }
    default Map<String, String> getEmailsAndPasswordsFromFile(){
        Map<String,String> credentials = new HashMap<>();

        try(BufferedReader bf = new BufferedReader(new FileReader(Main.LOGIN_FILE))) {
            List<String> loginCredentials = bf.lines().toList();
            for (int i = 0; i<loginCredentials.size()/2;i++){
                String email = loginCredentials.get(i * 2);
                String password = loginCredentials.get(i*2+1);
                credentials.put(email,password);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return credentials;
    }
    default User findUserByEmail(String email, List<User> userList){
        return userList.stream().filter(korisnik1 -> korisnik1.getAccount().email().equals(email)).findFirst().orElse(null);
    }
}
