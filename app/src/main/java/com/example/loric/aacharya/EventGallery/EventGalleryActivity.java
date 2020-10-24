package com.example.loric.aacharya.EventGallery;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Adapters.EventGalleryAdapter;
import com.example.loric.aacharya.Models.EventGalleryModel;
import com.example.loric.aacharya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EventGalleryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private EventGalleryAdapter adapter;
    private List<EventGalleryModel> eventGalleryModelList = new ArrayList<>();
    private Dialog dialog;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_gallary);
        recyclerView = findViewById(R.id.rv_event_gallery);
        backBtn = findViewById(R.id.back_btn);
        firebaseFirestore = FirebaseFirestore.getInstance();


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_dialog_layout);
        dialog.setCancelable(false);
        dialog.show();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        ServerDataLoading();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private void ServerDataLoading() {
        adapter = new EventGalleryAdapter(eventGalleryModelList);
        recyclerView.setAdapter(adapter);
        firebaseFirestore.collection("PUBLIC_DATA")
                .document("EVENT_GALLERY")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            for (int i = 0; i < task.getResult().getLong("total_events"); i++) {
                                eventGalleryModelList.add(new EventGalleryModel(0, task.getResult().getString("event_" + i + "_name")));
                                eventGalleryModelList.add(new EventGalleryModel(0, ""));
                                Log.d("kok", "onComplete: " + task.getResult().getString("event_" + i + "_name"));

                                for (int x = 0; x < task.getResult().getLong("event_" + i + "_total_photos"); x++) {
                                    eventGalleryModelList.add(new EventGalleryModel(1, task.getResult().getString("event_" + i + "_photo_" + x)));
                                }

                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(EventGalleryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }
}