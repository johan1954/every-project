package com.example.foodreview;

import android.content.ContentValues;

import java.util.ArrayList;

class HardCodeFile {

    private static HardCodeFile instance = null;

    private HardCodeFile() {

    }

    static HardCodeFile getInstance () {
        if (instance == null) {
            instance = new HardCodeFile();
        }
        else {
            System.out.println("Instance already exists.");
        }
        return instance;
    }

    static ArrayList<ContentValues> hardCodeFoods(int counter, String newFoodDate) {
        float[] prices = {2.60f,5.40f,2.60f,2.60f,5.40f,2.20f, 2.60f, 2.60f, 5.40f, 2.60f, 5.40f};
        int[] restaurantIds = {1,1,1,2,2,3,3,4,4,5,5};
        ArrayList<ContentValues> contentValues = new ArrayList<>();

        switch (counter) {

            //Date 1
            case 1:
                //Foods from now on:
                String[] strings1 = {"Jauhelihapullat ja muusi","Hampurilaisateria",
                        "Lehtisalaatti ja keitto","Broileritortillat",
                        "Uunilohi ja perunamuusi","Jauhelihakastike ja perunat",
                        "Chili con Carne","Meksikolainen Uunimakkara","Lehtikaalikeitto",
                        "Jauheliha-Perunaviipalelaatikko", "Broileria Currykastikkeessa"};
                for (int x = 0; x < prices.length; x++) {
                    ContentValues cv = new ContentValues();
                    cv.put(UserIdContract.tableFood.COLUMN_RESTAURANTID, restaurantIds[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_FOODNAME, strings1[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_FOODPRICE, prices[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_DATE, newFoodDate);
                    contentValues.add(cv);
                }
                return contentValues;

            case 2:
                String[] strings2 = {"Isot lihapullat tomaattikastikkeessa",
                        "Lohiperunavuoka", "Sienikeitto",
                        "Broileritortillat", "Karjalanpaisti",
                        "Soijaa ja kasviksia hapanimelä", "Kasvissosekeitto",
                        "Kreikkalainen lihapata", "Smetana                    Broileri",
                        "Suppilovahvero-savuporokeitto", "Grillimakkaroita"};
                for (int x = 0; x < prices.length; x++ ) {
                    ContentValues cv = new ContentValues();
                    cv.put(UserIdContract.tableFood.COLUMN_RESTAURANTID, restaurantIds[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_FOODNAME, strings2[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_FOODPRICE, prices[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_DATE, newFoodDate);
                    contentValues.add(cv);
                }
                return contentValues;

            case 3:
                String[] strings3 = {"Uunilohi ja tillikermaviili","Tomaattiset lihapyörykät",
                        "Kasviskevätkääryleet", "Suurustettu kanakeitto",
                        "Karjalanpaisti","Soijaa ja kasviksia hapanimelä",
                        "Kasvissosekeitto","Kreikkalainen lihapata",
                        "Kesäkurpitsa-juustokeittoa","Kebablihawokkia",
                        "Sitruunakuorrutettua seitiä"};
                for (int x = 0; x < prices.length; x++ ) {
                    ContentValues cv = new ContentValues();
                    cv.put(UserIdContract.tableFood.COLUMN_RESTAURANTID, restaurantIds[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_FOODNAME, strings3[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_FOODPRICE, prices[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_DATE, newFoodDate);
                    contentValues.add(cv);
                }
                return contentValues;

            case 4:
                String[] strings4= {"Kasvisbolognesea VE", "Parmesanbroileria",
                        "Kinkku-aurajuustopasta","Hernekeitto + pannari",
                        "Pyttipannu", "Jauhelihakeitto", "Juustotäyte & uuniperuna",
                        "Punaviinihärkää", "Rapea kalapala", "Kasvispizza",
                        "Valkosipuli-perunasosekeitto"};
                for (int x = 0; x < prices.length; x++ ) {
                    ContentValues cv = new ContentValues();
                    cv.put(UserIdContract.tableFood.COLUMN_RESTAURANTID, restaurantIds[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_FOODNAME, strings4[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_FOODPRICE, prices[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_DATE, newFoodDate);
                    contentValues.add(cv);
                }
                return contentValues;

            case 5:
                String[] strings5= {"Kana       salaatti", "Kevyesti savustettua kasslerpaistia",
                        "Jauhelihalasagnette", "Pinaattilettuja fetatäytteellä",
                        "Hernekeitto", "Juustolenkki",
                        "Kesäkeitto", "Purjo-perunasosekeittoa",
                        "Kookos-kalacurrya", "Broilerikiusausta",
                        "Linssi-kasvismuhennosta"};
                for (int x = 0; x < prices.length; x++ ) {
                    ContentValues cv = new ContentValues();
                    cv.put(UserIdContract.tableFood.COLUMN_RESTAURANTID, restaurantIds[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_FOODNAME, strings5[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_FOODPRICE, prices[x]);
                    cv.put(UserIdContract.tableFood.COLUMN_DATE, newFoodDate);
                    contentValues.add(cv);
                }
                return contentValues;
            default:
                return null;

        }
    }

}
