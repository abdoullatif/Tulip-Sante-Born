package com.example.tulipsante.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.models.Patient;
import com.example.tulipsante.models.uIModels.ConsultationUMedecin;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.PdfGenerator;
import com.example.tulipsante.views.activities.ConsultationActivity;
import com.example.tulipsante.views.dialogFragment.MailPdfDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ConsultationHistoryTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ConsultationUMedecin> consultationList = new ArrayList<>();
    private List<String> basicData;
    private Context cont;
    private Activity activity;

    FragmentManager fm;

    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;


    public void setConsultationList(List<ConsultationUMedecin> consultationList) {
        this.consultationList = consultationList;

    }

    public List<ConsultationUMedecin> getConsultationList() {
        return consultationList;
    }

    public ConsultationHistoryTableAdapter(List<String> basicData, FragmentManager fm, Activity activity) {
        this.basicData = basicData;
        this.fm = fm;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        cont = parent.getContext();

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.consultation_list_item, parent, false);
                viewHolder = new ViewHolder(viewItem);
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
                ViewHolder holder1 = (ViewHolder) holder;
                if(consultationList.get(position).getPrenomMedecin() != null) {
                    holder1.textViewId.setText(consultationList.get(position).getIdConsultation());
                    holder1.textViewDoc.setText(consultationList.get(position).getNomMedecin() + " " + consultationList.get(position).getPrenomMedecin());
                    holder1.textViewDate.setText(consultationList.get(position).getDateConsultation());
                    holder1.buttonAction.setOnClickListener(view -> {
                        Intent intent = new Intent(cont, ConsultationActivity.class);
                        intent.putExtra("From","Consultation History");
                        intent.putExtra("consultationId", consultationList.get(position).getIdConsultation());
                        Patient patient = new Patient(
                                basicData.get(1),
                                basicData.get(3),
                                basicData.get(2),
                                basicData.get(4),
                                basicData.get(6),
                                basicData.get(7),
                                basicData.get(0),
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
    public int getItemViewType(int position) {
        System.out.println((position == consultationList.size() - 1 && isLoadingAdded));
        return (position == consultationList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ConsultationUMedecin());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = consultationList.size() - 1;
        ConsultationUMedecin result = getItem(position);
        if (result != null) {
            consultationList.remove(position);
        }
    }

    public void add(ConsultationUMedecin consultationUMedecin) {
        consultationList.add(consultationUMedecin);
    }

    public void clear() {
        consultationList.clear();
        notifyDataSetChanged();
    }

    public ConsultationUMedecin getItem(int position) {
        return consultationList.get(position);
    }


    @Override
    public int getItemCount() {
        return consultationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewId;
        private TextView textViewDoc;
        private TextView textViewDate;
        private CardView buttonAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewDoc = itemView.findViewById(R.id.textViewDoc);
            textViewDate = itemView.findViewById(R.id.textViewDate);
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
