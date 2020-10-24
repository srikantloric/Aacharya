package com.example.loric.aacharya.StudentsDashboard;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loric.aacharya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ComplaintBox extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private TextView backBtn;

    private Button submitBtn;
    private EditText messageBox;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_box);
        backBtn = findViewById(R.id.back_btn);
        submitBtn = findViewById(R.id.submit_complain_btn);
        messageBox = findViewById(R.id.message_box);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_dialog_layout);
        dialog.setCancelable(false);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComplain();
            }
        });

    }

    private void saveComplain() {
        dialog.show();
        if (TextUtils.isEmpty(messageBox.getText())) {
            messageBox.setError("Type something...");
        } else {
            Map<String, Object> complaintMessage = new HashMap<>();
            complaintMessage.put("message", messageBox.getText().toString());
            complaintMessage.put("complaint_by", firebaseAuth.getCurrentUser().getUid());
            complaintMessage.put("complaint_timestamp", FieldValue.serverTimestamp());
            firebaseFirestore.collection("COMPLAIN_BOX")
                    .document()
                    .set(complaintMessage)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ComplaintBox.this, "Submitted Successfully..", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ComplaintBox.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
        }


    }
}