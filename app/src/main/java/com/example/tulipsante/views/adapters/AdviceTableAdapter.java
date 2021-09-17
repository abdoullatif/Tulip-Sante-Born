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

import com.example.tulipsante.models.Conseil;
import com.example.tulipsante.R;

import java.util.ArrayList;
import java.util.List;

public class AdviceTableAdapter extends RecyclerView.Adapter<AdviceTableAdapter.ViewHolder> {

    private Context cont;
    public List<Conseil> conseils = new ArrayList<>();

    public void setAdvice(Conseil cons) {
        if(true) {
            conseils.add(new Conseil(
                    cons.getIdConseil(),
                    "",
                    cons.getDescription(),
                    ""
            ));
        }
        else {
            Toast.makeText(cont, "Valeur exist déjà!", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.advice_table_item,parent,false);
        cont = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewAdvice.setText(conseils.get(position).getDescription());
        holder.buttonAction.setOnClickListener(view -> {
            conseils.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return conseils.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewAdvice;
        private CardView buttonAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAdvice = itemView.findViewById(R.id.textViewPrescription);
            buttonAction = itemView.findViewById(R.id.buttonAction);
        }
    }
}
