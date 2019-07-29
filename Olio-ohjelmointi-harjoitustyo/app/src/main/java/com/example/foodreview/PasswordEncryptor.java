package com.example.foodreview;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

class PasswordEncryptor {

    private static PasswordEncryptor instance = null;

    //This class also functions as a singleton.
    private PasswordEncryptor() {}

    static PasswordEncryptor getInstance() {
        if (instance == null) {
            instance = new PasswordEncryptor();
        }
        else {
            System.out.println("Instance already exists");
        }
        return instance;
    }


    // This method takes a string and converts it into hash. Uses SHA-256 algorithm and generated salt.
    String encryptor (String password, byte[] salt){

        String hashedPassword;

        MessageDigest digestor;
        try {
            digestor = MessageDigest.getInstance("SHA-256");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        digestor.update(salt);
        byte[] byteArray = digestor.digest(password.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : byteArray) {
            builder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        hashedPassword = builder.toString();
        return hashedPassword;
    }

    // This method uses username to generate salt. Salt is added to the hashed password on the method encryptor
    byte[] getSalt (String username) {
        SecureRandom randomize;

        try {
            randomize = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        byte[] saltArray = username.getBytes();
        randomize.nextBytes(saltArray);
        return saltArray;
    }
}
