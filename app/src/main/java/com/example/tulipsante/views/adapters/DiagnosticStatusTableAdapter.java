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

public class DiagnosticStatusTableAdapter extends RecyclerView.Adapter<DiagnosticStatusTableAdapter.ViewHolder> {
    private List<String> diagnosticList = new ArrayList<>();

    public void setDiagnosticList(List<String> diagnosticList) {
        this.diagnosticList = diagnosticList;
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
        holder.textViewSymptom.setText(diagnosticList.get(position));
    }

    @Override
    public int getItemCount() {
        return diagnosticList.size();
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

