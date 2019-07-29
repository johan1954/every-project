package com.example.foodreview;

import android.content.Context;

import java.util.regex.Pattern;

public class PasswordChecker {

    private static PasswordChecker instance = null;
    private Context context;

    //This class functions as a singleton

    private PasswordChecker(Context context) { this.context = context; }

    static PasswordChecker getInstance(Context context) {
        if (instance == null) {
            instance = new PasswordChecker(context);
        }
        else {
            System.out.println("Instance already exists.");
        }
        return instance;
    }

    //This method takes a password and runs checks. Returns string of error messages.
    String checker (String password) {
        String message = "";
        if (password.length() < 12) {
            message = message.concat(context.getResources().getString(R.string.signup_password_tooshort) + "\n");
        }
        if (password.length() > 32) {
            message = message.concat(context.getResources().getString(R.string.signup_password_toolong) + "\n");
        }
        if (password.equals(password.toLowerCase())) {
            message = message.concat(context.getResources().getString(R.string.signup_password_uppercase) + "\n");
        }
        if (password.equals(password.toUpperCase())) {
            message = message.concat(context.getResources().getString(R.string.signup_password_lowercase) + "\n");
        }
        if (!password.matches(".*\\d.*")) {
            message = message.concat(context.getResources().getString(R.string.signup_password_nonumbers) + "\n");
        }
        if (!Pattern.compile("(?=.*[@#$%^&+=€/()_]).*").matcher(password).matches()) {
            message = message.concat(context.getResources().getString(R.string.signup_password_nospecialchar));
        }

        return message;
    }
}
