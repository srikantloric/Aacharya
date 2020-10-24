package com.example.loric.aacharya;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQrCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);


    }

    @Override
    public void handleResult(final Result result) {

        if (result.toString().trim().toUpperCase().equals(getIntent().getStringExtra("QR_SERVER").trim().toUpperCase())) {
            Log.d("joj", "handleResult: Matched successfully");
        }
        Log.d("joj", "handleResult: " + result);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.attendance_recorded_layout);
        dialog.setCancelable(true);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}