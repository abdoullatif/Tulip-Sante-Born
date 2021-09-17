package com.example.tulipsante.pulse.oximeter.view.frg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.example.tulipsante.R;

import com.example.tulipsante.pulse.oximeter.BaseFrg;
import com.example.tulipsante.pulse.oximeter.Config;
import com.example.tulipsante.pulse.oximeter.repository.DbMgr;

import com.example.tulipsante.pulse.oximeter.repository.bean.SpoRecord;
import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecordChartFragment extends BaseFrg {
    private LineChart mChartSpo2, mChartPr, mChartPi;
    private DbMgr mDbMgr;

    @Override
    protected View getLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_record_chart, container, false);
    }

    @Override
    protected void initView(View container) {
        mDbMgr = DbMgr.getInstance(getContext());
        List<SpoRecord> records = mDbMgr.getSpoRecords();
        mChartSpo2 = container.findViewById(R.id.lineChat_spo2);
        mChartPr = container.findViewById(R.id.lineChat_pr);
        mChartPi = container.findViewById(R.id.lineChat_pi);

        initChart(mChartSpo2, Config.cSpo2Max, records);
        initChart(mChartPr, Config.cPrMax, records);
        initChart(mChartPi, Config.cPiMax, records);
        registerBroadcast();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegisterBroadcast();
    }

    private void initChart(LineChart chart, float yMax, List<SpoRecord> records) {
        int recordsLen = records.size();
        //后台绘制
        chart.setDrawGridBackground(false);
        //设置描述文本
        chart.getDescription().setEnabled(false);
        //设置支持触摸交互
        chart.setTouchEnabled(true);
        //设置图表的拖动
        chart.setDragEnabled(true);
        //设置缩放
        chart.setScaleXEnabled(false);
        chart.setScaleYEnabled(false);
        //如果设置为true，则同时缩放xy轴。如果禁用，则可以单独缩放x轴和y轴。
        chart.setPinchZoom(false);
        //设置在曲线中显示的最大数据量
        chart.setVisibleXRangeMaximum(6);
        //设置标签的位置
        chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        chart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //设置图表距离底部的距离
        chart.setExtraBottomOffset(20);
        //设置移动到曲线的x坐标
        // chart.moveViewToX(recordsLen - 1);
        // 设置一页最大显示个数为10，超出部分就滑动
        float ratio = (float) recordsLen / (float) 6;
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        chart.zoom(ratio, 1f, 0, 0);

        //x轴
        XAxis xAxis = chart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置x轴的最大值
        xAxis.setAxisMaximum(recordsLen);
        //设置x轴的最小值
        xAxis.setAxisMinimum(0f);
        //设置x轴在底部显示
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴文字顺时针旋转角度
        // xAxis.setLabelRotationAngle(-30);
        //设置x轴的坐标名称
     /*  xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value < recordsLen && value >= 0) {
                    long time = records.get((int) value).getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd\rHH:mm");
                    return sdf.format(time);
                }
                return "";
            }
        });

      */



        //y轴
        YAxis yAxis = chart.getAxisLeft();
        //重置所有限制线,以避免重叠线
        yAxis.removeAllLimitLines();
        //y轴最大
        yAxis.setAxisMaximum(yMax);
        //y轴最小
        yAxis.setAxisMinimum(0f);
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setDrawZeroLine(false);
        // 限制数据(而不是背后的线条勾勒出了上面)
        yAxis.setDrawLimitLinesBehindData(true);
        chart.getAxisRight().setEnabled(false);

        //设置数据
        setData(chart, records);
    }

    //传递数据集
    private void setData(LineChart chart, List<SpoRecord> records) {
        String title = "";
        ArrayList<Entry> values = new ArrayList<Entry>();
        int id = chart.getId();
        if (id == R.id.lineChat_spo2) {
            title = "Spo2";
            for (int i = 0; i < records.size(); i++) {
                SpoRecord record = records.get(i);
                values.add(new Entry(i, record.getSpo2()));
            }
        } else if (id == R.id.lineChat_pr) {
            title = "PR";
            for (int i = 0; i < records.size(); i++) {
                SpoRecord record = records.get(i);
                values.add(new Entry(i, record.getPr()));
            }
        } else if (id == R.id.lineChat_pi) {
            title = "PI";
            for (int i = 0; i < records.size(); i++) {
                SpoRecord record = records.get(i);
                values.add(new Entry(i, record.getPi()));
            }
        }

        LineDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // 创建一个数据集,并给它一个类型
            set1 = new LineDataSet(values, title);

            // 在这里设置线
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            if (id == R.id.lineChat_spo2) {
                set1.setColor(ContextCompat.getColor(getContext(), R.color.spo));
                set1.setCircleColor(ContextCompat.getColor(getContext(), R.color.spo));
            } else if (id == R.id.lineChat_pr) {
                set1.setColor(ContextCompat.getColor(getContext(), R.color.pr));
                set1.setCircleColor(ContextCompat.getColor(getContext(), R.color.pr));
            } else if (id == R.id.lineChat_pi) {
                set1.setColor(ContextCompat.getColor(getContext(), R.color.pi));
                set1.setCircleColor(ContextCompat.getColor(getContext(), R.color.pi));
            }
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // 填充背景只支持18以上
            if (Utils.getSDKInt() >= 18) {
                Drawable drawable;
                if (id == R.id.lineChat_spo2) {
                    drawable = ContextCompat.getDrawable(getContext(), R.drawable.chart_bg_spo);
                } else if (id == R.id.lineChat_pr) {
                    drawable = ContextCompat.getDrawable(getContext(), R.drawable.chart_bg_pr);
                } else {
                    drawable = ContextCompat.getDrawable(getContext(), R.drawable.chart_bg_pi);
                }
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            //添加数据集
            dataSets.add(set1);

            //创建一个数据集的数据对象
            LineData data = new LineData(dataSets);

            //谁知数据
            chart.setData(data);
            //默认动画
            // chart.animateX(2500);
            //刷新
            //chart.invalidate();
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<SpoRecord> records = mDbMgr.getSpoRecords();
            setData(mChartSpo2, records);
            setData(mChartPr, records);
            setData(mChartPi, records);
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
