package com.example.tulipsante.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.models.Conseil;
import com.example.tulipsante.R;

import java.util.ArrayList;
import java.util.List;

public class AdviceStatusTableAdapter extends RecyclerView.Adapter<AdviceStatusTableAdapter.ViewHolder> {
    private List<Conseil> conseilList = new ArrayList<>();

    public void setConseilList(List<Conseil> conseilList) {
        this.conseilList = conseilList;
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
        holder.textViewId.setText(String.valueOf(position+ 1));
        holder.textViewDescription.setText(conseilList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return conseilList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewId,textViewDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewDescription = itemView.findViewById(R.id.textViewSymptom);
        }
    }
}

