package com.example.tulipsante.views.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tulipsante.R;
import com.example.tulipsante.viewModel.PermissionDialogViewModel;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PermissionDialogFragment extends BottomSheetDialogFragment {
    private CardView cancel;
//    private DisableNfc listener;
    private Context context;
    private SignaturePad signaturePad;
    private CardView cardViewValidate;
    private RadioGroup radioGroupLength;

    private String length;
    private String idPatient;

    // View Model
    private PermissionDialogViewModel permissionDialogViewModel;

    public PermissionDialogFragment(Context context, String idPatient) {
        this.context = context;
        this.idPatient = idPatient;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_permission_dialog, container, false);
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
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
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
        signaturePad = view.findViewById(R.id.signaturePad);
        cardViewValidate = view.findViewById(R.id.buttonValidate);
        radioGroupLength = view.findViewById(R.id.radioGroupLength);

    }

    private void initialisation() {
        cardViewValidate.setEnabled(false);
        permissionDialogViewModel = ViewModelProviders
                .of(this)
                .get(PermissionDialogViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        onSignatureDone();
        onDialogCancel();
        onButtonValidate(view);

    }

    private void onButtonValidate(View v) {
        cardViewValidate.setOnClickListener(view -> {
            Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
            String photoPath = addJpgSignatureToGallery(signatureBitmap);
            if(photoPath != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                Date todayDate = new Date(calendar.getTimeInMillis());
                String dateRequested = sdf.format(todayDate);

                RadioButton radioButtonLength =
                        v.findViewById(radioGroupLength.getCheckedRadioButtonId());
                length = radioButtonLength.getText().toString().trim();
                String type = null;
                String dateExpiration = null;
                if (length.equals("Never")) {
                    type = "private";
                    dateExpiration = "";
                }
                if (length.equals("Always")) {
                    type = "public";
                    calendar.add(Calendar.YEAR, 5);
                    Date resultDate = new Date(calendar.getTimeInMillis());
                    dateExpiration = sdf.format(resultDate);
                }
                if(length.equals("24 hours")) {
                    type = "public";
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    Date resultDate = new Date(calendar.getTimeInMillis());
                    dateExpiration = sdf.format(resultDate);
                }
                if(length.equals("7 Days")) {
                    type = "public";
                    calendar.add(Calendar.DAY_OF_MONTH, 7);
                    Date resultDate = new Date(calendar.getTimeInMillis());
                    dateExpiration = sdf.format(resultDate);
                }
                if(length.equals("1 Month")) {
                    type = "public";
                    calendar.add(Calendar.DAY_OF_MONTH, 30);
                    Date resultDate = new Date(calendar.getTimeInMillis());
                    dateExpiration = sdf.format(resultDate);
                }
                permissionDialogViewModel.insertPermission(dateRequested,dateExpiration, type, photoPath);
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            else {
                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSignatureDone() {
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                cardViewValidate.setEnabled(true);
            }

            @Override
            public void onClear() {
                cardViewValidate.setEnabled(false);
            }
        });
    }

    private void onDialogCancel() {
        cancel.setOnClickListener(view -> dismiss());
    }

    public String addJpgSignatureToGallery(Bitmap signature) {
        String result = null;
        try {
            File photo = new File(
                    Environment.getExternalStorageDirectory()
                            +File.separator+
                            "Tulip_sante"+
                            File.separator+
                            "Patients"+
                            File.separator+
                            idPatient+
                            File.separator+
                            "Personal", String.format("Signature_%d.jpg",System.currentTimeMillis())
            );
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = photo.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);;
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    private File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        ), albumName);
        if(!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }


}