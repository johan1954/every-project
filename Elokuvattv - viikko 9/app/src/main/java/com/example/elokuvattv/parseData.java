package com.example.elokuvattv;


import java.util.ArrayList;

public class parseData {

    private static parseData instance = null;

    public static parseData getInstance() {
        if(instance == null) {
            instance = new parseData();
        }
        else {
            System.out.println("Instance already exists.");
        }
        return instance;
    }

    public void ParseData(ArrayList<String> dataTitle, ArrayList<String> startTime, ArrayList<String> theatreAndAuditorium, ArrayList<String> id) {
        elokuvatList elokuvat = elokuvatList.getInstance();
        elokuva newMovie;
        String newTitle, newLine2, newStartTime;
        String[] tempArray;
        int newId;
        for (int x = 0; x < dataTitle.size(); x++) {
            newTitle = dataTitle.get(x);
            tempArray = parseTime(startTime.get(x)).split("T");
            newStartTime = tempArray[0];
            newLine2 = parseLine2(tempArray, theatreAndAuditorium.get(x));
            newId = Integer.parseInt(id.get(x));
            newMovie = newElokuva(newTitle,newLine2,newStartTime,newId);
            elokuvat.newElokuva(newMovie);
        }

    }

    public elokuva newElokuva(String name, String line2, String time, int teatteri) {
        elokuva thisElokuva = new elokuva(name,line2, time, teatteri);
        return thisElokuva;
    }

    public String parseLine2(String[] dateAndTime, String TheatreOnIndex) {

        String temp, uusiLine2;
        temp = dateAndTime[0];
        uusiLine2 = dateAndTime[1];
        /* Concat kaikki toisen rivin tekstit... */
        uusiLine2 = uusiLine2.concat(" klo: ").concat(temp);
        uusiLine2 = uusiLine2.concat(" ").concat(TheatreOnIndex);
        return uusiLine2;
    }

    public String parseTime (String timeOnIndex) {
        String timeAndDate;

        String[] temp = timeOnIndex.split("T");
        String time = temp[1];
        String date = temp[0];

        /* Parse time */
        String[] timeparse = time.split(":");
        time = timeparse[0].concat(":");
        time = time.concat(timeparse[1]);

        /*Parse date */
        String[] dateparse = date.split("-");
        date = dateparse[2].concat(".");
        date = date.concat(dateparse[1]).concat(".");

        timeAndDate = time.concat("T").concat(date);

        return timeAndDate;
    }
}