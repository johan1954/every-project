package com.example.elokuvattv;

import java.util.ArrayList;

public class teatteriList {

    ArrayList<teatteri> teatterit;
    ArrayList<String> listassaTeatterit;

    private static teatteriList instance = null;

    private teatteriList() {
        teatterit = new ArrayList<>();
    }
    public static teatteriList getInstance() {
        if(instance == null) {
            instance = new teatteriList();
        }
        else {
            System.out.println("Instance already exists.");
        }
        return instance;
    }
    public void newTeatteri (String nimi, int index) {
        System.out.println("Tehdään uutta teatteria parametreilla: "+ nimi + " "+ index);
        teatteri jokinTeatteri = new teatteri(nimi, index);
        System.out.println("Uusi teatteri tehty, lisätään listaan...");
        teatterit.add(jokinTeatteri);
    }

    public String getTheaterId(String nimi) {
        int id = -1;
        String teaterid = "null";
        for (int x = 0; x < teatterit.size(); x++) {
            System.out.println(nimi + teatterit.get(x).getName());
            if (nimi.equals(teatterit.get(x).getName())) {
                id = teatterit.get(x).getId();
                teaterid = Integer.toString(id);
                return teaterid;
            }
        }
        return "null";
    }

    public void teatteritTulostus () {
        for (int i = 0; i < teatterit.size(); i++) {
            System.out.print(teatterit.get(i).getId() + " ");
            System.out.println(teatterit.get(i).getName());
        }
    }

    public ArrayList<String> teatteritMerkkijonoiksi () {

        listassaTeatterit = new ArrayList<>();
        for (int i = 0; i < teatterit.size(); i++) {
            listassaTeatterit.add(teatterit.get(i).getName());
        }
        return listassaTeatterit;
    }
}
