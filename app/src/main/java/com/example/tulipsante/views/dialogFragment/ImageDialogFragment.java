package com.example.tulipsante.views.dialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tulipsante.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;

public class ImageDialogFragment extends BottomSheetDialogFragment {
    private CardView cancel;
    private TextView textView;
    private ImageView imageView;

    private String patientId;
    private String imagePath;
    private Context context;
    private String type;

    public ImageDialogFragment(Context context, String patientId, String type, String imagePath) {
        this.context = context;
        this.patientId = patientId;
        this.type = type;
        this.imagePath = imagePath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_dialog, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        View bottomSheet = dialog.findViewById(R.id.bottom_sheet);
        bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        final View view = getView();
        view.post(() -> {
            View parent = (View) view.getParent();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight(),true);
        });
    }

    private void initViews(View view) {
        cancel = view.findViewById(R.id.cancelDialog);
        textView = view.findViewById(R.id.textView);
        imageView = view.findViewById(R.id.imageView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        onDialogCancel();
        setImageView();
        setTextView();
    }

    private void setTextView() {
        textView.setText(imagePath);
    }

    private void setImageView() {
        File myPath = new File(
                Environment
                        .getExternalStorageDirectory()+
                        File.separator+
                        "Tulip_sante/Patients/"+
                        patientId+
                        File.separator+
                        type+
                        File.separator+
                        imagePath);
        if(myPath.exists()) {
            Glide.with(context).load(myPath).into(imageView);
        }
    }

    private void onDialogCancel() {
        cancel.setOnClickListener(view -> dismiss());
    }
}