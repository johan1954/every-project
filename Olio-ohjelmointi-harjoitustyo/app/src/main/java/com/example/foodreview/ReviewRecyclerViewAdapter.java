package com.example.foodreview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ReviewViewHolder> {
    private ArrayList<Review> mReviewList;
    private OnItemClickListener mListener;
    private boolean isAdmin;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener) { mListener = listener; }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView mFoodName, mGrade, mText;
        ImageView mEdit, mDelete;

        public ReviewViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mFoodName = itemView.findViewById(R.id.review_foodName);
            mGrade = itemView.findViewById(R.id.review_grade);
            mText = itemView.findViewById(R.id.review_text);
            mEdit = itemView.findViewById(R.id.review_edit);
            mDelete = itemView.findViewById(R.id.review_delete);

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

            mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    ReviewRecyclerViewAdapter(ArrayList<Review> reviewList, boolean isAdminArg) {
        mReviewList = reviewList;
        isAdmin = isAdminArg;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reviewrecyclerview_row, viewGroup, false);
        return new ReviewViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
        Review currentItem = mReviewList.get(i);

        if (isAdmin) {
            String text = currentItem.getFoodName() + " - " + currentItem.getUserId();
            reviewViewHolder.mFoodName.setText(text);
        } else {
            reviewViewHolder.mFoodName.setText(currentItem.getFoodName());
        }
        String grade = " " + currentItem.getGrade() + " / 5.0";
        reviewViewHolder.mGrade.setText(grade);
        reviewViewHolder.mText.setText(currentItem.getReview());


    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }
}
