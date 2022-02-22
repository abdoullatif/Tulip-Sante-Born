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

import com.example.tulipsante.models.TypeVaccination;
import com.example.tulipsante.models.VaccinationPatient;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

import java.util.ArrayList;
import java.util.List;

public class VaccineTableAdapter extends RecyclerView.Adapter<VaccineTableAdapter.ViewHolder> {
    private Context cont;
    private List<TypeVaccination> typeVaccinations = new ArrayList<>();
    public List<VaccinationPatient> vaccinationPatients = new ArrayList<>();

    public void setTypeVaccinations(TypeVaccination vaccination) {
        if(!containsValue(typeVaccinations,vaccination.getType())) {
            typeVaccinations.add(vaccination);
            vaccinationPatients.add(new VaccinationPatient(
                    GeneralPurposeFunctions.idTable(),
                    "",
                    vaccination.getIdTypeVaccination(),
                    "",
                    ""
            ));
        }
        else {
            Toast.makeText(cont, "Valeur exist déjà!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean containsValue(final List<TypeVaccination> typeVaccinations, final String description) {
        return typeVaccinations.stream().anyMatch(o -> o.getType().equals(description));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.diagnostic_patology_table_item,parent,false);
        cont = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewPatology.setText(typeVaccinations.get(position).getType());
        holder.buttonAction.setOnClickListener(view -> {
            typeVaccinations.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return typeVaccinations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPatology;
        private CardView buttonAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPatology = itemView.findViewById(R.id.textViewPatology);
            buttonAction = itemView.findViewById(R.id.buttonAction);
        }
    }
}
