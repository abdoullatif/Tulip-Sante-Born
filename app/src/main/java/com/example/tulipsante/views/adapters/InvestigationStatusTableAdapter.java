package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.models.uIModels.ExamenXTypeExamen;
import com.example.tulipsante.R;
import com.example.tulipsante.views.dialogFragment.ImageDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InvestigationStatusTableAdapter extends RecyclerView.Adapter<InvestigationStatusTableAdapter.ViewHolder> {
    private List<ExamenXTypeExamen> investigationList = new ArrayList<>();
    private FragmentManager fm;
    private Context context;

    private SharedPreferences sharedPreferences;

    public InvestigationStatusTableAdapter(FragmentManager fm, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.fm = fm;
        this.context = context;
    }

    public void setInvestigationList(List<ExamenXTypeExamen> investigationList) {
        this.investigationList = investigationList;
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
        File path = new File(investigationList.get(position).getValeur());
        holder.textViewId.setText(investigationList.get(position).getTypeExamens());
        holder.textViewSymptom.setText(path.getName());
        holder.textViewSymptom.setOnClickListener(view -> {
            ImageDialogFragment fragment = new ImageDialogFragment(
                    context,
                    sharedPreferences.getString("IDPATIENT",null),
                    "Diagnostic",
                    path.getName());
            fragment.show(fm,"ModalBottomSheet");
        });
    }

    @Override
    public int getItemCount() {
        return investigationList.size();
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
