package com.example.loric.aacharya;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.loric.aacharya.DbQueries.isQrScannedAndMatched;

public class ScanQrCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void handleResult(final Result result) {

        if (result.toString().trim().toUpperCase().equals(getIntent().getStringExtra("QR_SERVER").trim().toUpperCase())) {
            isQrScannedAndMatched = true;
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.beep);
            mp.start();
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.attendance_recorded_sucess_layout);
            dialog.setCancelable(true);
            dialog.show();
        } else {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.beep);
            mp.start();
            isQrScannedAndMatched = false;
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.attendance_recorded_failed_layout);
            dialog.setCancelable(true);
            dialog.show();
        }
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