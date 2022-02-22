package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.interfaces.OnDeleteClicked;
import com.example.tulipsante.models.PermissionXMedecin;
import com.example.tulipsante.R;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.views.dialogFragment.ImageDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class PermissionManagementTableAdapter extends
        RecyclerView.Adapter<PermissionManagementTableAdapter.Viewholder>  {
    private Context cont;
    private List<PermissionXMedecin> permissionList = new ArrayList<>();
    private OnDeleteClicked listener;
    private FragmentManager fm;
    private SharedPreferences sharedPreferences;

    public PermissionManagementTableAdapter(Context context, FragmentManager fm) {
        listener = (OnDeleteClicked) context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.fm = fm;
    }

    public void setPermissionList(List<PermissionXMedecin> permissionList) {
        this.permissionList = permissionList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.permission_management_table_item,parent,false);
        cont = parent.getContext();
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        String fullName = permissionList.get(position).getNomMedecin()
                + " " + permissionList.get(position).getPrenomMedecin();
        holder.textViewNo.setText(String.valueOf(position + 1));
        holder.textViewReqOn.setText(permissionList.get(position).getDateDemande());
        holder.textViewAccessFor.setText(fullName);
        holder.textViewExpiration.setText(permissionList.get(position).getDateExpiration());
        holder.textViewSignature.setText(permissionList.get(position).getChemin());
        holder.textViewSignature.setOnClickListener(view -> {
            ImageDialogFragment fragment = new ImageDialogFragment(
                    cont,
                    sharedPreferences.getString("IDPATIENT",null),
                    "Personal",
                    permissionList.get(position).getChemin());
            fragment.show(fm,"ModalBottomSheet");
        });
        if(GeneralPurposeFunctions.hasExpired(permissionList.get(position).getDateExpiration())
                || permissionList.get(position).getType().equals("private")) {
            holder.cardViewValid.setVisibility(View.INVISIBLE);
            holder.cardViewInvalid.setVisibility(View.VISIBLE);
            holder.cardViewDelete.setVisibility(View.INVISIBLE);
        }else {
            holder.cardViewValid.setVisibility(View.VISIBLE);
            holder.cardViewInvalid.setVisibility(View.INVISIBLE);
            holder.cardViewDelete.setVisibility(View.VISIBLE);
            holder.cardViewDelete.setOnClickListener(view -> {
                listener.onClick(permissionList.get(position).getIdPermission());
            });
        }
    }

    @Override
    public int getItemCount() {
        return permissionList.size();
    }

    public void clear() {
        permissionList.clear();
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private TextView textViewNo, textViewReqOn, textViewAccessFor, textViewExpiration, textViewSignature;
        private CardView cardViewDelete, cardViewValid, cardViewInvalid;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            textViewNo = itemView.findViewById(R.id.textViewNo);
            textViewAccessFor = itemView.findViewById(R.id.textViewAccessFor);
            textViewReqOn = itemView.findViewById(R.id.textViewRequestedOn);
            textViewExpiration = itemView.findViewById(R.id.textViewExpiration);
            cardViewDelete = itemView.findViewById(R.id.cardViewDelete);
            cardViewValid = itemView.findViewById(R.id.cardViewValid);
            cardViewInvalid = itemView.findViewById(R.id.cardViewInvalid);
            textViewSignature = itemView.findViewById(R.id.textViewSignature);
        }

    }
}
