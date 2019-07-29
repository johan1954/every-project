package com.example.l07t01;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView text, newText;
    Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MainActivity.this;
        setContentView(R.layout.activity_main);
//        text = (TextView) findViewById(R.id.textView);
//        StartListener();
    }


    public void paina(View v) {

        System.out.println("HELLO WORLD!");
        String toSet = getTheText();
        text.setText(toSet);
    }
    public String getTheText() {
        newText = (TextView) findViewById(R.id.editTextField);
        String text = newText.getText().toString();
        return text;

    }

//    public void StartListener () {
//        newText = (EditText) findViewById(R.id.editTextField);
//
//
//        newText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String toSet = getTheText();
//                text.setText(toSet);
//            }
//        });
//    }

    public void saveFile(View v) {
        String fileName = ((TextView) findViewById(R.id.editTextName)).getText().toString();
        try {
            OutputStreamWriter outStream = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_APPEND));
            String toWrite = getTheText();
            outStream.write(toWrite);
            outStream.close();

        }
        catch (FileNotFoundException e) {
            Log.e("FileNotFoundException e", "Tiedostoa ei löytynyt");
        }
        catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä.");
        }

    }

    public void openFile(View v) {
        String fileName = ((TextView) findViewById(R.id.editTextName)).getText().toString();
        try {

            InputStream inStream = context.openFileInput(fileName);

            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));

            String merkit = "";
            String listaString = "";


            while ((merkit = br.readLine()) != null) {
                listaString = listaString.concat(merkit).concat("\n");
            }
            newText.setText(listaString);
            inStream.close();
        }
        catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", "Ei tiedostoa.");
        }
        catch (IOException e) {
            Log.e("IOException", "Virhe syötteessä.");
        }
    }


}
