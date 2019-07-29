package com.example.foodreview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class AdminNewRestaurantFragment extends Fragment {
    View view;
    EditText newRestaurantName;
    EditText newRestaurantAddress;
    EditText newRestaurantPC;
    EditText newRestaurantCity;
    Spinner newRestUniSpinner;
    UniversityManager universityManager;
    ArrayList<String> uniNames;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_adminnewrestaurant, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        newRestaurantName = this.view.findViewById(R.id.fieldNewRestaurantName);
        newRestaurantAddress = this.view.findViewById(R.id.fieldNewRestaurantAddress);
        newRestaurantPC = this.view.findViewById(R.id.fieldNewRestaurantPC);
        newRestaurantCity = this.view.findViewById(R.id.fieldNewRestaurantCity);
        newRestUniSpinner = this.view.findViewById(R.id.newRestUniSpinner);
        universityManager = UniversityManager.getInstance();

        uniNames = universityManager.getUniNames();


        ArrayAdapter<String> adapterUni = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, uniNames);
        adapterUni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newRestUniSpinner.setAdapter(adapterUni);
    }

    public String getNewRestaurantName() {
        String newRestaurantNameString = newRestaurantName.getText().toString();
        return newRestaurantNameString;
    }

    public String getNewRestaurantAddress() {
        String newRestaurantAddressString = newRestaurantAddress.getText().toString();
        return newRestaurantAddressString;
    }

    public String getNewRestaurantPC() {
        String newRestaurantPCString = newRestaurantPC.getText().toString();
        return newRestaurantPCString;
    }

    public String getNewRestaurantCity() {
        String newRestaurantCityString = newRestaurantCity.getText().toString();
        return newRestaurantCityString;
    }

    public String getNewRestaurantUni() {
        String newRestaurantUni = uniNames.get(newRestUniSpinner.getSelectedItemPosition());
        return newRestaurantUni;
    }

}
