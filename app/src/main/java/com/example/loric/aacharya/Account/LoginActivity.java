package com.example.loric.aacharya.Account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button loginBtn;
    private String userName, password;
    private EditText userNameView, userPasswordView;
    private FirebaseFirestore db;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
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
                            db.collection("USERS").document(mAuth.getCurrentUser().getUid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                boolean isStudent = (boolean) task.getResult().get("isStudent");
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
    }

    public void onStart() {
        super.onStart();
    }
}
