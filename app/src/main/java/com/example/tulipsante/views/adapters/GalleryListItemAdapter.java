package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tulipsante.interfaces.RecyclerImageClick;
import com.example.tulipsante.R;

import java.io.File;
import java.util.ArrayList;

public class GalleryListItemAdapter extends RecyclerView.Adapter<GalleryListItemAdapter.ViewHolder> {

    private Context cont;
    private ArrayList<File> imagePath = new ArrayList<>();
    private RecyclerImageClick listener;


    public GalleryListItemAdapter(RecyclerImageClick listener) {
        this.listener = listener;
    }

    public void setImagePath(ArrayList<File> imagePath) {
        this.imagePath = imagePath;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.gallery_list_item,parent,false);
        cont = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(cont != null) {
            Glide.with(cont).load(imagePath.get(position).getAbsoluteFile()).into(holder.imageView);
        }
        holder.imageView.setOnClickListener(view -> {
            // data
            listener.onCenterImageChange(imagePath.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return imagePath.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewGallery);
        }
    }
}
