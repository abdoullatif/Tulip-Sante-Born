package com.example.tulipsante.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.models.uIModels.VaccineTableDialog;
import com.example.tulipsante.R;

import java.util.ArrayList;
import java.util.List;

public class VaccMoreTableAdapter extends RecyclerView.Adapter<VaccMoreTableAdapter.ViewHolder> {
    List<VaccineTableDialog> vaccineTableDialogs = new ArrayList<>();

    public void setVaccineTableDialogs(List<VaccineTableDialog> vaccineTableDialogs) {
        this.vaccineTableDialogs = vaccineTableDialogs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.historique_table_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewDate.setText(vaccineTableDialogs.get(position).getDateVaccination());
        holder.textViewVaccine.setText(vaccineTableDialogs.get(position).getType());
        holder.textViewConsultId.setText(vaccineTableDialogs.get(position).getIdConsultation());
    }

    @Override
    public int getItemCount() {
        return vaccineTableDialogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate, textViewVaccine, textViewConsultId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewVaccine = itemView.findViewById(R.id.textViewDoc);
            textViewConsultId = itemView.findViewById(R.id.textViewDescription);

        }
    }
}
