package com.example.foodreview;

class User {
    private String username;
    private String nickname;
    private boolean isAdmin;
    private int homeUniId;

    User (String newUsername,String newNickname, int newIsAdmin, int newHomeUniId) {
        username = newUsername;
        homeUniId = newHomeUniId;
        nickname = newNickname;
        if (newIsAdmin == 1) {
            isAdmin = true;
        }
        else if (newIsAdmin == 0) {
            isAdmin = false;
        }
    }

    String getUsername (){
        return username;
    }
    String getNickname () {return nickname;}
    boolean getIsAdmin (){
        return isAdmin;
    }
    int getHomeUniId() {
        return homeUniId;
    }
}
