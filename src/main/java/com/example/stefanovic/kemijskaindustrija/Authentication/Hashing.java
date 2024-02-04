package com.example.stefanovic.kemijskaindustrija.Authentication;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface Hashing {
    static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger bigInteger = new BigInteger(1, messageDigest);
        return bigInteger.toString(16);
    }
}
