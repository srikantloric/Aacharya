package com.example.loric.aacharya.FacultyDashboard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Adapters.OnlineClassFacultyAdapter;
import com.example.loric.aacharya.Models.OnlineClassFacultyModel;
import com.example.loric.aacharya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OnlineClassFaculty extends AppCompatActivity {

    private Button datePicker, timePicker, scheduleBtn;
    private TextView dateTv, timeTv;
    private RecyclerView recyclerView;
    private OnlineClassFacultyAdapter adapter;
    private ImageView backBtn;
    private EditText description, meetingCode;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_class_faculty);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        datePicker = findViewById(R.id.date_lc);
        timePicker = findViewById(R.id.time_lc);
        description = findViewById(R.id.about_class_tv);
        meetingCode = findViewById(R.id.meeting_code_tv);
        scheduleBtn = findViewById(R.id.make_schedule_btn);
        backBtn = findViewById(R.id.back_btn);

        dateTv = findViewById(R.id.date_tv);
        timeTv = findViewById(R.id.incoming_time_tv);
        recyclerView = findViewById(R.id.rv_online_class_faculty);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(description.getText())) {
                    if (!TextUtils.isEmpty(meetingCode.getText())) {
                        makeSchedule();

                    } else {
                        meetingCode.requestFocus();
                        meetingCode.setError("Please enter meeting code..");
                    }
                } else {
                    description.requestFocus();
                    description.setError("Put some description of class...");
                }

            }
        });

        getServerData();


        Calendar calendar2 = Calendar.getInstance();

        int YEAR = calendar2.get(Calendar.YEAR);
        int MONTH = calendar2.get(Calendar.MONTH);
        int DATE = calendar2.get(Calendar.DATE);
        calendar2.set(Calendar.YEAR, YEAR);
        calendar2.set(Calendar.MONTH, MONTH);
        calendar2.set(Calendar.DATE, DATE);
        CharSequence charSequence = DateFormat.format("EEEE, dd-MM-yyyy", calendar2);
        dateTv.setText(charSequence);


        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();

            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();

            }
        });
    }

    private void getServerData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        List<OnlineClassFacultyModel> onlineClassFacultyModelList = new ArrayList<>();
        adapter = new OnlineClassFacultyAdapter(onlineClassFacultyModelList);
        recyclerView.setAdapter(adapter);
        firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                .collection("ONLINE_CLASSES")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            onlineClassFacultyModelList.clear();
                            for (QueryDocumentSnapshot snapshot : value) {
                                onlineClassFacultyModelList.add(new OnlineClassFacultyModel(snapshot.getString("class_date"), snapshot.getString("class_time"), snapshot.getBoolean("isLive"), snapshot.getId()));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(OnlineClassFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24hrsSys = DateFormat.is24HourFormat(this);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hourOfDay);
                calendar1.set(Calendar.MINUTE, minute);

                CharSequence charSequence = DateFormat.format("h:mm a", calendar1);

                timeTv.setText(charSequence);
            }
        }, HOUR, MINUTE, is24hrsSys);
        timePickerDialog.show();


    }

    private void setDate() {
        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Toast.makeText(OnlineClassFaculty.this, "" + year + month + dayOfMonth, Toast.LENGTH_SHORT).show();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, dayOfMonth);
                CharSequence charSequence = DateFormat.format("EEEE, dd-MM-yyyy", calendar1);
                dateTv.setText(charSequence);

            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();

    }

    private void makeSchedule() {
        Map<String, Object> scheduleData = new HashMap<>();
        scheduleData.put("timestamp", FieldValue.serverTimestamp());
        scheduleData.put("class_time", timeTv.getText());
        scheduleData.put("class_date", dateTv.getText());
        scheduleData.put("class_description", "" + description.getText());
        scheduleData.put("meeting_code", "" + meetingCode.getText());
        scheduleData.put("isLive", false);
        scheduleData.put("teacher_name",getIntent().getStringExtra("TEACHER_NAME"));
        scheduleData.put("teacher_pic",getIntent().getStringExtra("IMAGE_URL"));
        scheduleData.put("teacher_subject",getIntent().getStringExtra("TEACHER_SUBJECT"));


        String uniqueId = UUID.randomUUID().toString();

        firebaseFirestore.collection("ONLINE_CLASSES")
                .document(uniqueId)
                .set(scheduleData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                                    .collection("ONLINE_CLASSES")
                                    .document(uniqueId)
                                    .set(scheduleData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(OnlineClassFaculty.this, "Scheduled Success", Toast.LENGTH_SHORT).show();
                                                meetingCode.setText("");
                                                description.setText("");
                                            } else {
                                                Toast.makeText(OnlineClassFaculty.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            Toast.makeText(OnlineClassFaculty.this, "Scheduled Success", Toast.LENGTH_SHORT).show();
                            meetingCode.setText("");
                            description.setText("");
                        } else {
                            Toast.makeText(OnlineClassFaculty.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}