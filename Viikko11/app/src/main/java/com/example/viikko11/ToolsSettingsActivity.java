package com.example.viikko11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

public class ToolsSettingsActivity extends AppCompatActivity
implements AdapterView.OnItemSelectedListener {

    private EditText editText, usernameTextEdit;
    private Spinner spinner1, spinner2;
    private String currFontSize, currFontBold, currFontCursed, currWidthofTextBox, currEditable, username, currLang;
    private String returnValue;
    private Switch boldSwitch, cursedSwitch, editableSwitch;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_settings);
        spinner1 = (Spinner) findViewById(R.id.spinnerFonts);
        spinner2 = (Spinner) findViewById(R.id.language);
        boldSwitch = (Switch) findViewById(R.id.boldSwitchid);
        editText = (EditText)findViewById(R.id.editText3);

        cursedSwitch = (Switch)findViewById(R.id.cursedSwitchid);
        editableSwitch = (Switch)findViewById(R.id.ewditableSwitch);
        usernameTextEdit = (EditText) findViewById(R.id.editTextUsenameId);
        doSpinner();
        doSpinner2();
        String values = getIntent().getStringExtra("values");
        System.out.println(values);
        String[] parts = values.split(":");
        currFontSize =parts[0];
        currFontBold =parts[1];
        currFontCursed =parts[2];
        currWidthofTextBox =parts[3];
        currEditable =parts[4];
        username = parts[5];
        currLang = parts[6];


        editText.setText(currWidthofTextBox);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currWidthofTextBox = (String) editText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        usernameTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                username = (String) usernameTextEdit.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        boldSwitch.setChecked(Boolean.parseBoolean(currFontBold));
        cursedSwitch.setChecked(Boolean.parseBoolean(currFontCursed));
        editableSwitch.setChecked(Boolean.parseBoolean(currEditable));
        String[] fonts= getResources().getStringArray(R.array.fonttikootInt);
        for (int x = 0; x < fonts.length; x++) {
            if (currFontSize.equals(fonts[x])) {
                id = x;
                break;
            }
        }
        int id2 = -1;
        String[] langs = getResources().getStringArray(R.array.languages);
        for (int x = 0; x < langs.length; x++) {
            if (currLang.equals(langs[x])) {
                id2 = x;
                break;
            }
        }
        spinner1.setSelection(id);
        spinner2.setSelection(id2);
    }





    public void doSpinner() {
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.fonttikoot,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int index = parent.getId();
        if (index == R.id.spinnerFonts) {
            String[] items = getResources().getStringArray(R.array.fonttikootInt);
            String item = items[position];
            currFontSize = item;
        }
        else if (index == R.id.language) {
            currLang = (String) parent.getItemAtPosition(position).toString();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        if (boldSwitch.isChecked()) {
            currFontBold = "true";
        }
        else {
            currFontBold = "false";
        }
        if (cursedSwitch.isChecked()) {
            currFontCursed = "true";
        }
        else {
            currFontCursed = "false";
        }
        if (editableSwitch.isChecked()) {
            currEditable = "true";
        }
        else {
            currEditable = "false";
        }

        returnValue = currFontSize;
        returnValue = returnValue.concat(":").concat(currFontBold).concat(":");
        returnValue = returnValue.concat(currFontCursed).concat(":");
        returnValue = returnValue.concat(currWidthofTextBox).concat(":");
        returnValue = returnValue.concat(currEditable).concat(":");
        returnValue = returnValue.concat(username).concat(":");
        returnValue = returnValue.concat(currLang);

        Intent intentReturn = new Intent();
        intentReturn.putExtra("returnValues", returnValue);
        setResult(RESULT_OK, intentReturn);
        finish();
    }

    public void doSpinner2() {
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.languages,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

    }

}
