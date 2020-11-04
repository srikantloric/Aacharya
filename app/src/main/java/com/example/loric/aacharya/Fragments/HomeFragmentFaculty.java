package com.example.loric.aacharya.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.FacultyDashboard.FacultyAttendanceActivity;
import com.example.loric.aacharya.FacultyDashboard.OnlineClassFaculty;
import com.example.loric.aacharya.R;
import com.example.loric.aacharya.ScanQrCodeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.loric.aacharya.DbQueries.isQrScannedAndMatched;

public class HomeFragmentFaculty extends Fragment {


    private TextView facultyName, facultySubject;
    Button scanBtn;
    private CircleImageView circleImageView;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    String QrCodeServer;
    private CardView onlineClassBtn, attendanceBtn;
    private String teacherName, imageUrl, teacherSubject;


    public HomeFragmentFaculty() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_faculty, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        loadQrDataFromServer();

        initView(view);

        getServerData();


        return view;
    }

    private void loadQrDataFromServer() {
        firebaseFirestore.collection("QR_CODE")
                .document("qr_code_generation")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            QrCodeServer = task.getResult().getString("qr_code");
                        } else {
                            Toast.makeText(getContext(), "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initView(View view) {


        facultyName = view.findViewById(R.id.faculty_name_tv);
        facultySubject = view.findViewById(R.id.faculty_subject);
        circleImageView = view.findViewById(R.id.f_circleImageView);
        onlineClassBtn = view.findViewById(R.id.f_online_class_btn);
        attendanceBtn = view.findViewById(R.id.f_attendance_btn);
        scanBtn = view.findViewById(R.id.scan_btn);


        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QrCodeServer == null) {
                    Toast.makeText(getContext(), "Please Wait..Qr Processing", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), ScanQrCodeActivity.class);
                    intent.putExtra("QR_SERVER", QrCodeServer);
                    startActivity(intent);
                }
            }
        });

        attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext(), FacultyAttendanceActivity.class);

                startActivity(intent);

            }
        });
        onlineClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (teacherName != null && imageUrl != null && teacherSubject != null) {
                    Intent intent = new Intent(getContext(), OnlineClassFaculty.class);
                    intent.putExtra("IMAGE_URL", imageUrl);
                    intent.putExtra("TEACHER_NAME", teacherName);
                    intent.putExtra("TEACHER_SUBJECT", teacherSubject);

                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Please wait..", Toast.LENGTH_SHORT).show();
                }

            }
        });

        SharedPreferences sp = getContext().getSharedPreferences("com.example.loric.aacharya", Context.MODE_PRIVATE);
        String savedDateTime = sp.getString("SavedDate", "");
        if ("".equals(savedDateTime)) {
            scanBtn.setEnabled(true);
            scanBtn.setAlpha((float) 1);
        } else {
            String dateStringNow = DateFormat.format("MM/dd/yyyy", new Date((new Date()).getTime())).toString();
            if (savedDateTime.equals(dateStringNow)) {
                scanBtn.setEnabled(false);
                scanBtn.setAlpha((float) 0.6);
                scanBtn.setText("Attended Successfully");

            } else {
                scanBtn.setEnabled(true);
                scanBtn.setAlpha((float) 1);
            }
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0001);
        }


    }

    private void getServerData() {
        firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            facultySubject.setVisibility(View.VISIBLE);
                            facultyName.setVisibility(View.VISIBLE);
                            circleImageView.setVisibility(View.VISIBLE);

                            String profileImageUrl = task.getResult().getString("userProfile");
                            imageUrl = task.getResult().getString("userProfile");
                            teacherName = task.getResult().getString("userName");
                            teacherSubject = task.getResult().getString("faculty_subject");
                            if (circleImageView != null && isAdded()) {

                                Glide.with(getContext()).load(profileImageUrl).into(circleImageView);
                            }
                            facultyName.setText(task.getResult().getString("userName"));
                            facultySubject.setText("( " + task.getResult().getString("faculty_subject") + " )");
                        } else {
                            facultySubject.setVisibility(View.GONE);
                            facultyName.setVisibility(View.GONE);
                            circleImageView.setVisibility(View.GONE);
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        Map<String, Object> attendance = new HashMap<>();
        attendance.put("attendance_time", FieldValue.serverTimestamp());

        if (isQrScannedAndMatched) {
            firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                    .collection("MY_ATTENDANCE")
                    .document()
                    .set(attendance)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                String dateString = DateFormat.format("MM/dd/yyyy", new Date((new Date()).getTime())).toString();
                                SharedPreferences sp = getContext().getSharedPreferences("com.example.loric.aacharya", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("SavedDate", dateString);
                                editor.commit();
                                scanBtn.setEnabled(false);
                                scanBtn.setAlpha((float) 0.6);
                                scanBtn.setText("Attended Successfully");
                                Toast.makeText(getContext(), "Attendance Updated Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Attendance Update Failed Please Scan again...", Toast.LENGTH_SHORT).show();
                            }
                            isQrScannedAndMatched = false;

                        }
                    });
        }

    }
}