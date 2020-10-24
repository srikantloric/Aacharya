package com.example.loric.aacharya;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loric.aacharya.FacultyDashboard.DashboardFaculty;
import com.example.loric.aacharya.StudentsDashboard.StudentDashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0D63FF"));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            if (UserPreferences.getDefaultsBoolean("isStudent", SplashActivity.this)) {
                Intent intent = new Intent(SplashActivity.this, StudentDashboard.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, DashboardFaculty.class);
                startActivity(intent);
                finish();
            }

        } else {
            Intent intent = new Intent(SplashActivity.this, DashboardPublic.class);
            startActivity(intent);
            finish();
        }
    }
}