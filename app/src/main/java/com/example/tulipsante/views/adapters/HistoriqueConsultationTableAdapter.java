package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.models.Patient;
import com.example.tulipsante.models.uIModels.HistoriqueTacheMedecin;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.HistoriqueTacheViewModel;
import com.example.tulipsante.views.activities.ConsultationActivity;

import java.util.ArrayList;
import java.util.List;

public class HistoriqueConsultationTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<HistoriqueTacheMedecin> historiqueTacheMedecinList = new ArrayList<>();
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;
    private Context cont;


    public HistoriqueConsultationTableAdapter() {
    }

    public void setHistoriqueTacheMedecinList(List<HistoriqueTacheMedecin> historiqueTacheMedecinList) {
        this.historiqueTacheMedecinList = historiqueTacheMedecinList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        cont = parent.getContext();


        switch (viewType) {
            case ITEM:
                View viewItem = inflater
                        .inflate(R.layout.historique_table_list_item, parent, false);
                viewHolder = new HistoriqueViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater
                        .inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                HistoriqueViewHolder historiqueViewHolder = (HistoriqueViewHolder) holder;
                if(historiqueTacheMedecinList.get(position).getPrenomPatient() != null) {
                    historiqueViewHolder.buttonAction.setVisibility(View.VISIBLE);
                    String fullName = historiqueTacheMedecinList.get(position).getNomPatient()
                            + " " + historiqueTacheMedecinList.get(position).getPrenomPatient();
                    historiqueViewHolder.textViewDate.setText(historiqueTacheMedecinList
                            .get(position).getDateConsultation());
                    historiqueViewHolder.textViewDoc.setText(fullName);
                    historiqueViewHolder.textViewDescription.setText(historiqueTacheMedecinList
                            .get(position).getDescription());
                    historiqueViewHolder.buttonAction.setOnClickListener(view -> {
                        Intent intent = new Intent(cont, ConsultationActivity.class);
                        intent.putExtra("From","Consultation History");
                        intent.putExtra("consultationId", historiqueTacheMedecinList
                                .get(position).getIdConsultation());
                        Patient patient = new Patient(
                                historiqueTacheMedecinList.get(position).getIdPatient(),
                                historiqueTacheMedecinList.get(position).getNomPatient(),
                                historiqueTacheMedecinList.get(position).getPrenomPatient(),
                                historiqueTacheMedecinList.get(position).getGenrePatient(),
                                historiqueTacheMedecinList.get(position).getDateNaissancePatient(),
                                historiqueTacheMedecinList.get(position).getGroupeSanguinPatient(),
                                historiqueTacheMedecinList.get(position).getPhotoPatient(),
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                        );
                        intent.putExtra("Patient", patient);
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
        return historiqueTacheMedecinList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == historiqueTacheMedecinList.size() - 1 && isLoadingAdded)
                ? LOADING
                : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;

        add(new HistoriqueTacheMedecin());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = historiqueTacheMedecinList.size() - 1;
        HistoriqueTacheMedecin result = getItem(position);

        if (result != null) {
            historiqueTacheMedecinList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(HistoriqueTacheMedecin hPM) {
        historiqueTacheMedecinList.add(hPM);
        notifyDataSetChanged();
    }

    public void clear() {
        historiqueTacheMedecinList.clear();
        notifyDataSetChanged();
    }

    public HistoriqueTacheMedecin getItem(int position) {
        return historiqueTacheMedecinList.get(position);
    }

    public class HistoriqueViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate, textViewDoc, textViewDescription;
        private CardView buttonAction;
        public HistoriqueViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDoc = itemView.findViewById(R.id.textViewDoc);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            buttonAction = itemView.findViewById(R.id.buttonAction);
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
