package com.example.tulipsante.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tulipsante.models.Vice;
import com.example.tulipsante.R;

import java.util.ArrayList;
import java.util.List;

public class ViceListViewAdapter extends ArrayAdapter {

    LayoutInflater flater;
    public List<Vice> selectedVice= new ArrayList<>();

    public ViceListViewAdapter(@NonNull Context context, int resource, int textViewResourceId, List list) {
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
        Object obj = getItem(postion);
        Vice vice = (Vice) obj;

        ViewHolder viewHolder;
        View rowView = convertView;
        if(rowView == null) {
            viewHolder = new ViewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = flater.inflate(R.layout.checkbox_list_view_entry, null, false);

            viewHolder.textViewEntry = rowView.findViewById(R.id.textViewEntry);
            viewHolder.checkBox = rowView.findViewById(R.id.checkbox);
            rowView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    selectedVice.add(vice);
                }
                else {
                    selectedVice.remove(vice);
                }
                System.out.println(selectedVice.size());
            }
        });
        viewHolder.textViewEntry.setText(vice.getDescription());

        return rowView;
    }

    private static class ViewHolder {
        private TextView textViewEntry;
        private CheckBox checkBox;
    }
}

