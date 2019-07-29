package com.example.foodreview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class AdminNewFoodFragment extends Fragment implements Spinner.OnItemSelectedListener {
    View view;
    Spinner newFoodUniSpinner;
    Spinner newFoodRestSpinner;
    EditText newFoodName;
    EditText newFoodPrice;
    EditText newFoodDate;
    UniversityManager universityManager;
    ArrayList<String> uniNames;
    ArrayList<String> restNames;
    University currentUniversity;
    DatabaseManager dbms;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_adminnewfood, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        newFoodRestSpinner = this.view.findViewById(R.id.newFoodRestSpinner);
        newFoodUniSpinner = this.view.findViewById(R.id.newFoodUniSpinner);
        newFoodName = this.view.findViewById(R.id.fieldNewFoodName);
        newFoodPrice = this.view.findViewById(R.id.fieldNewFoodPrice);
        newFoodDate = this.view.findViewById(R.id.fieldNewFoodDate);
        universityManager = UniversityManager.getInstance();
        dbms = DatabaseManager.getInstance(view.getContext());

        uniNames = universityManager.getUniNames();

        ArrayAdapter<String> adapterUni = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, uniNames);
        adapterUni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newFoodUniSpinner.setAdapter(adapterUni);
        newFoodUniSpinner.setOnItemSelectedListener(this);


    }

    public void makeRestaurantSpinner(String uniName) {
        currentUniversity = universityManager.getUniversity(uniName);
        restNames = currentUniversity.getRestaurantStrings();
        ArrayAdapter<String> adapterRestaurant = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, restNames);
        adapterRestaurant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newFoodRestSpinner.setAdapter(adapterRestaurant);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String uniName = parent.getItemAtPosition(position).toString();
        dbms.updateUniversities();
        currentUniversity = universityManager.getUniversity(uniName);
        if (currentUniversity == null) {
            return;
        }
        dbms.updateCascade(currentUniversity);
        makeRestaurantSpinner(uniName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String getNewFoodName() {
        String newFoodNameText = newFoodName.getText().toString();
        return newFoodNameText;
    }

    public float getNewFoodPrice() {
        float newFoodPriceFloat = Float.parseFloat(newFoodPrice.getText().toString());
        return newFoodPriceFloat;
    }

    public String getNewFoodDate() {
        String newFoodDateText = newFoodDate.getText().toString();
        return newFoodDateText;
    }

    public String getNewFoodRest() {
        String newFoodRestText = restNames.get(newFoodRestSpinner.getSelectedItemPosition());
        return newFoodRestText;
    }

    public String getNewFoodUni() {
        String newFoodUniText = uniNames.get(newFoodUniSpinner.getSelectedItemPosition());
        return newFoodUniText;
    }
}
