package com.example.foodreview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminUserManagerRecyclerViewAdapter extends RecyclerView.Adapter<AdminUserManagerRecyclerViewAdapter.AdminUserManagerViewHolder>{
    private ArrayList<User> mUserList;
    private OnItemClickListener mListener;
    private String currentAdmin;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCheckboxClick(int position, boolean isChecked);
    }

    void setOnItemClickListener(OnItemClickListener listener) { mListener = listener; }

    static class AdminUserManagerViewHolder extends RecyclerView.ViewHolder {
        TextView mUsername;
        CheckBox mCheckbox;

        public AdminUserManagerViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mUsername = itemView.findViewById(R.id.adminusermanager_username);
            mCheckbox = itemView.findViewById(R.id.adminusermanager_isadmin);

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

            mCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCheckboxClick(position, mCheckbox.isChecked());
                        }
                    }
                }
            });

        }
    }

    AdminUserManagerRecyclerViewAdapter(ArrayList<User> userList, String username) {
        mUserList = userList;
        currentAdmin = username;
    }

    @NonNull
    @Override
    public AdminUserManagerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adminusermanagerrecyclerview_row, viewGroup, false);
        return new AdminUserManagerViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserManagerViewHolder adminUserManagerViewHolder, int i) {
        User currentItem = mUserList.get(i);

        if (currentItem.getIsAdmin()) {
            adminUserManagerViewHolder.mCheckbox.setChecked(true);
        }
        if (currentItem.getUsername().equals(currentAdmin)) {
            adminUserManagerViewHolder.mCheckbox.setEnabled(false);
        }

        adminUserManagerViewHolder.mUsername.setText(currentItem.getUsername());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }
}
