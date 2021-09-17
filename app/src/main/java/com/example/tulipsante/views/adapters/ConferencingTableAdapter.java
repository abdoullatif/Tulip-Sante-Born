package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.interfaces.OnDeleteClicked;
import com.example.tulipsante.models.ConferenceContact;
import com.example.tulipsante.R;
import com.example.tulipsante.views.activities.ConferencingActivity;

import java.util.ArrayList;
import java.util.List;

public class ConferencingTableAdapter extends RecyclerView.Adapter<ConferencingTableAdapter.ViewHolder> {
    private Context context;
    private List<ConferenceContact> confContactList = new ArrayList<>();
    private Context cont;

    private OnDeleteClicked listener;

    public ConferencingTableAdapter(Context context) {
        this.context = context;
    }

    public void setConfContactList(List<ConferenceContact> confContactList) {
        this.confContactList = confContactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conferencing_list_item, parent, false);
        cont = parent.getContext();
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        listener = (OnDeleteClicked) cont;
        holder.title.setText(confContactList.get(position).getNomComplet());
        holder.subTitle.setText(confContactList.get(position).getDescription());
        holder.buttonCall.setOnClickListener(view -> {
            ConferencingActivity.skype(confContactList.get(position).getDescription(), context);
        });
        holder.buttonDelete.setOnClickListener(view -> {
            listener.onClick(confContactList.get(position).getIdConferenceContact());
        });

    }

    @Override
    public int getItemCount() {
        return confContactList.size();
    }

    public void clear() {
        confContactList.clear();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, subTitle;
        private CardView buttonCall, buttonDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            subTitle = itemView.findViewById(R.id.textViewSubTitle);
            buttonCall = itemView.findViewById(R.id.buttonCall);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

        }
    }
}
