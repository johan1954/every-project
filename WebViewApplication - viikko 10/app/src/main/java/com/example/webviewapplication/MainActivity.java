package com.example.webviewapplication;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    WebView web;
    ImageButton refresh, next, previous;
    Button goToPage;
    ProgressBar showProgress;
    EditText urlAddress;
    String currentUrlAddress, previousUrlAddress = "null";
    ArrayList<urls> urlsS;
    int indexOfLocation = 0;
    boolean buttonClicked = false;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Setting up UI element ids to variables */
        web = (WebView) findViewById(R.id.webView1);
        refresh = (ImageButton) findViewById(R.id.refreshButtonId);
        next = (ImageButton) findViewById(R.id.nextButtonId);
        previous = (ImageButton) findViewById(R.id.previousButtonId);
        urlAddress = (EditText) findViewById(R.id.editUrlTextId);
        goToPage = (Button) findViewById(R.id.buttonGoId);
        showProgress = (ProgressBar) findViewById(R.id.progressBarId);
        urlsS = new ArrayList<>();

        web.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                urlAddress.setText(web.getUrl());
                String thisUrl = web.getUrl();
                urls newUrl = new urls(indexOfLocation, thisUrl);

                System.out.println(urlsS.size());
                if (!buttonClicked) {
                    if (!urlsS.contains(newUrl)) {
                        System.out.println(thisUrl);
                        int constant = (urlsS.size() - 1);
                        for (int x = 0; x < (constant - indexOfLocation); x++) {
                            System.out.println();
                            urlsS.remove(urlsS.size()-1);
                        }

                        indexOfLocation = indexOfLocation + 1;
                        urlsS.add(newUrl);
                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                urls newUrl = new urls(indexOfLocation, url);
                buttonClicked = false;
//                if (!urlsS.contains(newUrl)) {
//                    indexOfLocation = indexOfLocation +1;
//                    String thisPage = url;
//                    urls aNewUrl = new urls(indexOfLocation, url);
//                    urlsS.add(aNewUrl);
//                }

            }
        });

        currentUrlAddress="http://www.google.com";
        web.loadUrl(currentUrlAddress);
        urlAddress.setText(currentUrlAddress);
        urls newUrl = new urls(indexOfLocation, currentUrlAddress);
        System.out.println(indexOfLocation + "   " + currentUrlAddress);
        urlsS.add(newUrl);

        /* javascript enabled */
        web.getSettings().setJavaScriptEnabled(true);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUrlAddress = urlAddress.getText().toString();
                web.loadUrl(currentUrlAddress);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked = true;
                if (!(indexOfLocation == urlsS.size()-1)) {
                    indexOfLocation = indexOfLocation + 1;
                    String thisurl = urlsS.get(indexOfLocation).getUrlFromIndex();
                    web.loadUrl(thisurl);
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked = true;
                if ((urlsS.size() == 0) || indexOfLocation == 0) {
                    return;
                }
                else {
                    String prevurl;
                    indexOfLocation = indexOfLocation - 1;
                    prevurl = urlsS.get(indexOfLocation).getUrlFromIndex();
                    web.loadUrl(prevurl);
                }
            }
        });
//        goToPage.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {


//            }
//        });


        urlAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentUrlAddress = urlAddress.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





    }

    public void gobutton(View v){
        buttonClicked = false;
        String temp = currentUrlAddress;
        if (currentUrlAddress.endsWith("html")) {
            currentUrlAddress = "file:///android_asset/".concat(currentUrlAddress);
        } else if (!currentUrlAddress.startsWith("http")) {
            currentUrlAddress = "http://".concat(currentUrlAddress);
        }
        urlAddress.setText(currentUrlAddress);
        web.loadUrl(currentUrlAddress);
    }

    public void executeJavascript(View v) {
        web.evaluateJavascript("javascript:shoutOut()", null);
    }

    public void initialize(View v) {
        web.evaluateJavascript("javascript:initialize()", null);
    }
}
