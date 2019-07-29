package com.example.foodreview;

import java.util.ArrayList;

class UserManager {

    private static UserManager instance = null;
    private ArrayList<User> users;

    private UserManager() {

    }

    static UserManager getInstance () {
        if (instance == null) {
            instance = new UserManager();
        }
        else {
            System.out.println("Instance already exists.");
        }
        return instance;
    }

    void setUsers (ArrayList<User> newUsers) {
        users = newUsers;
    }

    ArrayList<User> getUsers() {
        return users;
    }

}
