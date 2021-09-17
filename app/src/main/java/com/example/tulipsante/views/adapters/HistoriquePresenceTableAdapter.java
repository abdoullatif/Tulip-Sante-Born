package com.example.tulipsante.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.models.uIModels.HistoriquePresenceMedecin;
import com.example.tulipsante.R;

import java.util.ArrayList;
import java.util.List;

public class HistoriquePresenceTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<HistoriquePresenceMedecin> historiquePresenceMedecinList = new ArrayList<>();
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    public HistoriquePresenceTableAdapter() {
    }

    public void setHistoriquePresenceMedecinList(List<HistoriquePresenceMedecin> historiquePresenceMedecinList) {
        this.historiquePresenceMedecinList = historiquePresenceMedecinList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.historique_table_list_item, parent, false);
                viewHolder = new HistoriqueViewHolder(viewItem);
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
                HistoriqueViewHolder historiqueViewHolder = (HistoriqueViewHolder) holder;
                if(historiquePresenceMedecinList.get(position).getPrenomMedecin() != null) {
                    String fullName = historiquePresenceMedecinList.get(position).getNomMedecin() + " " + historiquePresenceMedecinList.get(position).getPrenomMedecin();
                    historiqueViewHolder.textViewDate.setText(historiquePresenceMedecinList.get(position).getDateHistorique());
                    historiqueViewHolder.textViewDoc.setText(fullName);
                    historiqueViewHolder.textViewDescription.setText(historiquePresenceMedecinList.get(position).getDescription());
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
        return historiquePresenceMedecinList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == historiquePresenceMedecinList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;

        add(new HistoriquePresenceMedecin());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = historiquePresenceMedecinList.size() - 1;
        HistoriquePresenceMedecin result = getItem(position);

        if (result != null) {
            historiquePresenceMedecinList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(HistoriquePresenceMedecin hPM) {
        historiquePresenceMedecinList.add(hPM);
        notifyDataSetChanged();
    }

    public void clear() {
        historiquePresenceMedecinList.clear();
        notifyDataSetChanged();
    }

    public HistoriquePresenceMedecin getItem(int position) {
        return historiquePresenceMedecinList.get(position);
    }

    public class HistoriqueViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate, textViewDoc, textViewDescription;
        public HistoriqueViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDoc = itemView.findViewById(R.id.textViewDoc);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);

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
