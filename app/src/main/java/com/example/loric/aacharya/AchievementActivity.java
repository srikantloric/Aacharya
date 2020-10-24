package com.example.loric.aacharya;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Adapters.OurAchieverAdapter;
import com.example.loric.aacharya.Models.OurAchieverModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AchievementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private OurAchieverAdapter adapter;
    private Dialog dialog;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        firebaseFirestore = FirebaseFirestore.getInstance();
        dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_dialog_layout);
        dialog.show();
        recyclerView = findViewById(R.id.rv_achiever);
        backBtn = findViewById(R.id.back_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        loadAchievementData();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void loadAchievementData() {
        final List<OurAchieverModel> ourAchieverModelList = new ArrayList<>();
        adapter = new OurAchieverAdapter(ourAchieverModelList);
        recyclerView.setAdapter(adapter);
        firebaseFirestore.collection("INSTITUTE_ACHIEVER").orderBy("index")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                ourAchieverModelList.add(new OurAchieverModel(snapshot.getString("student_profile"), snapshot.getString("student_name"), "Current Position : " + snapshot.getString("student_post")));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(AchievementActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }
}