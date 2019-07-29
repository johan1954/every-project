package com.example.viikko11;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int fontSize = 12;
    boolean cursed = false, bold = false, editable = true;
    int widthDp = 1000;
    EditText theTextBox, theTextBox2;
    TextView username;
    String thisUsername = " ";
    String lang = "English";
    String activatedLang = "English";
    Context context = this;
    Intent refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        theTextBox = (EditText) findViewById(R.id.editText);
        theTextBox2 = (EditText) findViewById(R.id.theTextBox2);
        theTextBox2.setEnabled(editable);
        theTextBox.setTextSize(fontSize);
        theTextBox.getLayoutParams().width=widthDp;
        username = (TextView) findViewById(R.id.usernameTextBox);

        String thisIntent = getIntent().getStringExtra("values");
        if (thisIntent != null) {
            stringToValues(thisIntent);
            updateValues();
            activatedLang = lang;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            startActivity1();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tools) {
            startActivity1();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String values = data.getStringExtra("returnValues");
                stringToValues(values);
                updateValues();

                if (!lang.equals(activatedLang)) {
                    setLang(lang);
                }
            }
            else {
                return;
            }
        }
    }

    public String valuesToString() {
        String values = Integer.toString(fontSize);
        values = values.concat(":").concat(Boolean.toString(bold)).concat(":");
        values = values.concat(Boolean.toString(cursed).concat(":"));
        values = values.concat(Integer.toString(widthDp)).concat(":");
        values = values.concat(Boolean.toString(editable)).concat(":");
        values = values.concat(thisUsername).concat(":");
        values = values.concat(lang);
        return values;
    }

    public void stringToValues (String values) {
        String[] parts = values.split(":");
        fontSize = Integer.parseInt(parts[0]);
        bold = Boolean.parseBoolean(parts[1]);
        cursed = Boolean.parseBoolean(parts[2]);
        widthDp = Integer.parseInt(parts[3]);
        editable = Boolean.parseBoolean(parts[4]);
        thisUsername = (String) parts[5];
        lang = (String)parts[6];
    }

    public void startActivity1() {
        Intent intent1 = new Intent(MainActivity.this, ToolsSettingsActivity.class);
        String values = valuesToString();
        intent1.putExtra("values", values);
        startActivityForResult(intent1, 1);
    }

    public void updateValues() {
        theTextBox.setTextSize(fontSize);
        theTextBox.getLayoutParams().width = widthDp;
        username.setText(thisUsername);
        if (!editable) {
            String temp = (String) theTextBox2.getText().toString();
            theTextBox.setText(temp);
            theTextBox2.setEnabled(editable);

        }

        if (cursed && bold) {
            theTextBox.setTypeface(null, Typeface.BOLD_ITALIC);
        }
        else if (cursed) {
            theTextBox.setTypeface(null, Typeface.ITALIC);
        }
        else if (bold) {
            theTextBox.setTypeface(null, Typeface.BOLD);
        }
        else {
            theTextBox.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void refreshSoftware(Locale locale) {

       // Locale.setDefault(locale);
        Resources res = getResources();

        Configuration config = res.getConfiguration();
        config.setLocale(locale);
        DisplayMetrics dm = res.getDisplayMetrics();
        res.updateConfiguration(config, dm);
        finish();
        refresh = new Intent(MainActivity.this, MainActivity.class);
        String newString = valuesToString();
        refresh.putExtra("values", newString);

        startActivity(refresh);
    }

    private void setLang(String language) {
        if (language.equals("Finnish")){
            String temp = "fi";
            Locale locale = new Locale("fi");
            refreshSoftware(locale);
        }
        else if (language.equals("English") || language.equals("Swedish")){
            String temp = "en";
            Locale locale = Locale.ENGLISH;
            refreshSoftware(locale);
        }
    }
}
