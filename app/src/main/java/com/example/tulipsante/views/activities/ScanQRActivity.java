package com.example.tulipsante.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.tulipsante.R;

public class ScanQRActivity extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannView;

    private void initView() {
        scannView = findViewById(R.id.scanner_view);
    }

    private void initialisation() {
        codeScanner = new CodeScanner(this,scannView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan_q_r);

        initView();
        initialisation();

        onScannerDecoded();
        scannView.setOnClickListener(v -> codeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private void onScannerDecoded() {
        codeScanner.setDecodeCallback(result -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("res",result.getText());
            setResult(RESULT_OK,returnIntent);
            finish();
        });
    }
}