package com.example.tulipsante.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.models.Prescription;
import com.example.tulipsante.R;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionStatusTableAdapter extends RecyclerView.Adapter<PrescriptionStatusTableAdapter.ViewHolder> {
    private List<Prescription> prescriptionList = new ArrayList<>();

    public void setPrescriptionList(List<Prescription> prescriptionList) {
        this.prescriptionList = prescriptionList;
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
        holder.textViewMedecine.setText(prescriptionList.get(position).getDescription());
        holder.textViewDate.setText(prescriptionList.get(position).getDatePrescription());
    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMedecine,textViewDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMedecine = itemView.findViewById(R.id.textViewId);
            textViewDate = itemView.findViewById(R.id.textViewSymptom);
        }
    }
}

