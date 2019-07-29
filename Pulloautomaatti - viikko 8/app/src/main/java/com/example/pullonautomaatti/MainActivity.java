package com.example.pullonautomaatti;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /* Muuttujien määrittely! */
    private Button rahaa, palautus, osta;
    private TextView activity, logText, lisattava, automaatissa;
    private SeekBar liukukytkin;
    private int bottleIndex;
    private float lisattavaRaha;
    BottleDispenser BD;

    ArrayAdapter<String> adapter = null;

    Context context;

    ArrayList<String> bottle_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = MainActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rahaa = (Button) findViewById(R.id.rahaLisays);
        palautus = (Button) findViewById(R.id.palautusNappula);
        osta = (Button) findViewById(R.id.ostos);
        osta.setEnabled(false);
        rahaa.setEnabled(false);

        logText = (TextView) findViewById(R.id.logTextView);

        BD = BottleDispenser.getBD();
        BD.setLog(logText);

        logText.setMovementMethod(new ScrollingMovementMethod());

        /* Onclick listener käyttöön: */

        rahaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BD.addMoney(lisattavaRaha);
                updateMoney();
                updateButton();
                liukukytkin.setProgress(0);
                lisattavaRaha = 0.00f;
                updateLisaa();
                lisattava.setText("0.00€");

            }
        });

        osta.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {


                BD.buyBottle(bottleIndex);
                bottleIndex = 0;
                doSpinner();
                updateMoney();
                updateButton();

            }
        });
        palautus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BD.returnMoney();
                updateMoney();
                updateButton();
            }
        });


        /* Seek Bar -tästä käyttöön! */
        doSeekBar();

        /* Spinner tästä käyttöön! */
        doSpinner();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String text = parent.getItemAtPosition(position).toString();
        bottleIndex = BD.getBottle(text);
//        BD.setLogText(Integer.toString(bottleIndex));


        if (bottleIndex == -1) {
            BD.setLogText("Spinneri ei toimi!");
        }
        else {
//            Toast.makeText(this, "Yritetään luoda uutta pulloa.", Toast.LENGTH_LONG).show();
            updateButton();
            updatePrice();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void updateMoney() {


        Float money = BD.getMoney();
        String toSet = "", temp = "";

        automaatissa = (TextView) findViewById(R.id.automRaha);
        temp = String.format("%.2f€", money);
        toSet = toSet.concat(temp);
        automaatissa.setText(toSet);
    }

    public void updatePrice() {

        if (BD.getAmount() <= 0) {
//            BD.setLogText("Masiina on tyhjä :(\nTarvitaan lisää pulloja");
            return;
        }


        Float hinta = BD.getBottlePrice(bottleIndex);
        String toSet = "", temp = "";
        temp = String.format("%.2f€", hinta);
        toSet = toSet.concat(temp);

        activity = (TextView) findViewById(R.id.pullonHinta);
        activity.setText(toSet);
    }

    public void updateButton() {

//        BD.setLogText("Nappula päivittyy...");
        if (BD.getAmount() <= 0) {
            BD.setLogText("Masiina on tyhjä :(");
            activity.setText("");
            osta.setEnabled(false);
            return;
        }


        if (BD.getBottlePrice(bottleIndex) > BD.getMoney()) {
            osta.setEnabled(false);
        }
        else {
            osta.setEnabled(true);
        }

    }

    public void doSeekBar() {
        liukukytkin = (SeekBar) findViewById(R.id.rahamaara);
        lisattava = (TextView) findViewById(R.id.lisaamaara);
        liukukytkin.setMin(0);
        liukukytkin.setMax(100);
        // lisattava.setText(liukukytkin.getProgress());

        liukukytkin.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener() {
                    String temp = "", merkit = "";

                    int matka;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float addMoney = matka;
                        addMoney = (addMoney / 20);
                        matka = progress;

                        if (addMoney < 2.0) {

                        }
                        else if ((2.0 <= addMoney) && (addMoney < 2.5)){
                            addMoney = 2.0f;
                        }
                        else if ((2.5 <= addMoney) && (addMoney < 3.5)){
                            addMoney = 3.0f;
                        }
                        else if ((3.5 <= addMoney) && (addMoney < 4.5)){
                            addMoney = 4.0f;
                        }
                        else if (4.5 <= addMoney) {
                            addMoney = 5.0f;
                        }
//                        temp = temp.concat("€");
                        temp = String.format("%.2f€", addMoney);

                        lisattavaRaha = addMoney;
                        lisattava.setText(temp);
                        updateLisaa();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
    }
    public void doSpinner() {
        /* Spinnerin alustus */

        bottle_string = BD.pullotListaan();

        Spinner spinner = findViewById(R.id.spinnerPullot);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bottle_string);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }
    public void updateLisaa() {

        if (!(lisattavaRaha > 0)) {
            rahaa.setEnabled(false);
        }
        else {
            rahaa.setEnabled(true);
        }
    }


    public void writeFileOnInternalStorage(View v){
        if (BD.getKuittiData() != null) {
            String sBody = "*** PULLOAUTOMAATTI ***\n\n***     KUITTI      ***\n\nTuote:\n";
            String nimi = "kuitti.txt";
            sBody = sBody.concat(BD.getKuittiData());
            File file = new File(context.getFilesDir(),"mydir");
            if(!file.exists()){
                file.mkdir();
            }

            try{
                File gpxfile = new File(file, nimi);
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(sBody);
                writer.flush();
                writer.close();

            }catch (Exception e){
                e.printStackTrace();

            }
            BD.setLogText("Kuitti tulostettu!");
        }
        else {
            Toast.makeText(this, "Ei ostettuja tuotteita. Osta ensin jotain.", Toast.LENGTH_LONG).show();
        }
    }


}
