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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.loric.aacharya.FeeDetails;
import com.example.loric.aacharya.PDFViewerActivity;
import com.example.loric.aacharya.R;
import com.example.loric.aacharya.ScanQrCodeActivity;
import com.example.loric.aacharya.StudentsDashboard.ComplaintBox;
import com.example.loric.aacharya.StudentsDashboard.OnlineClassStudentActivity;
import com.example.loric.aacharya.StudentsDashboard.PersonalInfoActivity;
import com.example.loric.aacharya.StudentsDashboard.StudentsNotesActivity;
import com.example.loric.aacharya.StudentsDashboard.WeeklyTestActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.loric.aacharya.DbQueries.isQrScannedAndMatched;

public class HomeFrag extends Fragment {

    Button scanBtn;
    private CardView complainBoxBtn,resultBtn, personalInfoBtn, feeDetail, onlineClass, weeklyTest, jobVacancy, studentNotes,mathsFormulas,currentAffairs;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String QrCodeServer;
    private String formulaUrl,vacanciesUrl,currentAffairUrl,resultUrl;

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
                            vacanciesUrl = task.getResult().getString("maths_formula");

                        } else {
                            Toast.makeText(getContext(), "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        firebaseFirestore.collection("PDF_CENTER")
                .document("PDF_DATA")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            formulaUrl = task.getResult().getString("maths_formulas_pdf");
                            formulaUrl = task.getResult().getString("vacancies_pdf");
                            currentAffairUrl = task.getResult().getString("current_affair_pdf");
                            resultUrl = task.getResult().getString("result_pdf");
                        } else {
                            Toast.makeText(getContext(), "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        scanBtn = view.findViewById(R.id.scan_btn);
        complainBoxBtn = view.findViewById(R.id.complain_box_btn);
        personalInfoBtn = view.findViewById(R.id.personal_info_btn);
        feeDetail = view.findViewById(R.id.fee_detail);
        onlineClass = view.findViewById(R.id.online_class_btn);
        weeklyTest = view.findViewById(R.id.weekly_test);
        jobVacancy = view.findViewById(R.id.job_vacancy);
        studentNotes = view.findViewById(R.id.notes_student);
        mathsFormulas = view.findViewById(R.id.mathsFormulaBtn);
        resultBtn = view.findViewById(R.id.result_btn);
        currentAffairs = view.findViewById(R.id.current_affairs);

        resultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!resultUrl.equals("")){
                    Intent intent = new Intent(getContext(), PDFViewerActivity.class);
                    intent.putExtra("BOOK_URL",resultUrl);
                    intent.putExtra("HEADER_TITLE","Results");
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "Please Wait..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        currentAffairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentAffairUrl.equals("")){
                    Intent intent = new Intent(getContext(), PDFViewerActivity.class);
                    intent.putExtra("BOOK_URL",currentAffairUrl);
                    intent.putExtra("HEADER_TITLE","Current Affairs");
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "Please Wait..", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mathsFormulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!formulaUrl.equals("")){
                    Intent intent = new Intent(getContext(), PDFViewerActivity.class);
                    intent.putExtra("BOOK_URL",formulaUrl);
                    intent.putExtra("HEADER_TITLE","Maths Formulas");
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "Please Wait..", Toast.LENGTH_SHORT).show();
                }

            }
        });

        jobVacancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!vacanciesUrl.equals("")){
                    Intent intent = new Intent(getContext(), PDFViewerActivity.class);
                    intent.putExtra("BOOK_URL",vacanciesUrl);
                    intent.putExtra("HEADER_TITLE","Vacancies Details");
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "Please Wait..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        studentNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StudentsNotesActivity.class);
                startActivity(intent);
            }
        });

        weeklyTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WeeklyTestActivity.class);
                startActivity(intent);
            }
        });

        onlineClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OnlineClassStudentActivity.class);
                startActivity(intent);
            }
        });

        feeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FeeDetails.class);
                startActivity(intent);
            }
        });


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
                if (QrCodeServer == null) {
                    Toast.makeText(getContext(), "Please Wait..Qr Processing", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), ScanQrCodeActivity.class);
                    intent.putExtra("QR_SERVER", QrCodeServer);
                    startActivity(intent);
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
        return view;
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