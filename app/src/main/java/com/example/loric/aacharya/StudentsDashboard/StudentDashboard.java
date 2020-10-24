package com.example.loric.aacharya.StudentsDashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.loric.aacharya.Fragments.AttendanceFrag;
import com.example.loric.aacharya.Fragments.FChatFrag;
import com.example.loric.aacharya.Fragments.HomeFrag;
import com.example.loric.aacharya.Fragments.SettingsFrag;
import com.example.loric.aacharya.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class StudentDashboard extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    private int backPressCount = 0;
    private boolean isHomeFrag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#3F8E88"));
        initView();
        BottomMenu();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFrag()).commit();

    }

    private void initView() {
        chipNavigationBar = findViewById(R.id.chip_navigation_bar);
    }

    private void BottomMenu() {
        chipNavigationBar.setItemSelected(R.id.home, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.home:
                        isHomeFrag = true;
                        backPressCount=0;
                        fragment = new HomeFrag();
                        break;
                    case R.id.attendance:
                        isHomeFrag = false;
                        backPressCount=0;
                        fragment = new AttendanceFrag();
                        break;
                    case R.id.f_chat:
                        isHomeFrag = false;
                        backPressCount=0;
                        fragment = new FChatFrag();
                        break;
                    case R.id.settings:
                        isHomeFrag = false;
                        backPressCount=0;
                        fragment = new SettingsFrag();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });

    }


    @Override
    public void onBackPressed() {
        backPressCount++;
        if (isHomeFrag) {
            Toast.makeText(this, "Press once more to exit", Toast.LENGTH_SHORT).show();
            if (backPressCount == 2) {
                super.onBackPressed();
            }
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFrag()).commit();
            chipNavigationBar.setItemSelected(R.id.home, true);
        }

    }
}