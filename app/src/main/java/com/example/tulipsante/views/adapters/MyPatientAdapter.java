package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.R;
import com.example.tulipsante.views.activities.ConsultationActivity;
import com.example.tulipsante.views.activities.PatientRecordActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyPatientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    private Context context;
    private Context cont;
    private String activityName;
    private List<Patient> patients = new ArrayList<>();


    public MyPatientAdapter(Context context,String activityName) {
        this.context = context;
        this.activityName = activityName;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        cont = parent.getContext();

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.my_patient_list_items, parent, false);
                viewHolder = new MyPatientViewHolder(viewItem);
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
                MyPatientViewHolder myPatientViewHolder = (MyPatientViewHolder) holder;
                Calendar calendar = Calendar.getInstance();
                if(patients.get(position).getNomPatient() != null) {
                    myPatientViewHolder.profile.setVisibility(View.VISIBLE);
                    try {
                        File myPath = new File(
                                Environment
                                        .getExternalStorageDirectory()+
                                        File.separator+
                                        "Tulip_sante/Patients/"+
                                        patients.get(position).getIdPatient()+
                                        "/Personal/"+
                                        patients.get(position).getPhotoPatient());
                        try {
                            myPatientViewHolder.profile.setVisibility(View.VISIBLE);
                            Glide.with(cont).load(myPath).into(myPatientViewHolder.profile);
                        }
                        catch (Exception e) {
                            System.out.println("No photo!");
                        }
                        myPatientViewHolder.last.setText(patients.get(position).getNomPatient());
                        myPatientViewHolder.first.setText(patients.get(position).getPrenomPatient());
                        Date date = GeneralPurposeFunctions.getStringDate(patients.get(position).getDateNaissancePatient());
                        calendar.setTime(date);
                        myPatientViewHolder.age.setText(GeneralPurposeFunctions.getAge(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR)));
                        myPatientViewHolder.dob.setText(patients.get(position).getDateNaissancePatient());
                        myPatientViewHolder.id.setText(patients.get(position).getIdPatient());
                        myPatientViewHolder.gender.setText(patients.get(position).getGenrePatient());
                        myPatientViewHolder.buttonPatientConsult.setOnClickListener(view -> {
                            Intent intent = new Intent(cont, ConsultationActivity.class);
                            intent.putExtra("From", "Patient List");
                            intent.putExtra("Patient", patients.get(position));
                            cont.startActivity(intent);
                        });
                        myPatientViewHolder.buttonPatientRecord.setOnClickListener(view -> {
                            Intent intent = new Intent(cont, PatientRecordActivity.class);
                            intent.putExtra("From", "Patient List");
                            intent.putExtra("Patient", patients.get(position));
                            cont.startActivity(intent);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
        System.out.println((position == patients.size() - 1 && isLoadingAdded));
        return (position == patients.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Patient());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = patients.size() - 1;
        Patient result = getItem(position);
        if (result != null) {
            patients.remove(position);
        }
    }

    public void add(Patient patient) {
        patients.add(patient);
    }

    public void clear() {
        patients.clear();
        notifyDataSetChanged();
    }

    public Patient getItem(int position) {
        return patients.get(position);
    }

    public static class MyPatientViewHolder extends RecyclerView.ViewHolder {
        private TextView id, last, first, age, gender, dob, phone;
        private CardView buttonPatientConsult, buttonPatientRecord;
        private ImageView profile;
        public MyPatientViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.textViewIdentifier);
            last = itemView.findViewById(R.id.textViewLast);
            first = itemView.findViewById(R.id.textViewFirst);
            age = itemView.findViewById(R.id.textViewAge);
            gender = itemView.findViewById(R.id.textViewGender);
            profile = itemView.findViewById(R.id.profile_image);
            buttonPatientRecord = itemView.findViewById(R.id.buttonPatientRecord);
            buttonPatientConsult = itemView.findViewById(R.id.buttonPatientConsult);
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