package com.example.tulipsante.pulse.oximeter.util.view.adapter.recycleview;

import android.view.View;
import android.view.ViewGroup;

public interface OnItemClickListener<T> {
    void onItemClick(ViewGroup parent, View view, T t, int position);

    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}