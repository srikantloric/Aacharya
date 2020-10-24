package com.example.loric.aacharya;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ContactUsActivity extends AppCompatActivity {

    private static final int REQUEST_PHONE_CALL = 001;
    ImageView callBtn1, callBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        callBtn1 = findViewById(R.id.call_btn_1);
        callBtn2 = findViewById(R.id.call_btn_2);

        callBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               callContact("+917979080633");
            }
        });
        callBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               callContact("+917979080654");
            }
        });
    }




    private void callContact( String ContactNo){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + ContactNo));//change the number
        if (ActivityCompat.checkSelfPermission(ContactUsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactUsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            startActivity(callIntent);
        }
    }
}