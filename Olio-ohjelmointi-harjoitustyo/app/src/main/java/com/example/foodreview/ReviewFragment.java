package com.example.foodreview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewFragment extends Fragment {

    View view;
    TextView reviewWords;
    TextView foodTitleName;
    TextView reviewLetters;
    int letters;
    String name;
    RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reviewfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            assert getArguments() != null;
            name = getArguments().getString("foodName"); //Sets the review fragment's title to correspond the correct food
            foodTitleName = this.view.findViewById(R.id.foodTitleName);
            reviewWords = this.view.findViewById(R.id.reviewWords);
            reviewLetters = this.view.findViewById(R.id.reviewLetters);
            ratingBar = this.view.findViewById(R.id.ratingBar);
            foodTitleName.setText(name);
            letters = reviewWords.getText().length();
            reviewLetters.setText(letters + "");
        } catch (Exception e) {
            TextView foodTitleName = this.view.findViewById(R.id.foodTitleName);
            foodTitleName.setText(R.string.fragment_foodTitleNameNull);
        }

        reviewWords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                letters = reviewWords.getText().length();
                reviewLetters.setText(letters + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public String getReviewFoodName() {
        return name;
    }

    public float getReviewGrade() {
        float grade = ratingBar.getRating();
        return grade;
    }

    public String getReviewString() {
        String reviewString = reviewWords.getText().toString();
        return reviewString;
    }

}
