package com.example.loric.aacharya.StudentsDashboard;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Adapters.VacancyAdapter;
import com.example.loric.aacharya.Models.VacancyModel;
import com.example.loric.aacharya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VacancyActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    VacancyAdapter adapter;
    List<VacancyModel> vacancyModelList;
    FirebaseFirestore firebaseFirestore;
    ImageView backBtn;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancy);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.rv_vacancy);
        backBtn = findViewById(R.id.back_btn);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_dialog_layout);
        dialog.setCancelable(false);
        dialog.show();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getDataFromServer();


    }

    private void getDataFromServer() {
        vacancyModelList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new VacancyAdapter(vacancyModelList, this);
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("VACANCY_MANAGER")
                .orderBy("time_stamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                vacancyModelList.add(new VacancyModel(snapshot.getString("vacancy_title"),
                                        snapshot.getString("vacancy_des"),
                                        snapshot.getDate("time_stamp"),
                                        snapshot.getString("vacancy_pdf_url"),
                                        snapshot.getBoolean("is_over")));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(VacancyActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }
}