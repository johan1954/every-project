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
import android.widget.TextView;

import java.util.ArrayList;

public class AdminFoodEditFragment extends Fragment {
    View view;
    EditText fieldFoodName;
    EditText fieldFoodPrice;
    EditText fieldFoodDate;
    int selection;
    String uni;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_adminfoodeditfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            assert getArguments() != null;
            String name = getArguments().getString("foodName");//Sets the admin edit fragment's textviews to correspond the correct information
            String price = getArguments().getString("foodPrice");
            String date = getArguments().getString("foodDate");
            uni = getArguments().getString("foodUni");
            selection = getArguments().getInt("selectedFood");
            fieldFoodName = this.view.findViewById(R.id.fieldFoodName);
            fieldFoodPrice = this.view.findViewById(R.id.fieldFoodPrice);
            fieldFoodDate = this.view.findViewById(R.id.fieldFoodDate);

            fieldFoodName.setText(name);
            fieldFoodPrice.setText(price);
            fieldFoodDate.setText(date);
        } catch (Exception e) {
        }
    }

    public String getEditFoodName() {
        String editFoodNameText = fieldFoodName.getText().toString();
        return editFoodNameText;
    }

    public float getEditFoodPrice() {
        float editFoodPriceFloat = Float.parseFloat(fieldFoodPrice.getText().toString());
        return editFoodPriceFloat;
    }

    public String getEditFoodDate() {
        String editFoodDateText = fieldFoodDate.getText().toString();
        return editFoodDateText;
    }

    public String getFoodUni() {
        return uni;
    }

    public int getSelectedFood() {
        return selection;
    }


}
