package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tulipsante.views.activities.LoginActivity;
import com.example.tulipsante.models.uIModels.SliderListModel;
import com.example.tulipsante.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder>{
    private Context context;
    private ArrayList<SliderListModel> sliderListModelArrayList;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.slider_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide
                .with(context)
                .load(context
                                .getResources()
                                .getIdentifier(sliderListModelArrayList
                                        .get(position)
                                        .getImage_url(),
                                        "drawable",
                                        context.getPackageName())
                        )
                .into(holder.myimage);
        holder.myimage.setOnClickListener(view -> {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sliderListModelArrayList.size();
    }

    public void setSliderListModel(ArrayList<SliderListModel> sliderListModelArrayList) {
        this.sliderListModelArrayList = sliderListModelArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView myimage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myimage = itemView.findViewById(R.id.myimage);

        }
    }
}
