package com.example.elokuvattv;

public class teatteri {

    private String name;
    private int id;

    public teatteri (String nimi, int index) {
        this.name = nimi;
        this.id = index;
    }

    public String getName() {return this.name; }
    public int getId() {return this.id;}
}
