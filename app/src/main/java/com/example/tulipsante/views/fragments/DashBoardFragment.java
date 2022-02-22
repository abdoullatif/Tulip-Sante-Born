package com.example.tulipsante.views.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tulipsante.models.Medecin;
import com.example.tulipsante.models.Nouvelles;
import com.example.tulipsante.models.uIModels.DashProfileData;
import com.example.tulipsante.viewModel.DashBoardViewModel;
import com.example.tulipsante.views.activities.AddPatientActivity;
import com.example.tulipsante.interfaces.DashBoardListener;
import com.example.tulipsante.R;
import com.example.tulipsante.views.activities.ConferencingActivity;
import com.example.tulipsante.views.activities.ConsultationToolsActivity;
import com.example.tulipsante.views.activities.ReferenceActivity;
import com.example.tulipsante.views.adapters.TodayRecyclerAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashBoardFragment extends Fragment  {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String numberPatients;
    private  @ColorInt int color;

    // Views
    private ImageView profileImage;
    private TextView viewAllPatients, textViewName, textViewDocDash;
    private TextView viewHistory;
    private CardView cardViewHistory, cardViewPatient;
    private TextView viewAllProfile;
    private TextView createNewPatient, textViewPatientNumber;
    private CardView cardViewEmergency,cardViewConferencing, cardViewReference;

    private List<String> xAxisLabel = new ArrayList<>();
    private List<BarEntry> barEntries = new ArrayList<>();
    private BarDataSet barDataSet1;
    private List<DashProfileData> dashData;
    private BarData data;
    private BarChart chart;
    private XAxis xAxis;

    // Listener
    private DashBoardListener listener;

    // View Model
    private DashBoardViewModel dashBoardViewModel;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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
        return inflater.inflate(R.layout.fragment_dash_board, container, false);
    }

    private void initViews(View view) {
        viewAllPatients = view.findViewById(R.id.viewAllPatient);
        viewHistory = view.findViewById(R.id.viewAllMessage);
        viewAllProfile = view.findViewById(R.id.viewAllProfile);
        createNewPatient = view.findViewById(R.id.createNewPatient);
        chart = view.findViewById(R.id.dashStatisticsBarChart);
        textViewDocDash = view.findViewById(R.id.textViewDocName);
        textViewName = view.findViewById(R.id.textViewName);
        profileImage = view.findViewById(R.id.profileImageDocDash);
        textViewPatientNumber = view.findViewById(R.id.textViewNumberPatients);
        cardViewEmergency = view.findViewById(R.id.cardViewEmergency);
        cardViewHistory = view.findViewById(R.id.cardViewHistory);
        cardViewPatient = view.findViewById(R.id.cardViewPatient);
        cardViewConferencing = view.findViewById(R.id.cardViewConferencing);
        cardViewReference = view.findViewById(R.id.cardViewReference);
    }

    private void initialisation() {
        listener = (DashBoardListener) getContext();
        dashBoardViewModel = ViewModelProviders.of(this).get(DashBoardViewModel.class);
        // Adapter
        TodayRecyclerAdapter todayRecyclerAdapter = new TodayRecyclerAdapter();
        List<Nouvelles> data = dashBoardViewModel.getNouvelles();
        todayRecyclerAdapter.setNewsItems(data);
        barEntries.add(new BarEntry(1,0));
        Medecin medecin = dashBoardViewModel.getMedecin();
        if (medecin != null) {
            setProfileWidget(medecin);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        onViewAllPatientPressed();
        onViewAllMessagePressed();
        onViewAllProfilePressed();
        onCreateNewPatient();
        onChartDataReceived();
        onEmergencyButtonPressed();
        onConferencingButtonPressed();
        onReferenceButtonPressed();
        initChart();
    }

    private void initChart() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = Objects.requireNonNull(getContext()).getTheme();
        theme.resolveAttribute(R.attr.primaryTextColor, typedValue, true);
        color = typedValue.data;

        barDataSet1 = new BarDataSet(barEntries, "Consultations");
        barDataSet1.setColor(getContext().getColor(R.color.blue));
        barDataSet1.setBarBorderWidth(0);
        barDataSet1.setValueTextColor(color);

        Description desc = new Description();
        desc.setText("Number of consultation for the period (" + dashBoardViewModel.getDate() + ")");
        desc.setTextColor(color);

        data = new BarData(barDataSet1);

        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setLabelCount(3, true);
        chart.getAxisRight().setLabelCount(3, true);
        chart.getXAxis().setLabelCount(5,true);
        chart.setNoDataTextColor(color);
        chart.setData(data);
        chart.setNoDataTextColor(color);
        chart.setDescription(desc);
        chart.animateXY(600,600);
        chart.invalidate();
    }

    private void onChartDataReceived() {
        new Thread(() -> {
            numberPatients = dashBoardViewModel.numberOfPatients();
            dashData = new ArrayList<>(dashBoardViewModel.getConsultationGraph());
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                textViewPatientNumber.setText(numberPatients);
                if(dashData.size() > 0) {
                    barDataSet1.removeEntry(0);
                }
                for(int i = 0; i < dashData.size(); i++) {
                    xAxisLabel.add(dashData.get(i).getDateConsultation().substring(8,10));
                    barDataSet1.addEntry(new BarEntry(
                            Float.parseFloat(dashData.get(i).getDateConsultation().substring(8,10)),
                            Float.parseFloat(dashData.get(i).getNumPatient())
                    ));
                }
                xAxis = chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setTextColor(color);
                xAxis.setEnabled(true);
                data.notifyDataChanged();
                chart.notifyDataSetChanged();
                chart.invalidate();
            });
        }).start();
    }

    private void onConferencingButtonPressed() {
        cardViewConferencing.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ConferencingActivity.class);
            startActivity(intent);
        });
    }

    private void onReferenceButtonPressed() {
        cardViewReference.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ReferenceActivity.class);
            startActivity(intent);
        });
    }

    private void onEmergencyButtonPressed() {
        cardViewEmergency.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ConsultationToolsActivity.class);
            intent.putExtra("From", "Dashboard");
            startActivity(intent);
        });
    }

    private void setProfileWidget(Medecin medecin) {
        File myPath =
                new File(
                        Environment
                                .getExternalStorageDirectory()+
                                File.separator+
                                "Tulip_sante/Medecin/"+medecin.getPhoto()
                );
        System.out.println(myPath);
        if(myPath.exists()) {
            Glide.with(this).load(myPath).into(profileImage);
        }
        String topName = getText(R.string.hello) + " " +medecin.getPrenomMedecin();
        String name = medecin.getNomMedecin() + " " + medecin.getPrenomMedecin();
        textViewName.setText(topName);
        textViewDocDash.setText(name);
    }

    private void onCreateNewPatient() {
        createNewPatient.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddPatientActivity.class);
            startActivity(intent);
        });
    }

    private void onViewAllProfilePressed() {
        profileImage.setOnClickListener(view -> listener.onViewAllProfilePressed());
        viewAllProfile.setOnClickListener(view -> listener.onViewAllProfilePressed());
    }

    private void onViewAllMessagePressed() {
        cardViewHistory.setOnClickListener(view -> listener.onViewAllMessagePressed());
        viewHistory.setOnClickListener(view -> listener.onViewAllMessagePressed());
    }

    private void onViewAllPatientPressed() {
        viewAllPatients.setOnClickListener(view -> listener.onViewAllPatientPressed());
        cardViewPatient.setOnClickListener(view -> listener.onViewAllPatientPressed());
    }
}
