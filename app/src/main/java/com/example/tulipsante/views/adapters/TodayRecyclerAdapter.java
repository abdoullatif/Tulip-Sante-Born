package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.models.Nouvelles;
import com.example.tulipsante.R;

import java.util.ArrayList;
import java.util.List;

public class TodayRecyclerAdapter extends RecyclerView.Adapter<TodayRecyclerAdapter.ViewHolder> {

    private Context cont;
    private List<Nouvelles> data = new ArrayList<>();

    public void setNewsItems(List<Nouvelles> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.today_list_items,parent,false);
        cont = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewTitle.setText(data.get(position).getTitre());
        holder.textViewBody.setText(data.get(position).getContenu());
        holder.textViewDate.setText(data.get(position).getDateAdded());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewBody, textViewDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBody = itemView.findViewById(R.id.textViewBody);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}
