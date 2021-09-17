package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tulipsante.models.CategorieSigneVitaux;
import com.example.tulipsante.R;

import java.util.List;

public class VitalsSpinnerAdapter extends ArrayAdapter<CategorieSigneVitaux> {

    LayoutInflater flater;

    public VitalsSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, List list) {
        super(context, resource, textViewResourceId, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return rowView(convertView, position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return rowView(convertView,position);
    }

    private View rowView(View convertView, int postion) {
        CategorieSigneVitaux categorieSigneVitaux = getItem(postion);

        ViewHolder viewHolder;
        View rowView = convertView;
        if(rowView == null) {
            viewHolder = new ViewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = flater.inflate(R.layout.spinner_region_list_item, null, false);

            viewHolder.textViewSpinnerListItem = rowView.findViewById(R.id.textViewRegionListItem);
            rowView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        viewHolder.textViewSpinnerListItem.setText(categorieSigneVitaux.getDescription());

        return rowView;
    }

    private static class ViewHolder {
        private TextView textViewSpinnerListItem;
    }
}
