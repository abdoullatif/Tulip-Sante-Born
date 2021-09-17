package com.example.tulipsante.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tulipsante.views.adapters.GalleryListItemAdapter;
import com.example.tulipsante.interfaces.RecyclerImageClick;
import com.example.tulipsante.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ChooseImageActivity extends AppCompatActivity implements RecyclerImageClick {

    private RecyclerView recyclerView;
    private CardView cardViewClose, cardViewDone;
    private ImageView imageView, imageViewPhoto;

    private final GalleryListItemAdapter galleryAdapter =
            new GalleryListItemAdapter(this);

    private File selectedImage;

    private void initViews() {
        recyclerView = findViewById(R.id.recViewGallery);
        imageView = findViewById(R.id.imageViewPlaceHolder);
        cardViewClose = findViewById(R.id.cardViewBack);
        cardViewDone = findViewById(R.id.cardViewDone);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_choose_image);

        initViews();

        display();
        onBackButtonPressed();
        onDoneButtonPressed();
        onImagePhotoPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int LAUNCH_SECOND_ACTIVITY = 1;
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                String result = data.getStringExtra("image");
                selectedImage = new File(result) ;
                imageView.setImageURI(Uri.parse(result));
            }
        }
    }

    private void onImagePhotoPressed() {
        int LAUNCH_SECOND_ACTIVITY = 1;
        imageViewPhoto.setOnClickListener(view -> {
            Intent intent1 = new Intent(
                    ChooseImageActivity.this,
                    TakeImageActivity.class);
            startActivityForResult(intent1, LAUNCH_SECOND_ACTIVITY);
        });
    }

    private void setApplicationTheme() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currentTheme = sharedPreferences.getString("current_Theme",null);
        assert currentTheme != null;
        if(currentTheme.equals("dark")) {
            setTheme(R.style.AppTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }
    }

    @Override
    public void onCenterImageChange(File imagePath) {
        imageView.setImageURI(Uri.fromFile(imagePath));
        selectedImage = imagePath;
    }

    private void onBackButtonPressed() {
        cardViewClose.setOnClickListener(view -> {
            setResult(0);
            finish();
        });
    }

    private void onDoneButtonPressed() {
        cardViewDone.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("imagePath",String.valueOf(selectedImage));
            setResult(RESULT_OK,returnIntent);
            finish();
        });
    }

    private ArrayList<File> findImage(File file) {
        ArrayList<File> imageList=new ArrayList<>();
        File[] imageFile=  file.listFiles();
        if(imageFile == null) {
            Toast.makeText(this, "No Images In Gallery", Toast.LENGTH_SHORT).show();
        } else {
            if (imageFile.length > 1) {
                Arrays.sort(imageFile);
            }
            for (File singleimage : imageFile) {
                // if the image is a directory than add all images inside the directory
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

    private void display() {
        ArrayList<File> myimageFile = findImage(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/Camera"));
        galleryAdapter.setImagePath(myimageFile);
        galleryAdapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setAdapter(galleryAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
    }
}
