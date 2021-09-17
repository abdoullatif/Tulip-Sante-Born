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
import com.example.tulipsante.R;
import com.example.tulipsante.models.Symptome;
import com.example.tulipsante.models.SymptomesPatient;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import java.util.ArrayList;
import java.util.List;

public class MedicalTableAdapter extends RecyclerView.Adapter<MedicalTableAdapter.ViewHolder> {
    private Context cont;
    private List<Symptome> symptomeList = new ArrayList<>();
    public List<SymptomesPatient> symptomesPatients = new ArrayList<>();

    public void setSymptomeList(Symptome symptome) {
        if(!containsValue(symptomeList,symptome.getDescription())) {
            symptomeList.add(symptome);
            symptomesPatients.add(new SymptomesPatient(
                    GeneralPurposeFunctions.idTable(),
                    symptome.getIdSymptome(),
                    "",
                    ""
            ));
        }
        else {
            Toast.makeText(cont, "Valeur exist déjà!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean containsValue(final List<Symptome> symptomes, final String description) {
        return symptomes.stream().anyMatch(o -> o.getDescription().equals(description));
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
        holder.textViewPatology.setText(symptomeList.get(position).getDescription());
        holder.buttonAction.setOnClickListener(view -> {
            symptomeList.remove(position);
            symptomesPatients.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return symptomeList.size();
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
