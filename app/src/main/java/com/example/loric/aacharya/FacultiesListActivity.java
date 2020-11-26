package com.example.loric.aacharya;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Adapters.FacultyListAdapter;
import com.example.loric.aacharya.Models.FacultyListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FacultiesListActivity extends AppCompatActivity {

    private ImageView backBtn;
    private RecyclerView recyclerView;
    private FacultyListAdapter adapter;
    private List<FacultyListModel> facultyListModelList =  new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculties_list);
        firebaseFirestore = FirebaseFirestore.getInstance();
        /////loading dialog//
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog_layout);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        /////loading dialog//


        viewInit();
        loadDataFromServer();


    }

    private void loadDataFromServer() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        adapter = new FacultyListAdapter(facultyListModelList);
        recyclerView.setAdapter(adapter);
        firebaseFirestore.collection("PUBLIC_DATA")
                .document("FACULTY_LIST")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (int i = 1; i < task.getResult().getLong("type_count") + 1; i++) {
                                String title_text = task.getResult().getString("type_" + i + "_title");
                                facultyListModelList.add(new FacultyListModel(0, title_text));
                                facultyListModelList.add(new FacultyListModel(0, ""));
                                for (int x = 1; x < task.getResult().getLong("type_" + i + "_total_teachers")+1; x++) {
                                    String faculty_image = task.getResult().getString("type_" + i + "_teacher_" + x + "_image");
                                    String faculty_name = task.getResult().getString("type_" + i + "_teacher_" + x + "_name");
                                    facultyListModelList.add(new FacultyListModel(1, faculty_image, faculty_name));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(FacultiesListActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });


    }

    private void viewInit() {
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.faculties_list_rv);

    }
}