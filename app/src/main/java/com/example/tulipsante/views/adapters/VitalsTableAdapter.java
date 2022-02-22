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

import com.example.tulipsante.models.SignesVitaux;
import com.example.tulipsante.models.uIModels.TempVitalValue;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

import java.util.ArrayList;
import java.util.List;

public class VitalsTableAdapter extends RecyclerView.Adapter<VitalsTableAdapter.ViewHolder> {

    private Context cont;
    private List<TempVitalValue> tempVitalValues = new ArrayList<>();
    public List<SignesVitaux> signesVitaux = new ArrayList<>();

    public void setTempVitalValue(TempVitalValue tempVitalValue) {
        if(!containsValue(tempVitalValues,tempVitalValue.getVital())) {
            tempVitalValues.add(tempVitalValue);
            signesVitaux.add(new SignesVitaux(
                    GeneralPurposeFunctions.idTable(),
                    tempVitalValue.getId(),
                    "",
                    tempVitalValue.getValue(),
                    ""
            ));
        }
        else {
            Toast.makeText(cont, "Valeur exist déjà!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean contains(String vital) {
        return tempVitalValues.stream().anyMatch(o -> o.getVital().equals(vital));
    }

    public boolean containsValue(final List<TempVitalValue> tempVitalValues, final String vital) {
        return tempVitalValues.stream().anyMatch(o -> o.getVital().equals(vital));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.category_vitals_table_item,parent,false);
        cont = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewVital.setText(tempVitalValues.get(position).getVital());
        holder.textViewValue.setText(tempVitalValues.get(position).getValue());
        holder.buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempVitalValues.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return tempVitalValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewVital, textViewValue;
        private CardView buttonAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewValue = itemView.findViewById(R.id.textViewValue);
            textViewVital = itemView.findViewById(R.id.textViewVital);
            buttonAction = itemView.findViewById(R.id.buttonAction);
        }
    }
}
