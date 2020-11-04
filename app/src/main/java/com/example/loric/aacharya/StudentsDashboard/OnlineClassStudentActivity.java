package com.example.loric.aacharya.StudentsDashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Adapters.OnlineClassStudentAdapter;
import com.example.loric.aacharya.Models.OnlineClassStudentModel;
import com.example.loric.aacharya.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OnlineClassStudentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    OnlineClassStudentAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_class);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.rv_online_class_students);
        backBtn = findViewById(R.id.back_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        getDataFromServer();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void getDataFromServer() {
        List<OnlineClassStudentModel> onlineClassStudentModelList = new ArrayList<>();
        adapter = new OnlineClassStudentAdapter(this, onlineClassStudentModelList);
        recyclerView.setAdapter(adapter);
        firebaseFirestore.collection("ONLINE_CLASSES")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            onlineClassStudentModelList.clear();
                            for (QueryDocumentSnapshot snapshot : value) {
                                onlineClassStudentModelList.add(new OnlineClassStudentModel(snapshot.getString("teacher_name"),
                                        "( " + snapshot.getString("teacher_subject") + " )",
                                        snapshot.getString("teacher_pic"),
                                        snapshot.getString("class_description"),
                                        snapshot.getString("meeting_code"),
                                        "Scheduled at " + snapshot.getString("class_time") + " " + snapshot.getString("class_date")
                                        , snapshot.getId()));
                            }

                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(OnlineClassStudentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}