package com.example.tulipsante.pulse.oximeter.view.frg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tulipsante.pulse.oximeter.BaseFrg;
import com.example.tulipsante.pulse.oximeter.Config;
import com.example.tulipsante.pulse.oximeter.repository.DbMgr;
import com.example.tulipsante.pulse.oximeter.repository.bean.SpoRecord;
import com.example.tulipsante.pulse.oximeter.util.view.adapter.ViewHolder;
import com.example.tulipsante.pulse.oximeter.util.view.adapter.recycleview.CommonAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.tulipsante.R;
import com.example.tulipsante.pulse.oximeter.util.view.adapter.recycleview.HorizontalDividerItemDecoration;

public class RecordListFragment extends BaseFrg {

    private RecyclerView mRecycler;
    private CommonAdapter<SpoRecord> mAdapter;
    private DbMgr mDbMgr;

    @Override
    protected View getLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_record_list, container, false);
    }

    @Override
    protected void initView(View container) {
        mRecycler = container.findViewById(R.id.recycler);
        mRecycler.addItemDecoration(new HorizontalDividerItemDecoration());
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mDbMgr = DbMgr.getInstance(getContext());
        List<SpoRecord> records = mDbMgr.getSpoRecords();
        mAdapter = new CommonAdapter<SpoRecord>(getContext(), R.layout.list_item_record, records) {
            @Override
            public void convert(ViewHolder holder, SpoRecord spoRecord) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                holder.setText(R.id.txt_time, sdf.format(new Date(spoRecord.getTime())));
                holder.setText(R.id.txt_spo2, String.valueOf(spoRecord.getSpo2()));
                holder.setText(R.id.txt_pr, String.valueOf(spoRecord.getPr()));
                holder.setText(R.id.txt_pi, String.valueOf(spoRecord.getPi()));
            }
        };
        mRecycler.setAdapter(mAdapter);
        registerBroadcast();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegisterBroadcast();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<SpoRecord> records = mDbMgr.getSpoRecords();
            mAdapter.notifyData(records);
        }
    };

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.RECEIVE_NEW_SPO_STATS);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
    }

    private void unRegisterBroadcast() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }
}
