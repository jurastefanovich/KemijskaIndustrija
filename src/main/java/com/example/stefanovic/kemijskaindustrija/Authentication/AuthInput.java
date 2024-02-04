package com.example.stefanovic.kemijskaindustrija.Authentication;

import com.example.stefanovic.kemijskaindustrija.DataBase.DBController;
import com.example.stefanovic.kemijskaindustrija.DataBase.UserRepository;
import com.example.stefanovic.kemijskaindustrija.Exception.AccountException;
import com.example.stefanovic.kemijskaindustrija.Exception.EmailException;
import com.example.stefanovic.kemijskaindustrija.Exception.PasswordException;
import com.example.stefanovic.kemijskaindustrija.Model.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public final class AuthInput {
//    Logger logger = LoggerFactory.getLogger(Glavna.class);

//    static boolean checkIfUserIsBanned(String email) throws BlackListException {
//        try{
//            List<BlackList> blackLists = DataBaseController.getBlackListedUsers();
//            if(blackLists.stream().anyMatch(blackList -> blackList.getUser().get().getAccount().email().equals(email))){
//                throw new BlackListException(AuthMessages.BAN.getMessage());
//            }
//        }catch (BlackListException e){
//            System.out.println("No blacklisted users");
//        }
//
//        return true;
//    }

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
    public static boolean checkEmail(List<String> existingEmails, String inputEmail) throws NullPointerException, AccountException, EmailException {
        if(!inputEmail.contains("@")){
            throw new EmailException(String.valueOf(AuthMessages.NOT_EMAIL.getMessage()));
        }
        return checkString(existingEmails, inputEmail, String.valueOf(AuthMessages.EMAIL_TAKEN.getMessage()));

    }
    public static boolean checkUserName(List<String> existingUserNames, String inputUsername) throws NullPointerException, AccountException {
        return checkString(existingUserNames, inputUsername,String.valueOf(AuthMessages.USERNAME_TAKEN.getMessage()));
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