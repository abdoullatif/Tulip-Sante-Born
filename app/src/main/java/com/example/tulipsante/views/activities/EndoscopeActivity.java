package com.example.tulipsante.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;

import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.R;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.jiangdg.usbcamera.utils.FileUtils;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.widget.CameraViewInterface;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EndoscopeActivity extends AppCompatActivity
        implements CameraViewInterface.Callback, CameraDialog.CameraDialogParent  {
    private static final String TAG = "EndoscopeActivity";

    private TextureView textureView;
    @BindView(R.id.cardViewCapture)
    public CardView cardViewCapture;
    @BindView(R.id.imageViewBackButton)
    public ImageView backButton;
    @BindView(R.id.cardViewCamera)
    public CardView cardViewCamera;
    @BindView(R.id.cardViewVideo)
    public CardView cardViewVideo;
    @BindView(R.id.buttonVideo)
    public CardView buttonVideo;
    @BindView(R.id.textViewTitle)
    public TextView textViewTitle;


    private UVCCameraHelper mCameraHelper;
    private CameraViewInterface mUVCCameraView;

    private boolean isRequest;
    private boolean isPreview;

    private boolean isCameraOn = true;
    private boolean isVideoOn = false;

    private void initViews() {
        textureView = findViewById(R.id.textureView);
    }

    private void initialisation() {
        // step.1 initialize UVCCameraHelper
        mUVCCameraView = (CameraViewInterface) textureView;
        mUVCCameraView.setCallback(this);
        mCameraHelper = UVCCameraHelper.getInstance();
        mCameraHelper.setDefaultFrameFormat(UVCCameraHelper.FRAME_FORMAT_MJPEG);
        mCameraHelper.initUSBMonitor(this, mUVCCameraView, listener);

        mCameraHelper.setOnPreviewFrameListener(new AbstractUVCCameraHandler.OnPreViewResultListener() {
            @Override
            public void onPreviewResult(byte[] nv21Yuv) {
                Log.d(TAG, "onPreviewResult: "+nv21Yuv.length);
            }
        });
        textViewTitle.setText(getResources().getString(R.string.preview));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_endoscope);
        ButterKnife.bind(this);
        initViews();
        initialisation();

        onCardViewCapturePressed();
        onBackButtonPressed();
        onVideoTogglePressed();
        onCameraTogglePressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraHelper.registerUSB();
    }

    private void setApplicationTheme() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String currentTheme = sharedPreferences.getString("current_Theme",null);
        assert currentTheme != null;
        if(currentTheme.equals("dark")) {
            setTheme(R.style.AppTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }
    }

    private void onCameraTogglePressed() {
        cardViewCamera.setOnClickListener(view -> {
            isCameraOn = true;
            isVideoOn = false;
            textViewTitle.setText(getResources().getText(R.string.camera));
            cardViewCamera.setCardBackgroundColor(getColor(R.color.darkgrey));
            cardViewVideo.setCardBackgroundColor(getColor(R.color.red));
            buttonVideo.setVisibility(View.INVISIBLE);
        });
    }

    private void onVideoTogglePressed() {
        cardViewVideo.setOnClickListener(view -> {
            isCameraOn = false;
            isVideoOn = true;
            textViewTitle.setText(getResources().getText(R.string.video));
            cardViewCamera.setCardBackgroundColor(getColor(R.color.blue));
            cardViewVideo.setCardBackgroundColor(getColor(R.color.darkgrey));
            buttonVideo.setVisibility(View.VISIBLE);
        });
    }

    private void onBackButtonPressed() {
        backButton.setOnClickListener(view -> finish());
    }

    private void onCardViewCapturePressed() {
        if(isCameraOn) {
            Date currentDate = Calendar.getInstance().getTime();
            File file = new File(Environment
                    .getExternalStorageDirectory()+ "/Tulip_sante/PatientId/Endoscope");
            if(!file.exists()) {
                boolean res = file.mkdirs();
                if(res) {
                    System.out.println(file.getPath() + "Created!");
                }
            }
            File finalFile = new File(file.getPath()+ "/" + currentDate + ".jpg");
            String picPath = String.valueOf(finalFile);
            cardViewCapture.setOnClickListener(view -> {
                mCameraHelper.setModelValue(UVCCameraHelper.MODE_BRIGHTNESS,200);
                mCameraHelper.capturePicture(picPath, picPath1 -> {
                    mCameraHelper.setModelValue(UVCCameraHelper.MODE_BRIGHTNESS,50);
                    Log.i(TAG, "onCaptureResult, saved:" + picPath1);
                });
            });
        }

    }

    private UVCCameraHelper.OnMyDevConnectListener listener = new UVCCameraHelper.OnMyDevConnectListener() {

        @Override
        public void onAttachDev(UsbDevice device) {
            // request open permission
            if (!isRequest) {
                isRequest = true;
                if (mCameraHelper != null) {
                    mCameraHelper.requestPermission(0);
                }
            }
        }

        @Override
        public void onDettachDev(UsbDevice device) {
            // close camera
            if (isRequest) {
                isRequest = false;
                mCameraHelper.closeCamera();
                showShortMsg(device.getDeviceName() + " is out");
            }
        }

        @Override
        public void onConnectDev(UsbDevice device, boolean isConnected) {
            if (!isConnected) {
                showShortMsg("fail to connect,please check resolution params");
                isPreview = false;
            } else {
                isPreview = true;
                showShortMsg("connecting");
                // initialize seekbar
                // need to wait UVCCamera initialize over
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Looper.prepare();
                        if(mCameraHelper != null && mCameraHelper.isCameraOpened()) {
//                            mSeekBrightness.setProgress(mCameraHelper.getModelValue(UVCCameraHelper.MODE_BRIGHTNESS));
//                            mSeekContrast.setProgress(mCameraHelper.getModelValue(UVCCameraHelper.MODE_CONTRAST));
                        }
                        Looper.loop();
                    }
                }).start();
            }
        }

        @Override
        public void onDisConnectDev(UsbDevice device) {
            showShortMsg("disconnecting");
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // step.2 register USB event broadcast
        if (mCameraHelper != null) {
            mCameraHelper.registerUSB();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // step.3 unregister USB event broadcast
        if (mCameraHelper != null) {
            mCameraHelper.unregisterUSB();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.releaseFile();
        // step.4 release uvc camera resources
        if (mCameraHelper != null) {
            mCameraHelper.release();
        }
    }

    private void showShortMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public USBMonitor getUSBMonitor() {
        return mCameraHelper.getUSBMonitor();
    }

    @Override
    public void onDialogResult(boolean canceled) {
        if (canceled) {
            showShortMsg("取消操作");
        }
    }

    public boolean isCameraOpened() {
        return mCameraHelper.isCameraOpened();
    }

    @Override
    public void onSurfaceCreated(CameraViewInterface view, Surface surface) {
        if (!isPreview && mCameraHelper.isCameraOpened()) {
            mCameraHelper.startPreview(mUVCCameraView);
            isPreview = true;
        }
    }

    @Override
    public void onSurfaceChanged(CameraViewInterface view, Surface surface, int width, int height) {

    }

    @Override
    public void onSurfaceDestroy(CameraViewInterface view, Surface surface) {
        if (isPreview && mCameraHelper.isCameraOpened()) {
            mCameraHelper.stopPreview();
            isPreview = false;
        }
    }
}
