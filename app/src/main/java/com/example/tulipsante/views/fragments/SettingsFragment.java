package com.example.tulipsante.views.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.example.tulipsante.R;


public class SettingsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private ToggleButton darkModeToggle;
    private CardView cardViewLanguage;

    private SharedPreferences sharedPreferences;


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    private void initViews(View view) {
        darkModeToggle = view.findViewById(R.id.darkModeToggle);
        cardViewLanguage = view.findViewById(R.id.cardViewLanguage);
    }

    private void initialisation() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        setToggleButtonStatus();
        onToggleButtonPressed();
        cardViewLanguage.setOnClickListener(v -> {
            final String[] Language = {"ENGLISH", "FRANÇAIS"};
            final int checkedItem;
            if (sharedPreferences.getString("Locale.Helper.Selected.Language", null)
                    .equals("en")) {
                checkedItem = 0;
            } else {
                checkedItem = 1;
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.select_language))
                    .setSingleChoiceItems(Language, checkedItem, (dialog, which) -> {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //if user select prefered language as English then
                        if (Language[which].equals("ENGLISH")) {
                            editor.putString("Locale.Helper.Selected.Language","en");
                            editor.apply();
                        }
                        //if user select prefered language as Hindi then
                        if (Language[which].equals("FRANÇAIS")) {
                            editor.putString("Locale.Helper.Selected.Language","fr");
                            editor.apply();
                        }
                    })
                    .setPositiveButton("OK", (dialog, which) -> {
                        getActivity().recreate();
                        dialog.dismiss();
                    });
            builder.create().show();
        });
    }

    private void setToggleButtonStatus() {
        darkModeToggle.setChecked(sharedPreferences.getString("current_Theme", null).equals("dark"));
    }

    private void onToggleButtonPressed() {
        darkModeToggle.setOnCheckedChangeListener((compoundButton, b) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if(b) {
                editor.putString("current_Theme","dark");
                editor.apply();
                getActivity().setTheme(R.style.AppTheme);
            }
            else {
                editor.putString("current_Theme","light");
                editor.apply();
                getActivity().setTheme(R.style.LightTheme);
            }
            getActivity().recreate();
        });
    }
}
