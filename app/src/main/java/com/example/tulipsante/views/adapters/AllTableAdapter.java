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
import com.example.tulipsante.models.Allergie;
import com.example.tulipsante.models.AllergiePatient;
import com.example.tulipsante.models.AntecedentPatient;
import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import java.util.ArrayList;
import java.util.List;

public class AllTableAdapter extends RecyclerView.Adapter<AllTableAdapter.ViewHolder> {
    private Context cont;
    public List<Allergie> allergies = new ArrayList<>();
    public List<AllergiePatient> allergiePatientList = new ArrayList<>();

    public void setAllergies(Allergie allergie) {
        if(!containsValue(allergies,allergie.getDescription())) {
            allergies.add(allergie);
            allergiePatientList.add(new AllergiePatient(
                    GeneralPurposeFunctions.idTable(),
                    allergie.getIdAllergie(),
                    "",
                    ""
            ));
        }
        else {
            Toast.makeText(cont, "Valeur exist déjà!", Toast.LENGTH_SHORT).show();
        }
    }


    public AllTableAdapter(Context cont) {
        this.cont = cont;
    }

    public boolean containsValue(final List<Allergie> allergies, final String description) {
        return allergies.stream().anyMatch(o -> o.getDescription().equals(description));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.diagnostic_patology_table_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewPatology.setText(allergies.get(position).getDescription());
        holder.buttonAction.setOnClickListener(view -> {
            allergies.remove(position);
            allergiePatientList.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return allergies.size();
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
