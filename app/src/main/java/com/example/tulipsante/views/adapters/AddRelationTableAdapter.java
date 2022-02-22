package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.interfaces.OnAddRelationClicked;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.models.Relation;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

import java.util.ArrayList;
import java.util.List;

public class AddRelationTableAdapter extends RecyclerView.Adapter<AddRelationTableAdapter.ViewHolder> {
    private Context cont;
    private List<Patient> patients = new ArrayList<>();
    private OnAddRelationClicked listener;
    private SharedPreferences sharedPreferences;
    private String idPatient;

    public AddRelationTableAdapter(Context context, Fragment fragment) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        idPatient = sharedPreferences.getString("IDPATIENT",null);
        listener = (OnAddRelationClicked) fragment;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.patient_relation_table_item,parent,false);
        cont = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(!patients.get(position).getIdPatient().equals(idPatient)) {
            holder.textViewId.setText(patients.get(position).getIdPatient());
            holder.textViewFull.setText(patients.get(position).getNomPatient() + " " + patients.get(position).getPrenomPatient());
            holder.textViewBirth.setText(patients.get(position).getDateNaissancePatient());
            holder.buttonValidate.setOnClickListener(view -> {
                listener.onAddRelation(new Relation(
                        GeneralPurposeFunctions.idTable(),
                        "",
                        patients.get(position).getIdPatient(),
                        "",
                        ""
                ), patients.get(position).getGenrePatient());
            });
        }
        else {
            holder.buttonValidate.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewId;
        private TextView textViewFull;
        private TextView textViewBirth;
        private CardView buttonValidate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBirth = itemView.findViewById(R.id.textViewBirthDate);
            textViewFull = itemView.findViewById(R.id.textViewFull);
            textViewId = itemView.findViewById(R.id.textViewId);
            buttonValidate = itemView.findViewById(R.id.buttonValidate);
        }
    }
}
