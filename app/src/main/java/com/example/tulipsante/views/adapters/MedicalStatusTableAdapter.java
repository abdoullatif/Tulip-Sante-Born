package com.example.tulipsante.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.R;

import java.util.ArrayList;
import java.util.List;

public class MedicalStatusTableAdapter extends RecyclerView.Adapter<MedicalStatusTableAdapter.ViewHolder> {
    private List<String> symptomList = new ArrayList<>();

    public void setSymptomList(List<String> symptomList) {
        this.symptomList = symptomList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.medical_status_table_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewId.setText(String.valueOf(position+1));
        holder.textViewSymptom.setText(symptomList.get(position));
    }

    @Override
    public int getItemCount() {
        return symptomList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewSymptom, textViewId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewSymptom = itemView.findViewById(R.id.textViewSymptom);
        }
    }
}
