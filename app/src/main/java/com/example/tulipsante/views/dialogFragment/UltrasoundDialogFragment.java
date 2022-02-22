package com.example.tulipsante.views.dialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tulipsante.interfaces.RecyclerImageClick;
import com.example.tulipsante.R;
import com.example.tulipsante.views.adapters.UltrasoundAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class UltrasoundDialogFragment extends BottomSheetDialogFragment implements RecyclerImageClick {
    private Context context;

    public ArrayList<String> listImages;

    // Views
    private CardView cancel, validate;
    private RecyclerView recyclerView;
    private ImageView imageView;

    // Adapter
    private UltrasoundAdapter ultrasoundAdapter;

    public UltrasoundDialogFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ultrasound, container, false);
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
        validate = view.findViewById(R.id.validate);
        recyclerView = view.findViewById(R.id.recViewGrid);
        imageView = view.findViewById(R.id.imageViewPlaceHolder);

    }

    private void initialisation() {
        ultrasoundAdapter = new UltrasoundAdapter(this);
        listImages = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        display();
        onDialogCancel();
        onValidatePressed();

    }

    private void onValidatePressed() {
        validate.setOnClickListener(view -> {
            dismiss();
        });
    }

    @Override
    public void onCenterImageChange(File imagePath) {
        imageView.setImageURI(Uri.fromFile(imagePath));
    }

    private void display() {
        ArrayList<File> myImageFile = findImage(new File(Environment.getExternalStorageDirectory()+File.separator+"WirelessKUS"));
        for (int i = 0; i < myImageFile.size(); i++) {
            listImages.add(myImageFile.get(i).getPath());
        }
        ultrasoundAdapter.setImagePath(myImageFile);
        ultrasoundAdapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setAdapter(ultrasoundAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context,3));
    }

    private ArrayList<File> findImage(File file) {
        ArrayList<File> imageList=new ArrayList<>();
        File[] imageFile=  file.listFiles();
        if(imageFile == null) {
            Toast.makeText(context, "No Images In Gallery", Toast.LENGTH_SHORT).show();
        } else {
            if (imageFile.length > 1) {
                Arrays.sort(imageFile);
            }
            for (File singleimage : imageFile) {
                if (singleimage.isDirectory() && !singleimage.isHidden()) {
                    imageList.addAll(findImage(singleimage));
                } else {
                    if (
                            singleimage.getName().endsWith(".jpg") ||
                                    singleimage.getName().endsWith(".png") ||
                                    singleimage.getName().endsWith(".webp") ||
                                    singleimage.getName().endsWith(".jpeg")
                    ) {
                        imageList.add(singleimage);
                    }
                }
            }
        }
        return  imageList;
    }

    private void onDialogCancel() {
        cancel.setOnClickListener(view -> {
            listImages = null;
            dismiss();
        });
    }
}
