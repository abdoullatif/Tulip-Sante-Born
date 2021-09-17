package com.example.tulipsante.views.fragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tulipsante.models.uIModels.DashPatientProfileData;
import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.DashPatientProfileViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DashPatientProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String patientAge;
    private String patientGender;
    private List<DashPatientProfileData> dashPatientProfileData;
    private String height;
    private String weight;
    private ArrayList<String> xAxisLabel = new ArrayList<>();
    private List<Entry> lineEntries = new ArrayList<>();

    // Views
    private TextView textViewAge, textViewWeight, textViewHeight;
    private ImageView imageViewHuman;

    private LineChart chart;

    // View Model
    private DashPatientProfileViewModel dashPatientProfileViewModel;

    public DashPatientProfileFragment() {
        // empty constructor
    }

    public DashPatientProfileFragment(String patientAge, String patientGender) {
        this.patientAge = patientAge;
        this.patientGender = patientGender;
    }

    public static DashPatientProfileFragment newInstance(String param1, String param2) {
        DashPatientProfileFragment fragment = new DashPatientProfileFragment("","");
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dash_patient_profile, container, false);
    }

    private void initViews(View view) {
        textViewAge = view.findViewById(R.id.textViewAge);
        textViewHeight = view.findViewById(R.id.textViewHeight);
        textViewWeight = view.findViewById(R.id.textViewWeight);
        chart = view.findViewById(R.id.dashStatisticsBarChart);
        imageViewHuman = view.findViewById(R.id.imageViewHuman);
    }

    private void initialisation() {
        dashPatientProfileViewModel = ViewModelProviders.of(requireActivity())
                .get(DashPatientProfileViewModel.class);
        lineEntries.add(new Entry(0,0));
    }

    LineData data;
    LineDataSet lineDataSet;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setBodyImage();
        setPatientAge();
        setData();

        initChart();
    }

    private void initChart() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.primaryTextColor, typedValue, true);
        @ColorInt int color = typedValue.data;

        lineDataSet = new LineDataSet(lineEntries, "Weight");
        lineDataSet.setLineWidth(1.75f);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setCircleHoleRadius(2.5f);
        lineDataSet.setColor(getContext().getColor(R.color.blue));
        lineDataSet.setCircleColor(getContext().getColor(R.color.blue));
        lineDataSet.setDrawValues(false);
        lineDataSet.setHighLightColor(getContext().getColor(R.color.blue));
        lineDataSet.setValueTextColor(color);
        lineDataSet.setDrawVerticalHighlightIndicator(true);

        Description desc = new Description();
        desc.setText("Weight Variation");
        desc.setTextColor(getContext().getColor(R.color.colorPrimary));

        data = new LineData(lineDataSet);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(color);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setLabelCount(3, true);
        chart.getAxisRight().setLabelCount(3, true);
        chart.getXAxis().setLabelCount(5,true);
        chart.setNoDataTextColor(color);
        chart.setData(data);
        chart.setDescription(desc);
        chart.animateXY(600,600);
    }

    private void setBodyImage() {
        if(patientGender.equals("Male") || patientGender.equals("Homme")) {
            Glide.with(this).load(R.drawable.male_body).into(imageViewHuman);
        }
        else if (patientGender.equals("Female") || patientGender.equals("Femme")) {
            Glide.with(this).load(R.drawable.female_body).into(imageViewHuman);
        }
        else {
            Glide.with(this).load(R.drawable.male_body).into(imageViewHuman);
        }
    }

    private void setData() {
        new Thread(() -> {
            height = dashPatientProfileViewModel.getHeight();
            weight = dashPatientProfileViewModel.getWeight();
            dashPatientProfileData = dashPatientProfileViewModel.getDashPatientProfile();
            getActivity().runOnUiThread(() -> {
                textViewHeight.setText(height);
                textViewWeight.setText(weight);
                if(dashPatientProfileData.size() > 0) {
                    lineDataSet.removeEntry(0);
                }
                for(int i = 0; i < dashPatientProfileData.size(); i++) {
                    xAxisLabel.add(dashPatientProfileData.get(i).getDateConsultation().substring(8,10));
                    lineDataSet.addEntry(new Entry(
                            Float.parseFloat(dashPatientProfileData.get(i).getDateConsultation().substring(8,10)),
                            Float.parseFloat(dashPatientProfileData.get(i).getValeur())
                    ));
                }
                data.notifyDataChanged();
                chart.notifyDataSetChanged();
                chart.invalidate();
            });
        }).start();
    }

    private void setPatientAge() {
        textViewAge.setText(patientAge);
    }


}
