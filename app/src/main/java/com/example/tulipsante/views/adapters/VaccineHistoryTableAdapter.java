package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.models.uIModels.VaccinePatient;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.views.dialogFragment.VaccHistoryDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VaccineHistoryTableAdapter extends RecyclerView.Adapter<VaccineHistoryTableAdapter.ViewHolder> {
    private List<VaccinePatient> vaccinePatientList = new ArrayList<>();
    private VaccHistoryDialogFragment vaccHistoryDialogFragment;
    private FragmentManager fm;
    private Context context;

    public VaccineHistoryTableAdapter(FragmentManager fm, Context context) {
        this.fm = fm;
        this.context = context;
    }

    public void setVaccinePatientList(List<VaccinePatient> vaccinePatientList) {
        this.vaccinePatientList = vaccinePatientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.vaccine_history_table_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewVaccine.setText(vaccinePatientList.get(position).getType());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:MM:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(vaccinePatientList.get(position).getDateVaccination()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.YEAR, Integer.parseInt(vaccinePatientList.get(position).getDuree()));
        String outDate = sdf.format(c.getTime());
        if(GeneralPurposeFunctions.hasVaccineExpired(outDate)) {
            holder.cardViewValid.setVisibility(View.INVISIBLE);
            holder.cardViewInvalid.setVisibility(View.VISIBLE);
        }
        else {
            holder.cardViewValid.setVisibility(View.VISIBLE);
            holder.cardViewInvalid.setVisibility(View.INVISIBLE);
        }
        holder.buttonAction.setOnClickListener(view -> {
            vaccHistoryDialogFragment = new VaccHistoryDialogFragment(context,vaccinePatientList.get(position).getType());
            vaccHistoryDialogFragment.show(fm,"ModalBottomSheet");
        });
    }

    @Override
    public int getItemCount() {
        return vaccinePatientList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewVaccine;
        private CardView cardViewValid, cardViewInvalid,buttonAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVaccine = itemView.findViewById(R.id.textViewVaccine);
            cardViewValid = itemView.findViewById(R.id.cardViewValid);
            cardViewInvalid = itemView.findViewById(R.id.cardViewInvalid);
            buttonAction = itemView.findViewById(R.id.buttonAction);
        }
    }
}
