package com.example.foodreview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminFoodRecyclerViewAdapter extends RecyclerView.Adapter<AdminFoodRecyclerViewAdapter.AdminViewHolder> {
    private ArrayList<Food> mFoodList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mInfo;
        ImageView mEdit;
        ImageView mDelete;

        AdminViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mName = itemView.findViewById(R.id.admin_name);
            mInfo = itemView.findViewById(R.id.admin_info);
            mDelete = itemView.findViewById(R.id.admin_delete);
            mEdit = itemView.findViewById(R.id.admin_edit);

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
        }
    }

    AdminFoodRecyclerViewAdapter(ArrayList<Food> foodList) {
        mFoodList = foodList;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adminfoodrecyclerview_row, viewGroup, false);
        return new AdminViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder adminViewHolder, int i) {
        Food currentItem = mFoodList.get(i);

        adminViewHolder.mName.setText(currentItem.getFoodName());
        String infoText = currentItem.getDate() + " - " + currentItem.getFoodPrice() + "€";
        adminViewHolder.mInfo.setText(infoText);

    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

}
