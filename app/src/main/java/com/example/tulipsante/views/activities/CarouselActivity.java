package com.example.tulipsante.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.models.Parametre;
import com.example.tulipsante.utils.Connectivity;
import com.example.tulipsante.utils.synchro;
import com.example.tulipsante.viewModel.CarouselViewModel;
import com.example.tulipsante.views.adapters.SliderAdapter;
import com.example.tulipsante.models.uIModels.SliderListModel;
import com.example.tulipsante.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CarouselActivity extends AppCompatActivity {
    private static final int NUM_PAGES = 3;
    private static int currentPage = 0;
    int[][] states = new int[][] {
            new int[] { android.R.attr.state_enabled}, // enabled
            new int[] {-android.R.attr.state_enabled}, // disabled
            new int[] {-android.R.attr.state_checked}, // unchecked
            new int[] { android.R.attr.state_pressed}  // pressed
    };
    int[] colors = new int[] {
            Color.RED,
            Color.RED,
            Color.RED,
            Color.RED
    };
    ColorStateList myList = new ColorStateList(states, colors);
    Thread workerThread = null;
    private List<Parametre> parametreList = new ArrayList<>();
    private Connectivity conn;
    // Views
    private ViewPager2 viewPager2;
    private ProgressBar progress_circular;
    private TextView textViewSync;

    private void initViews() {
        viewPager2 = findViewById(R.id.viewPager);
        progress_circular = findViewById(R.id.progress_circular);
        textViewSync = findViewById(R.id.textViewSync);
    }

    private void initialisation() {
        conn = new Connectivity(this);
        // View model
        CarouselViewModel carouselViewModel = ViewModelProviders.of(this).get(CarouselViewModel.class);
        carouselViewModel.getParametreList().observe(this, parametres -> parametreList.addAll(parametres));
        File parentDirectory = new File(Environment.getExternalStorageDirectory()+File.separator+"Tulip_sante");
        File childDirectory1 = new File(parentDirectory+File.separator+"Medecin");
        File childDirectory2 = new File(parentDirectory+File.separator+"Patients");
        if(!parentDirectory.exists()) {
            boolean res1 = parentDirectory.mkdirs();
            boolean res2 = childDirectory1.mkdirs();
            boolean res3 = childDirectory2.mkdirs();
            if(res1 || res2 || res3) {
                System.out.println("Folders created!");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_carousel);

        initViews();
        initialisation();

        setViewPager();
        setSynchronization();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkNetworkStatus() {
        return conn.haveNetworkConnection();
    }

    public void setPeriodicSync() {
        Timer timer = new Timer();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                syncComplete();
            }
        };
        //noinspection OctalInteger
        timer.scheduleAtFixedRate(myTask,  01, 3 * (60*1000));
    }

    private void setViewPager() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        viewPager2.setUserInputEnabled(false);
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            viewPager2.setCurrentItem(currentPage++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }

    private void getdata() {
        ArrayList<SliderListModel> sliderListModelArrayList = new ArrayList<>();
        sliderListModelArrayList.add(new SliderListModel(0,"doc_image1"));
        sliderListModelArrayList.add(new SliderListModel(1,"doc_image2"));
        sliderListModelArrayList.add(new SliderListModel(2,"doc_image3"));

        SliderAdapter adapter = new SliderAdapter(this);
        adapter.setSliderListModel(sliderListModelArrayList);
        viewPager2.setAdapter(adapter);
    }

    private void setSynchronization() {
        Timer timer = new Timer();
        workerThread = new Thread(this::sync);
        new Handler().postDelayed(() -> {
            workerThread.start();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(!workerThread.isAlive()) {
                        runOnUiThread(() -> {
                            progress_circular.setVisibility(View.INVISIBLE);
                            textViewSync.setVisibility(View.INVISIBLE);
                            getdata();
                            setPeriodicSync();
                        } );
                        timer.cancel();
                    }
                }
            }, 2000, 1000);
        }, 2000);

        progress_circular.setVisibility(View.VISIBLE);
        textViewSync.setVisibility(View.VISIBLE);

        if(workerThread == null) {
            return;
        }
        workerThread.isAlive();
    }

    private void sync() {
            try {
                System.out.println("==== Network status ======");
                System.out.println(checkNetworkStatus());
                if(checkNetworkStatus()) {
                    if(!parametreList.isEmpty()) {
                        new synchro(
                                CarouselActivity.this,
                                CarouselActivity.this,
                                "tulip_sante_db",
                                parametreList.get(0).getDbName(),
                                parametreList.get(0).getDbPass(),
                                parametreList.get(0).getWebSiteName(),
                                parametreList.get(0).getFtpName(),
                                parametreList.get(0).getFtpPass()).synchronizeMedecin();
                    }
                    else {
                        runOnUiThread(() -> {
                            Toast.makeText(
                                this,
                                "Parameters table empty!",
                                Toast.LENGTH_SHORT).show();
                        });
                    }
                }
                else {
                    runOnUiThread(() -> {
                        textViewSync.setText(getResources().getText(R.string.no_internet_connection));
                        textViewSync.setTextColor(Color.RED);
                        progress_circular.setIndeterminateTintList(myList);
                        Toast.makeText(
                            CarouselActivity.this,
                            "⚠ No Internet Connection!",
                            Toast.LENGTH_SHORT).show();

                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void syncComplete() {
        try {
                if(checkNetworkStatus()) {
                    if(!parametreList.isEmpty()) {
                        runOnUiThread(() -> {
                            Toast toast = Toast.makeText(
                                    CarouselActivity.this, "SYNC", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.END,1,1);
                            toast.show();
                        });
                        System.out.println("++++ Sync ++++");
                        new synchro(
                                CarouselActivity.this,
                                CarouselActivity.this,
                                "tulip_sante_db",
                                parametreList.get(0).getDbName(),
                                parametreList.get(0).getDbPass(),
                                parametreList.get(0).getWebSiteName(),
                                parametreList.get(0).getFtpName(),
                                parametreList.get(0).getFtpPass()).synchronize();
                    }
                    else {
                        runOnUiThread(() -> {
                            Toast.makeText(
                                this,
                                "Parameters table empty!",
                                Toast.LENGTH_SHORT).show();
                        });
                }
                }
                else {
                    runOnUiThread(() -> {
                        Toast toast = Toast.makeText(
                                CarouselActivity.this,
                                "⚠ No Internet Connection!",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.END,1,1);
                        toast.show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
