package com.example.loric.aacharya.Account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loric.aacharya.FacultyDashboard.DashboardFaculty;
import com.example.loric.aacharya.R;
import com.example.loric.aacharya.StudentsDashboard.StudentDashboard;
import com.example.loric.aacharya.UserPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button loginBtn;
    private String userName, password;
    private EditText userNameView, userPasswordView;
    private FirebaseFirestore db;
    private Dialog progressDialog;
    private TextView titleLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        titleLoginPage.setText(getIntent().getStringExtra("TITLE_USER"));
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextValidation();
            }
        });


        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.loading_dialog_layout);
        progressDialog.setCancelable(false);


    }

    private void editTextValidation() {
        if (!TextUtils.isEmpty(userNameView.getText())) {
            if (!TextUtils.isEmpty(userPasswordView.getText())) {
                userName = userNameView.getText().toString();
                password = userPasswordView.getText().toString();
                loginUser();
            } else {
                userPasswordView.requestFocus();
                userPasswordView.setError("Please enter password..");

            }
        } else {

            userNameView.requestFocus();
            userNameView.setError("Please enter userId..");
        }
    }

    private void loginUser() {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /////User Detail Check/////
                            db.collection("USERS").document(mAuth.getCurrentUser().getUid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                String deviceName = "";
                                                boolean isLoggedIn = (boolean) task.getResult().get("isLoggedIn");
                                                boolean isStudent = (boolean) task.getResult().get("isStudent");
                                                if (task.getResult().getString("loggedDeviceName") != null) {
                                                    deviceName = task.getResult().getString("loggedDeviceName");
                                                }

                                                if (isLoggedIn) {
                                                    mAuth.signOut();
                                                    progressDialog.dismiss();
                                                    Toast.makeText(LoginActivity.this, "User Already LoggedIn in " + deviceName, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    ///////LoginCheck/////
                                                    FirebaseMessaging.getInstance().getToken()
                                                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<String> task) {
                                                                    if (!task.isSuccessful()) {
                                                                        Toast.makeText(LoginActivity.this, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    } else {
                                                                        Map<String, Object> deviceLoginDetail = new HashMap<>();
                                                                        deviceLoginDetail.put("loggedDeviceName", android.os.Build.MODEL);
                                                                        deviceLoginDetail.put("isLoggedIn", true);
                                                                        deviceLoginDetail.put("FCMToken", task.getResult());
                                                                        db.collection("USERS").document(mAuth.getCurrentUser().getUid())
                                                                                .update(deviceLoginDetail)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            if (isStudent) {
                                                                                                UserPreferences.setDefaultsBoolean("isStudent", true, LoginActivity.this);
                                                                                                Intent intent = new Intent(LoginActivity.this, StudentDashboard.class);
                                                                                                startActivity(intent);
                                                                                            } else {
                                                                                                UserPreferences.setDefaultsBoolean("isStudent", false, LoginActivity.this);
                                                                                                Intent intent = new Intent(LoginActivity.this, DashboardFaculty.class);
                                                                                                startActivity(intent);
                                                                                            }
                                                                                            finish();
                                                                                            progressDialog.dismiss();
                                                                                        } else {

                                                                                        }
                                                                                    }
                                                                                });
                                                                    }

                                                                }
                                                            });
                                                    /////LoginCheck/////
                                                }
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void initViews() {
        userNameView = findViewById(R.id.username_edittext);
        userPasswordView = findViewById(R.id.password_edittext);
        loginBtn = findViewById(R.id.submit_btn);
        titleLoginPage = findViewById(R.id.title_login_page);
    }

    public void onStart() {
        super.onStart();
    }
}
