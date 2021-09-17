package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.models.Prescription;
import com.example.tulipsante.R;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionTableAdapter extends RecyclerView.Adapter<PrescriptionTableAdapter.ViewHolder> {

    private Context cont;
    public List<Prescription> prescriptions = new ArrayList<>();

    public void setPrescription(Prescription prescription) {
        if(true) {
            prescriptions.add(new Prescription(
                    prescription.getIdPrescription(),
                    "",
                    prescription.getDatePrescription(),
                    prescription.getDescription(),
                    ""
            ));
        }
        else {
            Toast.makeText(cont, "Valeur exist déjà!", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.prescription_table_item,parent,false);
        cont = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewPrescription.setText(prescriptions.get(position).getDescription());
        holder.textViewDate.setText(prescriptions.get(position).getDatePrescription());
        holder.buttonAction.setOnClickListener(view -> {
            prescriptions.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return prescriptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPrescription;
        private TextView textViewDate;
        private CardView buttonAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPrescription = itemView.findViewById(R.id.textViewPrescription);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            buttonAction = itemView.findViewById(R.id.buttonAction);
        }
    }
}
