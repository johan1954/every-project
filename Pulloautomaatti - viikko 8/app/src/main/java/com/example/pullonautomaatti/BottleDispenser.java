package com.example.pullonautomaatti;

import android.text.style.TtsSpan;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Float.parseFloat;

/**
 * @author johan1954
 */
public class BottleDispenser {

    private static BottleDispenser bd = new BottleDispenser();
    private TextView logText;
    private int bottles;
    private ArrayList<Bottle> bottle_array;
    private float money;

    private String kuitinTuote = null;

    private BottleDispenser() {
        bottles = 6;
        money = 0.0f;
        bottle_array = new ArrayList<>();
        for (int i = 0; i < bottles; i++) {
            bottle_array.add(new Bottle());
        }
        bottle_array.get(1).setBottle("Pepsi Max", 1.5f,2.20f);
        bottle_array.get(2).setBottle("Coca-Cola Zero", 0.5f,2.00f);
        bottle_array.get(3).setBottle("Coca-Cola Zero", 1.5f,2.50f);
        bottle_array.get(4).setBottle("Fanta Zero", 0.5f,1.95f);
        bottle_array.get(5).setBottle("Fanta Zero", 0.5f,1.95f);
    }

    public static BottleDispenser getBD() {
        return bd;
    }

    public void setLog (TextView logi) { logText = logi;}

    public void addMoney(float lisattava) {

        String ret = "";
        money += lisattava;
        ret = ret.concat("Klink! Lisää rahaa laitteeseen!");
        setLogText(ret);
    }

    public void buyBottle(int index) {

        String ret ="", temp ="";
        ret = ret.concat("KACHUNK! " + bottle_array.get(index).getName() + " tipahti masiinasta!");
        bottles -= 1;
        money -= bottle_array.get(index).getPrice();
        temp = bottle_array.get(index).getName();
        temp = temp.concat(" ");
        temp = temp.concat(Float.toString(bottle_array.get(index).getSize())).concat("l\n\n");
        temp = temp.concat("Maksettu:\n");
        temp = temp.concat(Float.toString(bottle_array.get(index).getPrice())).concat("€\n*** Kiitos käynnistä! ***");
        setKuittiArvot(temp);
        deleteBottle(index);

        setLogText(ret);
    }

    /* Rahanpalautus toimii kuten olettaa voisi. */
    public void returnMoney() {
        String ret = "";
        money = round(money, 2);
        String rahaa = String.format("%.2f€",money);
        if (money > 0) {
            setLogText("Klink klink. Sinne menivät rahat! "
                    + "Rahaa tuli ulos "
                    + rahaa);
            money = 0;
        }
        else {
            setLogText("Ei rahaa laitteessa.");
        }
    }

    /* Listausfunktio tulostaisi kaikki listan pullot lokiin tarvittaessa. Tarvetta ei ole
    spinnerin toimiessa oikein. */

    public void listBottles() {
        String ret = "";
        for (int i = 0; i < bottles; i++) {
            ret = ret.concat((i+1)+". Nimi: "+bottle_array.get(i).getName());
            ret = ret.concat("    Koko: "+bottle_array.get(i).getSize()
                    +"  Hinta: "+bottle_array.get(i).getPrice());
        }
        setLogText(ret);
    }


    /* DeleteBottle -poistaa annetun idn pullon listasta. */
    private void deleteBottle(int bottles) {
        bottle_array.remove(bottles);
    }


    /* SetLogText funktio lisää tulostamisen sijaan kaikki annetut merkkijonot lokiin, ohjelman alareunaan. */
    public void setLogText (String setText) {
        logText.append("\n"+setText);
    }

    public ArrayList<String> pullotListaan(){
        ArrayList<String> bottles = new ArrayList<>();
        String temp;
        for (int i = 0; i < bottle_array.size(); i++) {
            temp = bottle_array.get(i).getName();
            temp = temp.concat(" " + bottle_array.get(i).getSize()+ "l");
            bottles.add(temp);
        }
        return bottles;
    }
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public int getBottle(String bottleData) {
        String temp = "";
        for (int i = 0; i < bottle_array.size(); i++) {
            temp = temp.concat(bottle_array.get(i).getName()+" " + bottle_array.get(i).getSize()+"l");
//            setLogText(temp);
//            setLogText(bottleData);
            if (temp.equals(bottleData)) {
                return i;
            }
            temp = "";
        }
        return -1;
    }
    public float getMoney() { return money; }
    public float getBottlePrice(int id){ return bottle_array.get(id).getPrice();}
    public int getAmount() {return bottle_array.size();}

    private void setKuittiArvot(String nimiKoko) {
        kuitinTuote = nimiKoko;
    }
    public String getKuittiData(){
        return kuitinTuote;
    }

}
