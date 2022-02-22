package com.example.tulipsante.views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tulipsante.R;
import com.example.tulipsante.views.activities.ConsultationToolsActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DashboardSAFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String myFormat = "E, dd MMM yyyy";

    // Views
    private CardView cardViewEmergency;
    private TextView textViewTime, textViewDay;

    private CountDownTimer newtimer;

    public DashboardSAFragment() {
        // Required empty public constructor
    }

    public static DashboardSAFragment newInstance(String param1, String param2) {
        DashboardSAFragment fragment = new DashboardSAFragment();
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
        return inflater.inflate(R.layout.fragment_dashboard_s_a, container, false);
    }

    private void initViews(View view){
        cardViewEmergency = view.findViewById(R.id.cardViewEmergency);
        textViewTime = view.findViewById(R.id.textViewTime);
        textViewDay = view.findViewById(R.id.textViewDay);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        onEmergencyButtonPressed();
        setDateAndTime();
    }

    private void setDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        textViewDay.setText(sdf.format(calendar.getTime()));
        newtimer = new CountDownTimer(1000000000, 1000) {
            public void onTick(long millisUntilFinished) {
                Calendar c = Calendar.getInstance();
                textViewTime.setText(c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND));
            }
            public void onFinish() {
            }
        };
        newtimer.start();
    }

    private void onEmergencyButtonPressed() {
        cardViewEmergency.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ConsultationToolsActivity.class);
            intent.putExtra("From", "Dashboard");
            startActivity(intent);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        newtimer.cancel();
    }
}