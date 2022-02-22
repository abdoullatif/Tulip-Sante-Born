package com.example.tulipsante.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.models.uIModels.SignesVitauxXCatSignesVitaux;
import com.example.tulipsante.R;

import java.util.ArrayList;
import java.util.List;

public class VitalsStatusTableAdapter extends RecyclerView.Adapter<VitalsStatusTableAdapter.ViewHolder> {
    private List<SignesVitauxXCatSignesVitaux> signesVitauxList = new ArrayList<>();

    public void setSignesVitauxList(List<SignesVitauxXCatSignesVitaux> signesVitauxList) {
        this.signesVitauxList = signesVitauxList;
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
        holder.textViewId.setText(signesVitauxList.get(position).getDescription());
        holder.textViewSymptom.setText(signesVitauxList.get(position).getValeur());
    }

    @Override
    public int getItemCount() {
        return signesVitauxList.size();
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
