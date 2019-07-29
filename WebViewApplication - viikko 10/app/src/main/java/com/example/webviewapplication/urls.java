package com.example.webviewapplication;

public class urls {
    private int id;
    private String url;

    public urls (int index, String currUrl) {
        id = index;
        url = currUrl;

    }

    public String getUrlFromIndex() {
        return url;
    }
}
