package com.example.loric.aacharya.FacultyDashboard;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loric.aacharya.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FacultyAttendanceActivity extends AppCompatActivity {

    private CompactCalendarView compactCalendarView;
    private TextView dateView, presentedTv, totalClassesTv, absentedTv;
    private ImageView calLeftBtn, calRightBtn;
    private PieChart pieChart;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private List<Event> eventList = new ArrayList<>();

    private int totalAttendanceCount = 0;
    private int totalNoOfClasses = 0;

    private SimpleArcLoader loader;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_attendance);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_dialog_layout);
        dialog.setCancelable(false);
        dialog.show();

        compactCalendarView = findViewById(R.id.compactcalendar_view);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        dateView = findViewById(R.id.year_date);
        calLeftBtn = findViewById(R.id.calander_left_btn);
        calRightBtn = findViewById(R.id.calender_right_btn);
        loader = findViewById(R.id.loader);
        pieChart = findViewById(R.id.piechart);
        presentedTv = findViewById(R.id.presented_tv);
        totalClassesTv = findViewById(R.id.total_classes);
        absentedTv = findViewById(R.id.total_absented_days);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.shouldSelectFirstDayOfMonthOnScroll(false);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
                dateView.setText(format.format(firstDayOfNewMonth));
            }
        });

        calRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollRight();
            }
        });

        calLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollLeft();
            }
        });


        getAttendanceFromServer();

    }


    private long milliToTimeConverter(String date) {
        String[] parts = date.split("/");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTimeInMillis();
    }


    private void getAttendanceFromServer() {
        firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                .collection("MY_ATTENDANCE")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                totalAttendanceCount++;
                                Date attendanceDate = snapshot.getDate("attendance_time");
                                SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                                String date = sf.format(attendanceDate);
                                eventList.add(new Event(Color.GREEN, milliToTimeConverter(date)));
                            }
                            for (int i = 0; i < eventList.size(); i++) {
                                compactCalendarView.addEvent(eventList.get(i));
                            }
                            firebaseFirestore.collection("QR_CODE").document("TOTAL_CLASSES")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                Long total = task.getResult().getLong("total_classes_happened");
                                                totalNoOfClasses = Integer.parseInt(String.valueOf(total));
                                                presentedTv.setText("" + totalAttendanceCount);
                                                totalClassesTv.setText("" + totalNoOfClasses);
                                                String absent = String.valueOf(totalNoOfClasses - totalAttendanceCount);
                                                absentedTv.setText(absent);
                                                pieChart.addPieSlice(new PieModel("Total Classes", totalNoOfClasses, Color.parseColor("#4CAF50")));
                                                pieChart.addPieSlice(new PieModel("Present", totalAttendanceCount, Color.parseColor("#03A9F4")));
                                                pieChart.addPieSlice(new PieModel("Absent", totalNoOfClasses - totalAttendanceCount, Color.parseColor("#F14B17")));
                                                pieChart.startAnimation();

                                            } else {
                                                Toast.makeText(FacultyAttendanceActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            dialog.dismiss();
                            Toast.makeText(FacultyAttendanceActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



}