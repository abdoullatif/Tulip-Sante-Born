package com.example.tulipsante.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tulipsante.R;
import com.example.tulipsante.utils.SPUtils;
import com.guide.guidecore.GuideInterface;
import com.guide.guidecore.GuideUsbManager;
import com.guide.guidecore.UsbStatusInterface;
import com.guide.guidecore.view.IrSurfaceView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ThermalCamActivity extends AppCompatActivity implements GuideInterface.ImageCallBackInterface, UsbStatusInterface, View.OnClickListener {
    private static final int SRC_WIDTH = 90; //90
    private static final int SRC_HEIGHT = 120; //120
    // internal camera device
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    protected FrameLayout mIrSurfaceViewLayout;
    protected IrSurfaceView mIrSurfaceView;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private SharedPreferences sharedPreferences;
    private GuideInterface mGuideInterface;
    private GuideUsbManager guideUsbManager;
    private Bitmap mIrBitmap;
    private short[] mY16Frame;
    private short[] mSyncY16Frame;
    private final boolean isFliptY = true;
    private TextureView textureView;
    private ImageView mHighCrossView;
    private TextView mFocusTextView;
    private TextView mHumanTemp;
    private ImageView imageViewBack;
    private CardView cardViewValidate;
    private FrameLayout mDisplayFrameLayout;
    private FrameLayout mHumanDisplayFrameLayout;
    private RelativeLayout.LayoutParams irSurfaceViewLayoutParams;
    private RelativeLayout.LayoutParams displayViewLayoutParams;
    private RelativeLayout.LayoutParams humanDiaplayLayoutParams;
    private Timer mHumanTimer;
    private TimerTask mHumanTimerTask;
    private boolean isDispLayTemp;
    private boolean isDispLayHumanTemp;
    private int width;
    private int height;
    private int count;
    private final int FRAME = 25;
    private int maxIndex =0;
    private int rawWidth;
    private int rawHeight;
    private int highCrossWidth = 40;
    private int highCrossHeight = 40;
    private String maxTempStr = "0";
    private int rotateType = 1;
    private int irSurfaceViewWidth;
    private int irSurfaceViewHeight;
    private String cameraId;
    private Size imageDimension;

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            openCamera();
        }
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };
    private ImageReader imageReader;
    private File file;
    private Handler mBackgroundHandler;
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NotNull CameraDevice camera) {
            //This is called when the camera is open
            System.out.println("opened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NotNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NotNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };
    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(ThermalCamActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
            createCameraPreview();
        }
    };
    private String humanTemperature;

    private final Handler mHandler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mFocusTextView.setText(mGuideInterface.getFoucsTemp() + "•C");
                    System.out.println("===== Temperature =======");
                    System.out.println(mGuideInterface.getFoucsTemp());
                    break;
                case 1:
                    try {
//                    float ambientTemp = GuideInterface.DEFAULT_AMBIENT_TEMP;
//                    if(TextUtils.isEmpty(mAmbientTempEditText.getText())) {
//                        ambientTemp = GuideInterface.DEFAULT_AMBIENT_TEMP;
//                    } else {
//                        ambientTemp = Float.valueOf(mAmbientTempEditText.getText().toString());
//                    }
                        float centerTemp = Float.valueOf(mGuideInterface.getCenterTemp());
                        float ambientTemp = Float.valueOf("28");
                        float maxTemp = Float.parseFloat(maxTempStr);
                        humanTemperature = mGuideInterface.getHumanTemp(centerTemp, ambientTemp) + "•C";
                        mFocusTextView.setText(mGuideInterface.getHumanTemp(maxTemp, ambientTemp) + "•C");
                        mHumanTemp.setText(mGuideInterface.getHumanTemp(centerTemp, ambientTemp) + "•C");
                        onValidateResult();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 3:
                    Toast.makeText(ThermalCamActivity.this, "0.5 ", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(ThermalCamActivity.this, "1.2 ", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    private float mScale = 4f;

    private void initViews() {
        mFocusTextView = findViewById(R.id.focus_temp_display);
        mIrSurfaceViewLayout = findViewById(R.id.final_ir_layout);
        mDisplayFrameLayout = findViewById(R.id.temp_display_layout);
        mHumanDisplayFrameLayout = findViewById(R.id.human_temp_display_layout);
        textureView = findViewById(R.id.textureView);
        imageViewBack = findViewById(R.id.imageViewBackButton);
        mHumanTemp = findViewById(R.id.textViewBodyTemp);
        cardViewValidate = findViewById(R.id.cardViewValidate);

        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);

        mIrSurfaceView = new IrSurfaceView(this);
        FrameLayout.LayoutParams ifrSurfaceViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mIrSurfaceView.setLayoutParams(ifrSurfaceViewLayoutParams);

        mIrSurfaceView.setMatrix(dip2px(360)/360,0,0);
        mIrSurfaceViewLayout.addView(mIrSurfaceView);

        width = (int) getResources().getDimension(R.dimen.ir_width);
        height = (int) getResources().getDimension(R.dimen.ir_height);

        highCrossWidth = (int) getResources().getDimension(R.dimen.high_cross_width);
        highCrossHeight = (int) getResources().getDimension(R.dimen.high_cross_height);

        mIrSurfaceViewLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            irSurfaceViewLayoutParams = (RelativeLayout.LayoutParams) mIrSurfaceViewLayout.getLayoutParams();
            displayViewLayoutParams = (RelativeLayout.LayoutParams) mDisplayFrameLayout.getLayoutParams();
            humanDiaplayLayoutParams = (RelativeLayout.LayoutParams)mHumanDisplayFrameLayout.getLayoutParams();

            switch(rotateType){
                case 1:

                case 3:
                    irSurfaceViewWidth = width;
                    irSurfaceViewHeight = height;
                    break;
                case 2:

                case 0:
                    irSurfaceViewWidth = height;
                    irSurfaceViewHeight = width;
                    break;
            }

            irSurfaceViewLayoutParams.width = irSurfaceViewWidth ;
            irSurfaceViewLayoutParams.height = irSurfaceViewHeight;
            mIrSurfaceViewLayout.setLayoutParams(irSurfaceViewLayoutParams);

            displayViewLayoutParams.width = irSurfaceViewWidth;
            displayViewLayoutParams.height = irSurfaceViewHeight;
            mDisplayFrameLayout.setLayoutParams(displayViewLayoutParams);

            humanDiaplayLayoutParams.width = irSurfaceViewWidth;
            humanDiaplayLayoutParams.height = irSurfaceViewHeight;
            mHumanDisplayFrameLayout.setLayoutParams(humanDiaplayLayoutParams);

            mHighCrossView = new ImageView(ThermalCamActivity.this);
//                mHighCrossView.setImageResource(R.drawable.high_cross);
            mHighCrossView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setApplicationTheme();
        setContentView(R.layout.activity_thermal_cam);

        initViews();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        onCloseButtonPressed();
        try {
            mGuideInterface = new GuideInterface();
            int paletteIndex = Integer.parseInt(SPUtils.getPalette(ThermalCamActivity.this));
            mScale = Float.parseFloat(SPUtils.getScale(ThermalCamActivity.this));
            rotateType = Integer.parseInt(SPUtils.getRotate(ThermalCamActivity.this));
            mGuideInterface.guideCoreInit(this,paletteIndex, mScale , rotateType);
            mGuideInterface.setContrast(90);

            String imageAlgoSwitch = SPUtils.getImageAlgo(ThermalCamActivity.this);
            mGuideInterface.controlImageOptimizer(TextUtils.equals(imageAlgoSwitch, "开"));
            mY16Frame = new short[SRC_WIDTH*SRC_HEIGHT];
            mSyncY16Frame = new short[SRC_WIDTH*SRC_HEIGHT];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setApplicationTheme() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String currentTheme = sharedPreferences.getString("current_Theme",null);
        if(currentTheme.equals("dark")) {
            setTheme(R.style.AppTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }
    }

    private void onCloseButtonPressed() {
        imageViewBack.setOnClickListener(view -> {
            mGuideInterface.sendResetOrder();
            setResult(0);
        });
    }

    private float dip2px(int dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (dpValue * scale);
    }

    @Override
    public void onBackPressed() {
        mGuideInterface.sendResetOrder();
        setResult(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mGuideInterface != null) {
            mGuideInterface.guideCoreDestory();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");
        String autoShutterSwitch = SPUtils.getSwitch(ThermalCamActivity.this);
        long period = Long.parseLong(SPUtils.getPeriod(ThermalCamActivity.this));
        long delay = Long.parseLong(SPUtils.getDelay(ThermalCamActivity.this));

        mGuideInterface.registUsbPermissions();
        mGuideInterface.registUsbStatus(this);
        mGuideInterface.setAutoShutter(TextUtils.equals(autoShutterSwitch, "开"), period, delay);
        mGuideInterface.startGetImage(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mGuideInterface.stopGetImage();
        mGuideInterface.unRegistUsbPermissions();
        mGuideInterface.unRigistUsbStatus();
    }

    public void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Do you want to record current temperature " + humanTemperature + " C?");

        builder.setPositiveButton("Yes/Continue", (dialogInterface, i) -> {
            finish();
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void callBackOneFrameBitmap(Bitmap bitmap, final short[] y16Frame) {
        mGuideInterface.setRototeType(1);
        mGuideInterface.setFilpY(false);
        mIrBitmap = bitmap;
        if(mIrSurfaceView.getShowAjustView()) {
            runOnUiThread(() -> {
                int c1Y16Index = mIrSurfaceView.getC1Y16Index(rotateType);
                short c1Y16 = y16Frame[c1Y16Index];
                System.out.println("c1Y16 = " + c1Y16);
                String c1Temp = mGuideInterface.measureTemByY16(c1Y16);
                int c2Y16Index = mIrSurfaceView.getC2Y16Index(rotateType);
                short c2Y16 = y16Frame[c2Y16Index];
                System.out.println("c2Y16 = " + c2Y16);
                String c2Temp = mGuideInterface.measureTemByY16(c2Y16);
                mIrSurfaceView.setC1Text(c1Temp);
                mIrSurfaceView.setC2Text(c2Temp);
            });
        }
        mIrSurfaceView.doDraw(mIrBitmap,mGuideInterface.getShutterStatus());
        mY16Frame = y16Frame;


        count++;
        if(count % FRAME == 0){

            Log.v("GUIDE","======================");
            final short maxY16 = getMaxY16(y16Frame);
            maxTempStr = mGuideInterface.measureTemByY16(maxY16);
            if(rotateType == 1 || rotateType == 3){
                //宽是90，高是120
                rawWidth = SRC_WIDTH;
                rawHeight = SRC_HEIGHT;

            }else{
                //宽是120，高是90
                rawWidth = SRC_HEIGHT;
                rawHeight = SRC_WIDTH;
            }
            int centerIndex = rawWidth *(rawHeight/2) + rawWidth / 2;

            long measureTimeStart = System.currentTimeMillis();
            String centerTempStr = mGuideInterface.measureTemByY16(y16Frame[centerIndex]);
            long measureTimeEnd = System.currentTimeMillis();
            String centerTmepStrSDK = mGuideInterface.getCenterTemp();

            runOnUiThread(() -> {
                mDisplayFrameLayout.removeView(mHighCrossView);
                float scale = irSurfaceViewWidth / rawWidth;
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(highCrossWidth , highCrossHeight);
                lp.leftMargin = (int) ((maxIndex% rawWidth)*scale - highCrossWidth/2);
                lp.topMargin = (int) ((maxIndex / rawWidth)* scale - highCrossHeight/2);
                mDisplayFrameLayout.addView(mHighCrossView, lp);

            });

            count = 0;
        }


    }

    private short getMaxY16(short[]y16Arr){
        short maxY16 = Short.MIN_VALUE;
        int length = y16Arr.length;
        for(int i =0 ; i<length ;i++){

            if(maxY16 < y16Arr[i]){
                maxY16 = y16Arr[i];
                maxIndex = i;
            }
        }
        return maxY16;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void usbConnect() {
        System.out.println("usb connect");
    }

    @Override
    public void usbDisConnect() {
        finish();
    }

    // internal camera
    // internal camera// internal camera

    public void onTempBtnClicked(View view) {
        onHumanTempBtnClick();
    }

    private void onValidateResult() {
        cardViewValidate.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("temp ","10");
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("temp", humanTemperature);
            edit.apply();
            setResult(RESULT_OK,returnIntent);
            mGuideInterface.sendResetOrder();
        });
    }

    private void onHumanTempBtnClick() {
        if(false) {
            String ambientTempStr = "30";
            float ambientTemp = Float.parseFloat(ambientTempStr);
            if(ambientTemp < 10 || ambientTemp > 32) {
                Toast.makeText(this, "The ambient temperature input is illegal\n", Toast.LENGTH_LONG).show();
                return;
            }
        }

        isDispLayHumanTemp = !isDispLayHumanTemp;
        if (isDispLayHumanTemp) {
            cardViewValidate.setVisibility(View.VISIBLE);

            mHumanTimer = new Timer();
            mHumanTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(1);
                }
            };
            mHumanTimer.schedule(mHumanTimerTask, 0, 1000);
        } else {
            mHumanDisplayFrameLayout.setVisibility(View.GONE);

            mHandler.removeCallbacksAndMessages(null);
            mHumanTimerTask.cancel();
            mHumanTimerTask = null;
            mHumanTimer.cancel();
            mHumanTimer = null;
        }
    }

    private void createCameraPreview() {
        try {
//            textureView.setRotation(270);
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);

            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(ThermalCamActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            assert manager != null;
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[1];
            System.out.println(Arrays.toString(map.getOutputSizes(SurfaceTexture.class)));
//            mCenterTextView.setText(Arrays.toString(map.getOutputSizes(SurfaceTexture.class)));
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(ThermalCamActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ThermalCamActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ThermalCamActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void updatePreview() {
        if(cameraDevice == null) {
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
