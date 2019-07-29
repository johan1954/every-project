package com.example.foodreview;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.JsonWriter;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PrintToFile {

    private static PrintToFile instance = null;

    private PrintToFile() {

    }

    static PrintToFile getInstance() {
        if (instance == null) {
            instance = new PrintToFile();
        }
        else {
            System.out.println("Instance already exists.");
        }
        return instance;
    }

    void executePrint(Context context, University university) {
        try {
            FileWriter csvWriter = new FileWriter(context.getFilesDir() + university.getUniName() + "Data.csv");
            csvWriter.append("Restaurant Name, Restaurant Address, Food name, Food price, Review grade, Written review;\n");
            //csvWriter.append(university.getUniName());
            for (Restaurant restaurant : university.getRestaurants()) {

                String restaurantName = restaurant.getRestaurantName() + ";";
                String restaurantAddress = restaurant.getRestaurantAddress() + ";";

                for (Food food : restaurant.getFoods()) {
                    String foodName = food.getFoodName() + ";";

                    String price = food.getFoodPrice() + "€;";

                    csvWriter.append(restaurantName);
                    csvWriter.append(restaurantAddress);
                    csvWriter.append(foodName);
                    csvWriter.append(price);

                    if (food.getReviews().size() > 0) {
                        for (Review review : food.getReviews()) {

                            String grade = review.getGrade() + " / 5.0;";
                            csvWriter.append(grade);
                            csvWriter.append(review.getReview());
                            csvWriter.append(";\n");
                        }
                    }
                    else {
                        csvWriter.append(";");
                        csvWriter.append(";\n");
                    }

                }
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeJSON(Context context) {
        try {
            UniversityManager universityManager = UniversityManager.getInstance();
            ArrayList<University> uniList = universityManager.getUniversities();
            OutputStream out = new FileOutputStream(context.getFilesDir() + "FoodData.json");
            for (University uni : uniList) {
                for (Restaurant restaurant : uni.getRestaurants()) {
                    ArrayList<Food> foodList = restaurant.getFoods();
                    writeJsonStream(out, foodList);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeJsonStream(OutputStream out, ArrayList<Food> foods) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writeFoodArray(writer, foods);
        writer.close();
    }

    private void writeFoodArray(JsonWriter writer, ArrayList<Food> foods) throws IOException {
        writer.beginArray();
        for (Food food : foods) {
            writeFood(writer, food);
        }
        writer.endArray();
    }

    private void writeFood(JsonWriter writer, Food food) throws IOException {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        writer.beginObject();
        writer.name("name").value(food.getFoodName());
        writer.name("id").value(food.getFoodId());
        writer.name("date").value(food.getDate());
        writer.name("price").value(df.format(food.getFoodPrice()));
        writer.name("reviews");
        writeReviewArray(writer, food.getReviews());
        writer.endObject();
    }

    private void writeReviewArray(JsonWriter writer, ArrayList<Review> reviews) throws IOException {
        writer.beginArray();
        for (Review review : reviews) {
            writeReview(writer, review);
        }
        writer.endArray();
    }

    private void writeReview(JsonWriter writer, Review review) throws IOException {
        writer.beginObject();
        writer.name("id").value(review.getReviewId());
        writer.name("grade").value(review.getGrade());
        writer.name("review").value(review.getReview());
        writer.name("userId").value(review.getUserId());
        writer.endObject();
    }
}
