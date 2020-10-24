
package com.example.loric.aacharya.StudentsDashboard;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfoActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private TextView userName, userRollNo, userFathersName, userAddress, userContactNo, userCourseDetail;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Dialog dialog;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initViews();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_dialog_layout);
        dialog.setCancelable(false);
        setData();


    }

    private void setData() {
        dialog.show();
        firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            Glide.with(PersonalInfoActivity.this).load(task.getResult().getString("userProfile"));

                            if (task.getResult().getString("userName") != null) {
                                userName.setText(task.getResult().getString("userName"));
                            }

                            if (task.getResult().getString("userRollNo") != null) {
                                userRollNo.setText(task.getResult().getString("userRollNo"));
                            }

                            if (task.getResult().getString("userFathersName") != null) {
                                userFathersName.setText(task.getResult().getString("userFathersName"));
                            }

                            if (task.getResult().getString("userAddress") != null) {
                                userAddress.setText(task.getResult().getString("userAddress"));
                            }

                            if (task.getResult().getString("userContactNo") != null) {
                                userContactNo.setText(task.getResult().getString("userContactNo"));
                            }

                            if (task.getResult().getString("userCourseDetail") != null) {
                                userCourseDetail.setText(task.getResult().getString("userCourseDetail"));
                            }

                        } else {
                            Toast.makeText(PersonalInfoActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();

                    }
                });


    }

    private void initViews() {
        circleImageView = findViewById(R.id.profile_pic_user);
        userName = findViewById(R.id.name_tv);
        userRollNo = findViewById(R.id.rollno_tv);
        userFathersName = findViewById(R.id.fathersname_tv);
        userAddress = findViewById(R.id.address_tv);
        userContactNo = findViewById(R.id.contact_no_tv);
        userCourseDetail = findViewById(R.id.course_detail_tv);
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}