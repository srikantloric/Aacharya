package com.example.loric.aacharya;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.Adapters.CoursesOfferedAdapter;
import com.example.loric.aacharya.Models.CoursesOfferedModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CourseOfferedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CoursesOfferedAdapter adapter;
    private FirebaseFirestore firebaseFirestore;
    private List<CoursesOfferedModel> coursesOfferedModelList = new ArrayList<>();
    private Dialog loading;
    private ImageView dhamakaView;
    private TextView registrationFee, oneInstallmentPayment, twoInstallmentPayment, monthlyPayment;

    private CardView feeStructureLayout;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_offered);
        recyclerView = findViewById(R.id.rv_courses_offered);
        dhamakaView = findViewById(R.id.dhamaka_view);
        registrationFee = findViewById(R.id.registration_tv);
        oneInstallmentPayment = findViewById(R.id.onetime_payment_tv);
        twoInstallmentPayment = findViewById(R.id.two_installment_tv);
        monthlyPayment = findViewById(R.id.monthly_payment_tv);
        feeStructureLayout = findViewById(R.id.fee_structure_layout);
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        feeStructureLayout.animate().translationY(500);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseFirestore = FirebaseFirestore.getInstance();

        loading = new Dialog(this);
        loading.setContentView(R.layout.loading_dialog_layout);
        loading.setCancelable(false);
        loading.show();
        getDataFromServer();
        setDhamakaView();
        getFeeDetails();
    }

    private void getDataFromServer() {
        adapter = new CoursesOfferedAdapter(coursesOfferedModelList);
        recyclerView.setAdapter(adapter);
        firebaseFirestore.collection("AVAILABLE_COURSES")
                .orderBy("index")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                if (snapshot.getString("course_duration") != null) {
                                    coursesOfferedModelList.add(new CoursesOfferedModel(snapshot.getString("course_title")
                                            , snapshot.getString("course_detail")
                                            , "Detail : " + snapshot.getString("course_extra_detail") +
                                            " Course duration : " + snapshot.getString("course_duration")));
                                } else if (snapshot.getString("course_detail") == null) {
                                    coursesOfferedModelList.add(new CoursesOfferedModel(snapshot.getString("course_title")
                                            , "null"
                                            , "Detail : " + snapshot.getString("course_extra_detail")));
                                } else if (snapshot.getString("course_extra_detail") == null) {
                                    coursesOfferedModelList.add(new CoursesOfferedModel(snapshot.getString("course_title")
                                            , snapshot.getString("course_detail")
                                            , "null"));
                                } else {
                                    coursesOfferedModelList.add(new CoursesOfferedModel(snapshot.getString("course_title")
                                            , snapshot.getString("course_detail")
                                            , "Detail : " + snapshot.getString("course_extra_detail")));
                                }
                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(CourseOfferedActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        loading.dismiss();
                        feeStructureLayout.setVisibility(View.VISIBLE);
                        feeStructureLayout.animate().translationY(0);
                    }
                });

    }

    private void setDhamakaView() {
        firebaseFirestore.collection("PUBLIC_DATA")
                .document("VISIBILITY_CONTROLLER")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean isDhamakaActivated = task.getResult().getBoolean("isCourseOfferActive");
                            if (isDhamakaActivated) {
                                dhamakaView.setVisibility(View.VISIBLE);
                                firebaseFirestore.collection("PUBLIC_DATA")
                                        .document("COURSE_FEE_AND_BANNER")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    String dhamakaUrl = task.getResult().getString("dhamaka_banner_url");
                                                    Glide.with(CourseOfferedActivity.this).load(dhamakaUrl).into(dhamakaView);
                                                } else {
                                                    Toast.makeText(CourseOfferedActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                dhamakaView.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(CourseOfferedActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void getFeeDetails() {
        firebaseFirestore.collection("PUBLIC_DATA")
                .document("COURSE_FEE_AND_BANNER")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            registrationFee.setText("Registration Fee : "+task.getResult().getString("registration_fee"));
                            oneInstallmentPayment.setText("One Time Payment : "+task.getResult().getString("one_time_payment_fee"));
                            twoInstallmentPayment.setText("Two Time Payment : "+task.getResult().getString("two_installement_fee"));
                            monthlyPayment.setText("Monthly Payment : "+task.getResult().getString("monthly_payment_fee"));
                        } else {
                            Toast.makeText(CourseOfferedActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}