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

import com.example.tulipsante.models.Examen;
import com.example.tulipsante.models.TypeExamens;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

import java.util.ArrayList;
import java.util.List;

public class ExamTableAdapter extends RecyclerView.Adapter<ExamTableAdapter.ViewHolder> {

    private Context cont;
    private List<TypeExamens> typeExamens = new ArrayList<>();
    public List<Examen> examen = new ArrayList<>();

    public void setTypeExamens(TypeExamens typeExamen) {
        if(!containsValue(typeExamens,typeExamen.getDescription())) {
            typeExamens.add(typeExamen);
            examen.add(new Examen(
                    GeneralPurposeFunctions.idTable(),
                    "",
                    typeExamen.getIdTypeExamens(),
                    typeExamen.getDescription(),
                    ""
            ));
        }
        else {
            Toast.makeText(cont, "Valeur exist déjà!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean containsValue(final List<TypeExamens> typeExamens, final String description) {
        return typeExamens.stream().anyMatch(o -> o.getDescription().equals(description));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.investigation_table_item,parent,false);
        cont = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewExam.setText(typeExamens.get(position).getTypeExamens());
        holder.textViewDate.setText(typeExamens.get(position).getDescription());
        holder.buttonAction.setOnClickListener(view -> {
            typeExamens.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return typeExamens.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewExam, textViewDate;
        private CardView buttonAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewExam = itemView.findViewById(R.id.textViewExam);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            buttonAction = itemView.findViewById(R.id.buttonAction);
        }
    }
}