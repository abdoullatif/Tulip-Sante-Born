package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tulipsante.R;
import com.example.tulipsante.models.uIModels.PatientXRefXDoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReferredByOthersTableAdapter extends RecyclerView.Adapter<ReferredByOthersTableAdapter.ViewHolder> {
    private Context cont;
    private List<PatientXRefXDoc> patientXRefXDocList = new ArrayList<>();

    public void setPatientXRefXDocList(List<PatientXRefXDoc> patientXRefXDocList) {
        this.patientXRefXDocList = patientXRefXDocList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.referred_patient_list_items, parent, false);
        cont = parent.getContext();
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File myPath = new File(
                Environment
                        .getExternalStorageDirectory()+
                        File.separator+
                        "Tulip_sante/Patients/"+
                        patientXRefXDocList.get(position).getIdPatient()+
                        "/Personal/"+
                        patientXRefXDocList.get(position).getPhotoPatient());
        File path = new File(
                Environment
                        .getExternalStorageDirectory()+
                        File.separator+
                        "Tulip_sante/Medecin/"+
                        patientXRefXDocList.get(position).getPhoto());
        try {
            holder.profilePatient.setVisibility(View.VISIBLE);
            Glide.with(cont).load(myPath).into(holder.profilePatient);
        }
        catch (Exception e) {
            System.out.println("No photo!");
        }
        holder.last.setText(patientXRefXDocList.get(position).getNomPatient());
        holder.first.setText(patientXRefXDocList.get(position).getPrenomPatient());
        holder.dob.setText(patientXRefXDocList.get(position).getDateNaissancePatient());
        try {
            holder.profileMedecin.setVisibility(View.VISIBLE);
            Glide.with(cont).load(path).into(holder.profileMedecin);
        }
        catch (Exception e) {
            System.out.println("No photo!");
        }
        holder.referredBy.setText(patientXRefXDocList.get(position).getNomMedecin() + " " + patientXRefXDocList.get(position).getPrenomMedecin());
        holder.date.setText(patientXRefXDocList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return patientXRefXDocList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView last, first, dob, referredBy, date;
        private CardView buttonPatientConsult, buttonPatientRecord;
        private ImageView profilePatient;
        private ImageView profileMedecin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            last = itemView.findViewById(R.id.textViewLast);
            first = itemView.findViewById(R.id.textViewFirst);
            dob = itemView.findViewById(R.id.textViewDOB);
            referredBy = itemView.findViewById(R.id.textViewReferred);
            date = itemView.findViewById(R.id.textViewDate);
            profilePatient = itemView.findViewById(R.id.profile_image);
            profileMedecin = itemView.findViewById(R.id.profile_image_doc);
            buttonPatientRecord = itemView.findViewById(R.id.buttonPatientRecord);
            buttonPatientConsult = itemView.findViewById(R.id.buttonPatientConsult);
        }
    }}
