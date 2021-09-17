package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tulipsante.R;
import com.example.tulipsante.models.Medecin;
import com.example.tulipsante.models.Reference;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.viewModel.AddReferenceViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddReferenceTableAdapter extends RecyclerView.Adapter<AddReferenceTableAdapter.ViewHolder> {
    private Context cont;
    private List<Medecin> medecinList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String idMedecin;
    private AddReferenceViewModel addReferenceViewModel;
    private String idConsultation;

    public AddReferenceTableAdapter(Context cont, AddReferenceViewModel addReferenceViewModel, String idConsultation) {
        this.addReferenceViewModel = addReferenceViewModel;
        this.idConsultation = idConsultation;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cont);
        idMedecin = sharedPreferences.getString("IDMEDECIN",null);
    }

    public void setMedecinList(List<Medecin> medecinList) {
        this.medecinList = medecinList;
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
        if(medecinList.get(position).getIdMedecin().equals(idMedecin)) {
            holder.buttonValidate.setVisibility(View.INVISIBLE);
        }
        File myPath = new File(
                Environment
                        .getExternalStorageDirectory()+
                        File.separator+
                        "Tulip_sante/Medecin/"+
                        medecinList.get(position).getPhoto());
        try {
            holder.profile_image.setVisibility(View.VISIBLE);
            Glide.with(cont).load(myPath).into(holder.profile_image);
        }
        catch (Exception e) {
            System.out.println("No photo!");
        }
        holder.textViewFirst.setText(medecinList.get(position).getPrenomMedecin());
        holder.textViewLast.setText(medecinList.get(position).getNomMedecin());
        holder.textViewBirth.setText(medecinList.get(position).getDateDeNaissance());
        holder.buttonValidate.setOnClickListener(view -> {
            // TODO: 16/06/21 add into reference table
            System.out.println(idConsultation);
            System.out.println(idMedecin);
            System.out.println(medecinList.get(position).getIdMedecin());
            addReferenceViewModel.insertReference(
                    new Reference(
                            GeneralPurposeFunctions.idTable(),
                            idConsultation,
                            idMedecin,
                            medecinList.get(position).getIdMedecin(),
                            "reference",
                            "",
                            "",
                            ""
                    )
            );
            Toast.makeText(cont, "Success!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return medecinList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView profile_image;
        private TextView textViewFirst,
                textViewLast,
                textViewBirth,
                textViewProfession;
        private CardView buttonValidate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            textViewFirst = itemView.findViewById(R.id.textViewFirst);
            textViewLast = itemView.findViewById(R.id.textViewLast);
            textViewBirth = itemView.findViewById(R.id.textViewBirth);
            textViewProfession = itemView.findViewById(R.id.textViewProfession);
            buttonValidate = itemView.findViewById(R.id.buttonValidate);
        }
    }
}
