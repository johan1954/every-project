package com.example.foodreview;


//Class controls all the universities and keeps up the list with available universities from the database.

import java.util.ArrayList;

class UniversityManager {

    private static UniversityManager instance;
    private ArrayList<University> universities;


    private UniversityManager () {
        universities = new ArrayList<>();
    }

    static UniversityManager getInstance() {
        if (instance == null) {
            instance = new UniversityManager();
        }
        else {

            System.out.println("Instance already exists");
        }
        return instance;
    }

    void setUniversities(ArrayList<University> newUniversities) {
        universities.clear();
        universities = newUniversities;
    }

    University getUniversity(String name) {
        for (int x = 0; x < universities.size(); x++) {
            if (name.equals(universities.get(x).getUniName())) {
                //System.out.println("University found and returned");
                return universities.get(x);
            }
        }
        return null;
    }

    ArrayList<String> getUniNames () {
        ArrayList<String> uniNames = new ArrayList<>();
        for (int x = 0; x < universities.size(); x++) {
            uniNames.add(universities.get(x).getUniName());
        }
        return uniNames;
    }

    ArrayList<University> getUniversities() {
        return universities;
    }


}
