package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.interfaces.OnDeleteClicked;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.models.uIModels.RelationXPatient;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.views.activities.ConsultationActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RelationMTableAdapter extends RecyclerView.Adapter<RelationMTableAdapter.ViewHolder> {
    private Context cont;
    private List<RelationXPatient> relationData = new ArrayList<>();

    private OnDeleteClicked listener;

    public void setRelationData(List<RelationXPatient> relationData) {
        this.relationData = relationData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.relation_management_table_item,parent,false);
        cont = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        listener = (OnDeleteClicked) cont;
        Patient patient = new Patient(
                relationData.get(position).getIdPatient(),
                relationData.get(position).getNomPatient(),
                relationData.get(position).getPrenomPatient(),
                relationData.get(position).getGenrePatient(),
                relationData.get(position).getDateNaissancePatient(),
                relationData.get(position).getGroupeSanguinPatient(),
                relationData.get(position).getPhotoPatient(),
                relationData.get(position).getNumeroIdentitePatient(),
                relationData.get(position).getUidPatient(),
                relationData.get(position).getNationalitePatient(),
                relationData.get(position).getStatusMatrimonialPatient(),
                relationData.get(position).getIdAddresse(),
                relationData.get(position).getDateRegistration(),
                relationData.get(position).getFlagTransmis()
        );
        holder.textViewRelation.setText(relationData.get(position).getTypeRelation());
        holder.textViewLast.setText(relationData.get(position).getNomPatient());
        holder.textViewFirst.setText(relationData.get(position).getPrenomPatient());
        holder.textViewDate.setText(relationData.get(position).getDateNaissancePatient());
        holder.cardViewConsult.setOnClickListener(view -> {
            Intent intent = new Intent(cont, ConsultationActivity.class);
            intent.putExtra("From", "Patient List");
            intent.putExtra("Patient", patient);
            cont.startActivity(intent);
        });
        holder.cardViewDelete.setOnClickListener(view -> {
            listener.onClick(relationData.get(position).getIdRelation());
        });
    }

    @Override
    public int getItemCount() {
        return relationData.size();
    }

    public void clear() {
        relationData.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewRelation, textViewLast, textViewFirst, textViewDate;
        private CardView cardViewConsult, cardViewDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRelation = itemView.findViewById(R.id.textViewRelation);
            textViewLast = itemView.findViewById(R.id.textViewLast);
            textViewFirst = itemView.findViewById(R.id.textViewFirst);
            textViewDate = itemView.findViewById(R.id.textViewBirthDate);
            cardViewConsult = itemView.findViewById(R.id.buttonPatientConsult);
            cardViewDelete = itemView.findViewById(R.id.cardViewDelete);
        }

    }
}
