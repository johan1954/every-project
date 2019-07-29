package com.example.elokuvattv;

import java.util.ArrayList;

public class urlFetch {
    private String urlThis;

    private static urlFetch instance = null;

    public static urlFetch getInstance() {
        if(instance == null) {
            instance = new urlFetch();
        }
        else {
            System.out.println("Instance already exists.");
        }
        return instance;
    }


    public String urlFetch(String pvm, String currTeatteri){
        teatteriList listaTeatterit = teatteriList.getInstance();

        urlThis = "http://www.finnkino.fi/xml/Schedule/?area=";
        String temp = listaTeatterit.getTheaterId(currTeatteri);
        System.out.println(temp+ " "+listaTeatterit.getTheaterId(currTeatteri));
        if (!temp.equals("null")) {
            System.out.println(temp);
            urlThis = urlThis.concat(temp).concat("&dt=");
            System.out.println(urlThis);
        }
        else {
            return "";
        }
        urlThis = urlThis.concat(pvm);

        return urlThis;
    }

}
