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
import com.example.tulipsante.R;
import com.example.tulipsante.models.Symptome;
import com.example.tulipsante.models.SymptomesPatient;
import com.example.tulipsante.models.Vice;
import com.example.tulipsante.models.VicePatient;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import java.util.ArrayList;
import java.util.List;

public class ViceTableAdapter extends RecyclerView.Adapter<ViceTableAdapter.ViewHolder> {
    private Context cont;
    public List<Vice> viceList = new ArrayList<>();
    public List<VicePatient> vicePatientList = new ArrayList<>();

    public void setViceList(Vice vice) {
        if(!containsValue(viceList,vice.getDescription())) {
            viceList.add(vice);
            vicePatientList.add(new VicePatient(
                    GeneralPurposeFunctions.idTable(),
                    vice.getIdVice(),
                    "",
                    ""
            ));
        }
        else {
            Toast.makeText(cont, "Valeur exist déjà!", Toast.LENGTH_SHORT).show();
        }
    }


    public ViceTableAdapter(Context cont) {
        this.cont = cont;
    }

    public boolean containsValue(final List<Vice> vices, final String description) {
        return vices.stream().anyMatch(o -> o.getDescription().equals(description));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.diagnostic_patology_table_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewPatology.setText(viceList.get(position).getDescription());
        holder.buttonAction.setOnClickListener(view -> {
            viceList.remove(position);
            vicePatientList.remove((position));
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return viceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPatology;
        private CardView buttonAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPatology = itemView.findViewById(R.id.textViewPatology);
            buttonAction = itemView.findViewById(R.id.buttonAction);
        }
    }
}
