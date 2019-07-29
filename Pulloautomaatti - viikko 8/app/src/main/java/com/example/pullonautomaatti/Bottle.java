package com.example.pullonautomaatti;

/**
 * @author johan1954
 */
public class Bottle {

    private String name, drink;
    private float size, price;

    public Bottle() {
        name = "Pepsi Max";
        size = 0.5f;
        price = 1.80f;
    }

    public void setBottle(String nimi, float koko, float hinta) {
        this.name = nimi;
        this.price = hinta;
        this.size = koko;
    }

    public String getName () {
        return this.name;
    }
    public float getPrice() {
        return this.price;
    }
    public float getSize() {
        return this.size;
    }

}
