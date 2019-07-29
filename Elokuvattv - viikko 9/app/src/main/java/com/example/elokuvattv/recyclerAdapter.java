package com.example.elokuvattv;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.viewHolderRecycler> {

    private ArrayList<recyclerItem> movieList;

    public static class viewHolderRecycler extends RecyclerView.ViewHolder {
        public TextView textViewLine1;
        public TextView textViewLine2;
        public viewHolderRecycler(@NonNull View itemView) {
            super(itemView);
            textViewLine1 = itemView.findViewById(R.id.textBox1Recyle);
            textViewLine2 = itemView.findViewById(R.id.textBox2Recyle);
        }
    }

    public recyclerAdapter(ArrayList<recyclerItem> movies) {
        movieList = movies;
    }

    @NonNull
    @Override
    public viewHolderRecycler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_row_view, viewGroup, false);
        viewHolderRecycler vhr = new viewHolderRecycler(v);
        return vhr;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderRecycler viewHolderRecycler, int i) {
        recyclerItem currItem = movieList.get(i);

        viewHolderRecycler.textViewLine1.setText(currItem.getTextBox1());
        viewHolderRecycler.textViewLine2.setText(currItem.getTextBox2());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
