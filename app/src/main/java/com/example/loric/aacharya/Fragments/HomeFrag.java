package com.example.loric.aacharya.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.loric.aacharya.R;
import com.example.loric.aacharya.ScanQrCodeActivity;
import com.example.loric.aacharya.StudentsDashboard.ComplaintBox;
import com.example.loric.aacharya.StudentsDashboard.PersonalInfoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFrag extends Fragment {

    Button scanBtn;
    private CardView complainBoxBtn, personalInfoBtn;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String QrCodeServer;

    public HomeFrag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("QR_CODE")
                .document("qr_code_generation")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            QrCodeServer = task.getResult().getString("qr_code");
                            Log.d("joj", "onComplete: " + QrCodeServer);
                        } else {
                            Toast.makeText(getContext(), "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        scanBtn = view.findViewById(R.id.scan_btn);
        complainBoxBtn = view.findViewById(R.id.complain_box_btn);
        personalInfoBtn = view.findViewById(R.id.personal_info_btn);

        personalInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PersonalInfoActivity.class);
                startActivity(intent);
            }
        });


        complainBoxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ComplaintBox.class);
                startActivity(intent);
            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QrCodeServer==null){
                    Toast.makeText(getContext(), "Please Wait..Qr Processing", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getContext(), ScanQrCodeActivity.class);
                    intent.putExtra("QR_SERVER", QrCodeServer);
                    startActivity(intent);
                }
            }
        });

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0001);
        }
        return view;
    }
}