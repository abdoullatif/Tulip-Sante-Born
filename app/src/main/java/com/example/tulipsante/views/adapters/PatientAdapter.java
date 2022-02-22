package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tulipsante.views.activities.ConsultationActivity;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.R;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    private Context cont;
    private String activityName;
    private List<Patient> patients = new ArrayList<>();

    public PatientAdapter(String activityName) {
        this.activityName = activityName;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        cont = parent.getContext();

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.table_patient_list_item, parent, false);
                viewHolder = new PatientViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                PatientViewHolder patientViewHolder = (PatientViewHolder) holder;
                Calendar calendar = Calendar.getInstance();
                if(patients.get(position).getNomPatient() != null) {
                    patientViewHolder.profile.setVisibility(View.VISIBLE);
                    patientViewHolder.buttonPatientRecord.setVisibility(View.VISIBLE);
                    patientViewHolder.id.setText(patients.get(position).getIdPatient());
                    File myPath = new File(
                            Environment
                                    .getExternalStorageDirectory()+
                                    File.separator+
                                    "Tulip_sante/Patients/"+
                                    patients.get(position).getIdPatient()+
                                    "/Personal/"+
                                    patients.get(position).getPhotoPatient());
                    try {
                        patientViewHolder.profile.setVisibility(View.VISIBLE);
                        Glide.with(cont).load(myPath).into(patientViewHolder.profile);
                    } catch (Exception e) {
                        System.out.println("No photo!");
                    }
                    patientViewHolder.last.setText(patients.get(position).getNomPatient());
                    patientViewHolder.first.setText(patients.get(position).getPrenomPatient());
                    Date date = GeneralPurposeFunctions.getStringDate(patients.get(position).getDateNaissancePatient());
                    if (date != null) {
                        calendar.setTime(date);
                    }
                    patientViewHolder.age.setText(GeneralPurposeFunctions.getAge(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR)));
                    patientViewHolder.gender.setText(patients.get(position).getGenrePatient());
                    patientViewHolder.dob.setText(patients.get(position).getDateNaissancePatient());
                    patientViewHolder.buttonPatientRecord.setOnClickListener(view -> {
                        Intent intent = new Intent(cont, ConsultationActivity.class);
                        intent.putExtra("From", "Patient List");
                        intent.putExtra("Patient", patients.get(position));
                        cont.startActivity(intent);
                    });
                }
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == patients.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Patient());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = patients.size() - 1;
        if(patients.get(position).getIdPatient() == null) {
            Patient result = getItem(position);
            if (result != null) {
                patients.remove(position);
            }
        }
    }

    public void add(Patient patient) {
        patients.add(patient);
    }

    public void clear() {
        patients.clear();
    }

    public Patient getItem(int position) {
        return patients.get(position);
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {
        private TextView id, last, first, age, gender, dob;
        private CardView buttonPatientRecord;
        private ImageView profile;
        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.textViewIdentifier);
            last = itemView.findViewById(R.id.textViewLast);
            first = itemView.findViewById(R.id.textViewFirst);
            age = itemView.findViewById(R.id.textViewAge);
            gender = itemView.findViewById(R.id.textViewGender);
            profile = itemView.findViewById(R.id.profile_image);
            buttonPatientRecord = itemView.findViewById(R.id.buttonPatientRecord);
            dob = itemView.findViewById(R.id.textViewDOB);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loadmore_progress);

        }
    }
}
