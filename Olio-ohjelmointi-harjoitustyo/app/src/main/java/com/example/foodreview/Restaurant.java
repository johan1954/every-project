package com.example.foodreview;

import java.util.ArrayList;

class Restaurant {
    private String name;
    private int id;
    private int addressId;
    private String[] address;
    private ArrayList<Food> foods;
    private boolean isEnabled;

    Restaurant(int resId, String resName, String[] newAddress, int newAddressId, int newIsEnabled) {
        name = resName;
        id = resId;
        addressId = newAddressId;
        address = newAddress;
        if (newIsEnabled == 1) {
            isEnabled = true;
        }
        else if (newIsEnabled == 0) {
            isEnabled = false;
        }
    }

    String getRestaurantName() {
        return name;
    }

    int getRestaurantId() {
        return id;
    }

    void setRestaurantFoods(ArrayList<Food> newFoods) {
        System.out.println("Tiedot tallentuvat oikein. Pituus on: " + newFoods.size());
        this.foods = newFoods;
    }

    int getRestaurantAddressId() {
        return addressId;
    }

    boolean getIsEnabled() {
        return isEnabled;
    }

    String[] getRawRestaurantAddress() {
        return address;
    }

    String getRestaurantAddress() {
        String restaurantAddressString;
        restaurantAddressString = address[0];
        restaurantAddressString = restaurantAddressString.concat(" ");
        restaurantAddressString = restaurantAddressString.concat(address[1]).concat(" ");
        restaurantAddressString = restaurantAddressString.concat(address[2]);
        return restaurantAddressString;
    }

    // Takes date, compares all of the restaurant foods with the given date
    // Returns an ArrayList with compatible strings of food names.
    ArrayList<String> getRestaurantFoodStrings(String date) {
        ArrayList<String> restaurantFoodStrings = new ArrayList<>();
        for (int x = 0; x < foods.size(); x++) {
            if (date.equals(foods.get(x).getDate())) {
                restaurantFoodStrings.add(foods.get(x).getFoodName());
            }
        }
        return restaurantFoodStrings;
    }

    // Takes date, compares all of the restaurant foods with the given date
    // Returns an ArrayList with compatible floats of food prices.
    ArrayList<Float> getRestaurantFoodFloats(String date) {
        ArrayList<Float> foodPrices = new ArrayList<>();
        for (int x = 0; x < foods.size(); x++) {
            if (date.equals(foods.get(x).getDate())) {
                foodPrices.add(foods.get(x).getFoodPrice());
            }
        }
        return foodPrices;
    }

    ArrayList<Food> getRestaurantFoods(String date) {
        ArrayList<Food> newFoods = new ArrayList<>();
        for (int x = 0; x < foods.size(); x++) {
            if (date.equals(foods.get(x).getDate())) {
                newFoods.add(foods.get(x));
            }
        }
        return newFoods;
    }

    ArrayList<Food> getFoods () {
        return foods;
    }
}
