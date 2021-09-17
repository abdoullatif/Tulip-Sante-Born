package com.example.tulipsante.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.R;

import java.util.ArrayList;
import java.util.List;

public class AntecedentRecViewAdapter extends RecyclerView.Adapter<AntecedentRecViewAdapter.ViewHolder> {
    private List<Maladie> maladieList = new ArrayList<>();

    public void setMaladieList(List<Maladie> maladieList ) {
        this.maladieList = maladieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.allergie_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewValue.setText(maladieList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return maladieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewValue;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewValue = itemView.findViewById(R.id.textViewValue);
        }

    }
}
