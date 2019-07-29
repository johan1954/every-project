package com.example.foodreview;

import java.util.ArrayList;

// University is a class that also functions as a manager for all restaurants.
class University {
    private String name;
    private int id;
    private ArrayList<Restaurant> restaurants;

    University(int uniId, String uniName) {
        name = uniName;
        id = uniId;

    }

    String getUniName() {
        return name;
    }

    int getUniId() {
        return id;
    }

    ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    void setRestaurants(ArrayList<Restaurant> newRestaurants) {

        System.out.println("Onnistuneesti tämän pituinen newRestaurants " + newRestaurants.size());
        restaurants = newRestaurants;
    }

    Restaurant getRestaurant (String name) {

        for (int x = 0; x < restaurants.size(); x++) {
            if (name.equals(restaurants.get(x).getRestaurantName())) {
                return restaurants.get(x);
            }
        }
        //System.out.println("Null returned");
        return null;
    }
    ArrayList<String> getRestaurantStrings () {
        ArrayList<String> restaurantStrings = new ArrayList<>();
        for (int x = 0; x < restaurants.size(); x++) {
            restaurantStrings.add(restaurants.get(x).getRestaurantName());
        }
        return restaurantStrings;
    }
}
