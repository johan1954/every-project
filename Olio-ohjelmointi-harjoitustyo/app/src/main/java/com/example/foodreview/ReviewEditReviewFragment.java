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

public class ReviewEditReviewFragment extends Fragment {
    View view;
    TextView reviewWords;
    TextView foodTitleName;
    TextView reviewLetters;
    int letters;
    String review;
    RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editreviewfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            assert getArguments() != null;
            review = getArguments().getString("reviewString"); //Sets the edit review fragment's data to correspond the correct data
            foodTitleName = this.view.findViewById(R.id.editFoodTitleName);
            reviewWords = this.view.findViewById(R.id.editReviewWords);
            reviewLetters = this.view.findViewById(R.id.editReviewLetters);
            ratingBar = this.view.findViewById(R.id.editRatingBar);
            reviewWords.setText(review + "");
            letters = reviewWords.getText().length();
            reviewLetters.setText(letters + "");
        } catch (Exception e) {
            TextView foodTitleName = this.view.findViewById(R.id.editFoodTitleName);
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
        return review;
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
