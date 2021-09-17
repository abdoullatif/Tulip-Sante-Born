package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tulipsante.models.Allergie;
import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.R;
import com.example.tulipsante.models.Symptome;
import com.example.tulipsante.models.Vice;

import java.util.List;

public class AllergiesListViewAdapter extends ArrayAdapter<Allergie> {

    LayoutInflater flater;

    public AllergiesListViewAdapter(@NonNull Context context, int resource, int textViewResourceId, List list) {
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
        Allergie allergie = getItem(postion);

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
        viewHolder.textViewSpinnerListItem.setText(allergie.getDescription());

        return rowView;
    }

    private static class ViewHolder {
        private TextView textViewSpinnerListItem;
    }
}