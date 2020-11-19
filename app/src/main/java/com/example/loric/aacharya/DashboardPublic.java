package com.example.loric.aacharya;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.loric.aacharya.Account.LoginActivity;
import com.example.loric.aacharya.Adapters.ImageSliderAdapter;
import com.example.loric.aacharya.EventGallery.EventGalleryActivity;
import com.example.loric.aacharya.Models.ImageSliderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.tomer.fadingtextview.FadingTextView;

import java.util.ArrayList;

public class DashboardPublic extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_PHONE_CALL = 001;
    private FadingTextView fadingTextView;
    private SliderView sliderView;
    private ImageView mapView, drawerBtn, directorImage;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView studentLoginBtn, directorMessageText, translateBtn;
    private LinearLayout eventGalleryBtn, achievementBtn, coursesOfferBtn, facultiesList;
    private Button admissionEnquiryBtn;

    private Boolean isExpanded = false;
    private Boolean isTranslated = false;

    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_public);
        firebaseFirestore = FirebaseFirestore.getInstance();
//                Window window = getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.parseColor("#FFFFFF"));
        viewInit();
        setNotifications();
        setImageSlider();
        setNavigation();
        setDashboardButtons();

        studentLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardPublic.this, LoginActivity.class);
                startActivity(intent);


            }
        });
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=24.396274,86.286126");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


    }

    private void setDashboardButtons() {
        eventGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardPublic.this, EventGalleryActivity.class);
                startActivity(intent);
            }
        });
        achievementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardPublic.this, AchievementActivity.class);
                startActivity(intent);
            }
        });
        coursesOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardPublic.this, CourseOfferedActivity.class);
                startActivity(intent);
            }
        });
        facultiesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardPublic.this, FacultiesListActivity.class);
                startActivity(intent);

            }
        });

        admissionEnquiryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardPublic.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setImageSlider() {
        ////Image Slider//
        progressBar.setVisibility(View.VISIBLE);
        final ArrayList<ImageSliderModel> imageSliderModelList = new ArrayList<>();
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();


        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.parseColor("#A9A9A9"));

        ////Image Slider//

        firebaseFirestore.collection("PUBLIC_DATA")
                .document("IMAGE_SLIDER")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            imageSliderModelList.clear();
                            for (int i = 0; i < task.getResult().getLong("list_size"); i++) {
                                String imageUrl = task.getResult().getString("image_" + i);
                                imageSliderModelList.add(new ImageSliderModel(imageUrl));
                            }
                            sliderView.setSliderAdapter(new ImageSliderAdapter(DashboardPublic.this, imageSliderModelList));//set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        } else {
                            Toast.makeText(DashboardPublic.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void setNotifications() {
        //////Notifications
        final String[] strings = new String[1];
        fadingTextView.setTexts(strings);
        firebaseFirestore.collection("PUBLIC_DATA")
                .document("PUBLIC_MESSAGE")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Long list_size = task.getResult().getLong("list_size");
                            String[] strings = new String[Integer.parseInt(String.valueOf(list_size))];
                            for (int i = 0; i < list_size; i++) {
                                strings[i] = task.getResult().getString("message_" + i);
                            }
                            fadingTextView.setTexts(strings);
                        } else {
                            Toast.makeText(DashboardPublic.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void viewInit() {
        fadingTextView = findViewById(R.id.fading_tv);
        sliderView = findViewById(R.id.image_slider);
        mapView = findViewById(R.id.map_view);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        drawerBtn = findViewById(R.id.drawerBtn);
        studentLoginBtn = findViewById(R.id.studentLoginBtn);
        eventGalleryBtn = findViewById(R.id.eventGalleryBtn);
        achievementBtn = findViewById(R.id.our_achievement_btn);
        facultiesList = findViewById(R.id.faculties_list);
        coursesOfferBtn = findViewById(R.id.courses_available_btn);
        directorMessageText = findViewById(R.id.message_text);
        progressBar = findViewById(R.id.progress_imageslider);
        directorImage = findViewById(R.id.director_image);
        admissionEnquiryBtn = findViewById(R.id.admission_enquiry_btn);
        translateBtn = findViewById(R.id.translate_btn);
        directorMessageText.setText(Html.fromHtml(getResources().getString(R.string.director_message_short) + "<b>" + "<font color='#0D63FF'>" + " Read More" + "</font>" + "</b>"));

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTranslated) {
                    directorMessageText.setText(getResources().getString(R.string.director_message));
                    isTranslated = false;

                } else {
                    directorMessageText.setText(getResources().getString(R.string.director_message_hindi));
                    isTranslated = true;
                }
            }
        });


        directorMessageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    directorImage.setVisibility(View.VISIBLE);
                    translateBtn.setVisibility(View.GONE);
                    directorMessageText.setText(Html.fromHtml(getResources().getString(R.string.director_message_short) + "<b>" + "<font color='#0D63FF'>" + " Read More" + "</font>" + "</b>"));

                    isExpanded = false;
                } else {
                    directorImage.setVisibility(View.GONE);
                    directorMessageText.setText(getResources().getString(R.string.director_message));
                    translateBtn.setVisibility(View.VISIBLE);
                    isExpanded = true;
                }

            }
        });

    }

    private void setNavigation() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        drawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.setCheckedItem(R.id.nav_home);
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_login:
                Intent intent = new Intent(DashboardPublic.this,LoginActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_privacy:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://aacharyan.flycricket.io/privacy.html"));
                startActivity(browserIntent);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_rate:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.about_developer:
               Intent dev = new Intent(DashboardPublic.this,DeveloperContact.class);
               startActivity(dev);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }
}