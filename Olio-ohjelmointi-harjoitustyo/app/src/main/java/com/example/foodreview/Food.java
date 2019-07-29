package com.example.foodreview;

import java.util.ArrayList;

class Food {
    private String name;
    private int id;
    private float price;
    private String date;
    private ArrayList<Review> reviews;

    Food(String newName, int newId, float newPrice, String newDate) {
        name = newName;
        id = newId;
        price = newPrice;
        date = newDate;
    }

    String getFoodName() {
        return name;
    }

    int getFoodId() {
        return id;
    }

    float getFoodPrice() {
        return price;
    }

    String getDate() {
        return date;
    }

    void setReviews(ArrayList<Review> newReviews) {
        reviews = newReviews;
    }

    ArrayList<Review> getReviews() {
        return reviews;
    }
}
