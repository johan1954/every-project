package com.example.foodreview;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminRecyclerViewAdapter extends RecyclerView.Adapter<AdminRecyclerViewAdapter.AdminViewHolder> {
    private ArrayList<Restaurant> mRestaurantList;
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
        TextView mAddress;
        ImageView mEdit;
        ImageView mDelete;
        CardView mCard;

        AdminViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mName = itemView.findViewById(R.id.admin_name);
            mAddress = itemView.findViewById(R.id.admin_address);
            mDelete = itemView.findViewById(R.id.admin_delete);
            mEdit = itemView.findViewById(R.id.admin_edit);
            mCard = itemView.findViewById(R.id.admin_cardview);

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

    AdminRecyclerViewAdapter(ArrayList<Restaurant> restaurantList) {
        mRestaurantList = restaurantList;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adminrecyclerview_row, viewGroup, false);
        return new AdminViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder adminViewHolder, int i) {
        Restaurant currentItem = mRestaurantList.get(i);

        adminViewHolder.mName.setText(currentItem.getRestaurantName());
        adminViewHolder.mAddress.setText(currentItem.getRestaurantAddress());


        if (!currentItem.getIsEnabled()) {
            adminViewHolder.mName.setAlpha(0.3f);
            adminViewHolder.mAddress.setAlpha(0.4f);
        }
        else {
            adminViewHolder.mName.setAlpha(1.0f);
            adminViewHolder.mAddress.setAlpha(1.0f);
        }


    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }

}

