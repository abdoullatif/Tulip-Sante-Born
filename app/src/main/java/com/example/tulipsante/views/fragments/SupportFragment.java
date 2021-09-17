package com.example.tulipsante.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.tulipsante.R;
import com.example.tulipsante.models.Parametre;
import com.example.tulipsante.viewModel.SupportFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class SupportFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private List<Parametre> parametreList = new ArrayList<>();

    // U I
    private WebView webView;

    public SupportFragment() {
        // Required empty public constructor
    }

    public static SupportFragment newInstance(String param1, String param2) {
        SupportFragment fragment = new SupportFragment();
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
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    private void initViews(View view) {
        webView = view.findViewById(R.id.webViewSupport);
    }

    private void initialisation() {
        // View Model
        SupportFragmentViewModel supportFragmentViewModel = ViewModelProviders
                .of(this)
                .get(SupportFragmentViewModel.class);
        supportFragmentViewModel
                .getParametreList()
                .observe(this, parametres -> parametreList.addAll(parametres));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setSupportPage();
    }

    private void setSupportPage() {
        new Handler().postDelayed(() -> {
            webView.loadUrl(parametreList.get(0).getWebSiteName().substring(0,12));
        },2000);
    }
}
