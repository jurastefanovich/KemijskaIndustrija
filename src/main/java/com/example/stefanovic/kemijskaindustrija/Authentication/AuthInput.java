package com.example.stefanovic.kemijskaindustrija.Authentication;

import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.AccountException;
import com.example.stefanovic.kemijskaindustrija.Exception.EmailException;
import com.example.stefanovic.kemijskaindustrija.Exception.PasswordException;
import com.example.stefanovic.kemijskaindustrija.Exception.UsernameTakenException;
import com.example.stefanovic.kemijskaindustrija.Main.Main;
import com.example.stefanovic.kemijskaindustrija.Model.User;
import javafx.scene.control.Label;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AuthInput {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

//    Logger logger = LoggerFactory.getLogger(Glavna.class);

    public static boolean checkLoginCredetials(Map<String, String> loginCredentails, String email, String password) throws PasswordException, AccountException {
        if(loginCredentails.containsKey(email)){
            try {
                if (Hashing.hashPassword(password).equals(loginCredentails.get(email))){
                    return true;
                }
                else{
                    throw new PasswordException(AuthMessages.WRONG_PASSWORD.getMessage());
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }else{
            throw new AccountException(AuthMessages.NO_ACCOUNT.getMessage());
        }
    }

    public static void checkEmailAddress(String email) throws EmailException{
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches() && !email.toLowerCase().equals("admin")){
            throw new EmailException(AuthMessages.NOT_EMAIL.getMessage());
        }
    }

    public static boolean checkEmail(List<String> existingEmails, String inputEmail) throws NullPointerException, AccountException, EmailException {
        if(!inputEmail.contains("@")){
            throw new EmailException(String.valueOf(AuthMessages.NOT_EMAIL.getMessage()));
        }
        return checkString(existingEmails, inputEmail, String.valueOf(AuthMessages.EMAIL_TAKEN.getMessage()));

    }
    public static boolean checkUserName(List<String> existingUserNames, String inputUsername) throws NullPointerException, AccountException {
        return checkString(existingUserNames, inputUsername,String.valueOf(AuthMessages.USERNAME_TAKEN.getMessage()));
    }

    public static void checkUsername(List<String> listOfUsernames, String inputUsername) throws UsernameTakenException, NullPointerException{
        if (!inputUsername.isEmpty() && listOfUsernames.stream().anyMatch(inputUsername::equals)){
            throw new AccountException(AuthMessages.USERNAME_TAKEN.getMessage());
        }
        else{
            throw new NullPointerException(String.valueOf(AuthMessages.EMPTY_INPUT.getMessage()));
        }
    }

    public static boolean checkString(List<String> existingString, String inputString,String accountMessage ) throws NullPointerException, AccountException {
        if(inputString.isEmpty()){
            throw new NullPointerException(String.valueOf(AuthMessages.EMPTY_INPUT.getMessage()));
        }
        if(existingString.stream().anyMatch(inputString::equals)){
            throw new AccountException(accountMessage);
        }
        return false;
    }
    public static boolean checkPassword(String password, String confirmPassword) throws PasswordException, NullPointerException{
        if(password.length()<8){
            throw new PasswordException(String.valueOf(AuthMessages.PASSWORD_LENGTH.getMessage()));
        }
        return passwordMatcher(password,confirmPassword);
    }
    public static boolean passwordMatcher(String password, String confirmPassword) throws PasswordException, NullPointerException {
        if(!password.equals(confirmPassword)){
            throw  new PasswordException(String.valueOf(AuthMessages.NO_MATCH_PASSWORD.getMessage()));
        }
        if(password.isEmpty()) throw new NullPointerException(String.valueOf(AuthMessages.EMPTY_INPUT.getMessage()));

        return true;
    }
    public static <V> boolean inputChecker(V value) throws NullPointerException{
        if (value.equals("") || value == null){
            throw new NullPointerException(AuthMessages.EMPTY_INPUT.getMessage());
        }
        return true;
    }


}