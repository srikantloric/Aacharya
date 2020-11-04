package com.example.loric.aacharya.StudentsDashboard;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Adapters.StudentNotesAdapter;
import com.example.loric.aacharya.Models.StudentNotesModel;
import com.example.loric.aacharya.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentsNotesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<StudentNotesModel> studentNotesModelList = new ArrayList<>();
    StudentNotesAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ListenerRegistration listenerRegistration;
    Dialog dialog;
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_notes);
        recyclerView = findViewById(R.id.rv_students_notes);
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_dialog_layout);
        dialog.setCancelable(false);
        dialog.show();

        adapter = new StudentNotesAdapter(studentNotesModelList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        getDataFromServer();


    }

    private void getDataFromServer() {

        String rs = getResources().getString(R.string.Rs);
        listenerRegistration = firebaseFirestore.collection("BOOKS_NOTES")
                .orderBy("time_stamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            studentNotesModelList.clear();
                            for (QueryDocumentSnapshot snapshot : value) {
                                if ((List<String>) snapshot.get("allowed_users") != null) {
                                    List<String> list = (List<String>) snapshot.get("allowed_users");

                                    if (list.contains(firebaseAuth.getCurrentUser().getUid())) {
                                        studentNotesModelList.add(new StudentNotesModel(snapshot.getString("book_title"),
                                                snapshot.getString("book_description"),
                                                rs + " " + snapshot.getLong("book_price") + "/-",
                                                snapshot.getString("book_img"),
                                                true));
                                    } else {
                                        studentNotesModelList.add(new StudentNotesModel(snapshot.getString("book_title"),
                                                snapshot.getString("book_description"),
                                                rs + " " + snapshot.getLong("book_price") + "/-",
                                                snapshot.getString("book_img"),
                                                false));
                                    }
                                } else {
                                    studentNotesModelList.add(new StudentNotesModel(snapshot.getString("book_title"),
                                            snapshot.getString("book_description"),
                                            rs + " " + snapshot.getLong("book_price") + "/-",
                                            snapshot.getString("book_img"),
                                            false));
                                }

                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(StudentsNotesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }


    @Override
    protected void onPause() {
        super.onPause();
        listenerRegistration.remove();
    }
}