package com.example.loric.aacharya.EventGallery;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.R;
import com.jsibbold.zoomage.ZoomageView;

public class PhotoFullScreenActivity extends AppCompatActivity {

    ImageView backBtn;
    ZoomageView zoomageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_full_screen);
        zoomageView = findViewById(R.id.myZoomageView);
        backBtn = findViewById(R.id.l_one);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String imageUri = getIntent().getStringExtra("IMAGE_URL");
        Glide.with(this).load(imageUri).into(zoomageView);

    }
}