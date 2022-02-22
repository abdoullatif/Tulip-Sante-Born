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

import com.example.tulipsante.models.Diagnostic;
import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

import java.util.ArrayList;
import java.util.List;

public class PatologyTableAdapter extends RecyclerView.Adapter<PatologyTableAdapter.ViewHolder> {
    private Context cont;
    private List<Maladie> maladies = new ArrayList<>();
    public List<Diagnostic> diagnostics = new ArrayList<>();

    public void setMaladies(Maladie maladie) {
        if(!containsValue(maladies,maladie.getDescription())) {
            maladies.add(maladie);
            diagnostics.add(new Diagnostic(
                    GeneralPurposeFunctions.idTable(),
                    maladie.getIdMaladie(),
                    "",
                    maladie.getDescription(),
                    ""
            ));
        }
        else {
            Toast.makeText(cont, "Valeur exist déjà!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean containsValue(final List<Maladie> maladies, final String description) {
        return maladies.stream().anyMatch(o -> o.getDescription().equals(description));
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
        holder.textViewPatology.setText(maladies.get(position).getDescription());
        holder.buttonAction.setOnClickListener(view -> {
            maladies.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return maladies.size();
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
