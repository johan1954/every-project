package com.example.elokuvattv;

public class recyclerItem {
    private String textBox1;
    private String textBox2;

    public recyclerItem(String merkit1, String merkit2) {
        textBox1 = merkit1;
        textBox2 = merkit2;
    }

    public String getTextBox1() {
        return textBox1;
    }

    public String getTextBox2() {
        return textBox2;
    }
}
