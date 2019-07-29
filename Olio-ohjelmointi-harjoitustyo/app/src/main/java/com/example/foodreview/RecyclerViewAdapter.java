package com.example.foodreview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Food> mFoodList;
    private OnItemClickListener mListener;
    private String username;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onReviewClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName, mPrice, mAvgGrade;
        ImageView mReview;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mName = itemView.findViewById(R.id.main_name);
            mPrice = itemView.findViewById(R.id.main_price);
            mAvgGrade = itemView.findViewById(R.id.main_avgGrade);
            mReview = itemView.findViewById(R.id.main_grade);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            mReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onReviewClick(position);
                        }
                    }
                }
            });
        }
    }

    RecyclerViewAdapter(ArrayList<Food> foodList, String newUsername) {
        mFoodList = foodList;
        username = newUsername;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_row, viewGroup, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Food currentItem = mFoodList.get(i);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        DecimalFormat df2 = new DecimalFormat("0.00");
        df2.setMaximumFractionDigits(1);

        String time = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        viewHolder.mName.setText(currentItem.getFoodName());
        String price = " " + df.format(currentItem.getFoodPrice()) + "€";
        viewHolder.mPrice.setText(price);
        String gradeText;
        if (currentItem.getReviews().size() != 0) {
            float grade = 0;
            for (Review review : currentItem.getReviews()) {
                grade += review.getGrade();
            }
            grade /= currentItem.getReviews().size();
            gradeText = " " + df2.format(grade) + " / 5.0";
        } else {
            gradeText = " - / 5.0";
        }
        viewHolder.mAvgGrade.setText(gradeText);
        if (currentItem.getDate().equals(time)) {
            viewHolder.mReview.setVisibility(View.VISIBLE);
            ArrayList<Review> reviews = currentItem.getReviews();
            if (reviews.size() > 0) {
                for (int x = 0; x < reviews.size(); x++) {
                    if (reviews.get(x).getUserId().equals(username)) {
                        if (currentItem.getFoodId() == reviews.get(x).getFoodId()) {
                            viewHolder.mReview.setEnabled(false);
                            viewHolder.mReview.setAlpha(0.3f);
                        }
                    }
                }
            } else {
                viewHolder.mReview.setEnabled(true);
                viewHolder.mReview.setAlpha(1.0f);
            }
        }
        else {
            viewHolder.mReview.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }
}
