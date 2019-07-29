package com.example.elokuvattv;

import java.util.ArrayList;

public class elokuvatList {


    private int filterHourStart = -1;
    private int filterHourEnd = -1;
    private int filterMinuteStart = -1;
    private int filterMinuteEnd = -1;
    private String filterName = "-1-1";

    ArrayList<elokuva> naytaElokuvat;
    ArrayList<elokuva> filtered;

    private static elokuvatList instance = null;

    private elokuvatList() {
        this.filterHourStart = -1;
        this.filterHourEnd = -1;
        this.filterMinuteStart = -1;
        this.filterMinuteEnd = -1;
        this.filterName = "-1-1";
        naytaElokuvat = new ArrayList<>();
    }
    public static elokuvatList getInstance() {
        if(instance == null) {
            instance = new elokuvatList();
        }
        else {
            System.out.println("Instance already exists.");
        }
        return instance;
    }
    public void newElokuva(elokuva thisMovie) {
         naytaElokuvat.add(thisMovie);
    }

    public void newArrayList() {
        naytaElokuvat = new ArrayList<>();
    }

    public String getLine1fromIndex(int index) {
        return filtered.get(index).getTitle();
    }
    public String getLine2fromIndex(int index) {
        return filtered.get(index).getLine2();
    }
    public int getSizeFromelokuvat () {
        return filtered.size();
    }

    public void setFilterStart(int time) {
        this.filterMinuteStart = time;
        applyFilters();

    }
    public void setFilterEnd(int time) {
        this.filterMinuteEnd = time;
        applyFilters();
    }
    public void setFilterName (String name) {
        if (name.isEmpty()) {
            filterName = "-1-1";
        }
        else {
            this.filterName = name;
        }
        applyFilters();
    }

    public void applyFilters() {
        filtered = new ArrayList<>();
        elokuva thismovie;
        for (int x = 0; x < naytaElokuvat.size(); x++) {
            int counter = 0;

            thismovie = naytaElokuvat.get(x);
            int minutes = thismovie.getTime();

            counter = counter + isStart(minutes);
            counter = counter + isEnd(minutes);
            counter = counter + isString(thismovie.getTitle());
            if (counter == 3) {
                applyToList(x);
            }
        }
    }
    private void applyToList(int id) {

        elokuva thismovie = naytaElokuvat.get(id);
        for (int x = 0; x < filtered.size(); x++) {
            elokuva oldmovie = filtered.get(x);
            if ((oldmovie.getTitle().equals(thismovie.getTitle())) && (oldmovie.getLine2().equals(thismovie.getLine2()))) {
                return;
            }

        }
        filtered.add(thismovie);
    }

    private int isStart (int minutes) {

        if (filterMinuteStart > -1 ) {
            if (filterMinuteStart <= minutes) {
                return 1;
            }
            return 0;
        }
        else {return 1;}
    }
    private int isEnd (int minutes) {
        if (filterMinuteEnd > -1) {
            if (filterMinuteEnd >= minutes) {
                return 1;
            }
            return 0;
        }
        else {return 1;}
    }
    private int isString (String title) {
        if (!filterName.equals("-1-1")) {
            if (title.contains(filterName)) {
                return 1;
            }
            return 0;
        }
        else {
            return 1;
        }
    }
}
