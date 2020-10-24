package com.example.loric.aacharya.FacultyDashboard;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.loric.aacharya.Fragments.HomeFrag;
import com.example.loric.aacharya.Fragments.HomeFragmentFaculty;
import com.example.loric.aacharya.Fragments.SChatFragment;
import com.example.loric.aacharya.Fragments.SettingsFragmentFaculty;
import com.example.loric.aacharya.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class DashboardFaculty extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;
    private int backPressCount = 0;
    private boolean isHomeFrag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_faculty);
        initView();
        BottomMenu();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_faculty, new HomeFragmentFaculty()).commit();
    }

    private void initView() {
        chipNavigationBar = findViewById(R.id.chip_navigation_bar_faculty);
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
                        backPressCount = 0;
                        fragment = new HomeFragmentFaculty();
                        break;
                    case R.id.f_chat:
                        isHomeFrag = false;
                        backPressCount = 0;
                        fragment = new SChatFragment();
                        break;
                    case R.id.settings:
                        isHomeFrag = false;
                        backPressCount = 0;
                        fragment = new SettingsFragmentFaculty();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_faculty, fragment).commit();
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_faculty, new HomeFrag()).commit();
            chipNavigationBar.setItemSelected(R.id.home, true);
        }

    }
}