package com.example.loric.aacharya;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Adapters.FeeDetailAdapter;
import com.example.loric.aacharya.Models.FeeDetailModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeeDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FeeDetailAdapter adapter;
    private List<FeeDetailModel> feeDetailModelList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private TextView courseDetail, courseFeeDetail, feePaidTotal, feeDueTotal;
    private ImageView backBtn;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_details);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading_dialog_layout);
        dialog.setCancelable(false);
        dialog.show();

        recyclerView = findViewById(R.id.rv_fee_detail);
        courseFeeDetail = findViewById(R.id.course_fee_detail_tv);
        courseDetail = findViewById(R.id.course_detail_tv);
        feeDueTotal = findViewById(R.id.fee_due_total);
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        feePaidTotal = findViewById(R.id.feePaidTotal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new FeeDetailAdapter(feeDetailModelList);
        recyclerView.setAdapter(adapter);

        loadServerData();
    }

    private void loadServerData() {
        firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Long courseFee = task.getResult().getLong("userCourseFee");
                            String userCourseDetail = task.getResult().getString("userCourseDetail");
                            courseDetail.setText(userCourseDetail);
                            courseFeeDetail.setText("Rs." + courseFee);
                            firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                                    .collection("PAYMENT_HISTORY")
                                    .orderBy("payment_date", Query.Direction.DESCENDING)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                long feePaid = 0;
                                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                                    Long payAmount = snapshot.getLong("paid_amount");
                                                    Date date = snapshot.getDate("payment_date");
                                                    feePaid = feePaid + payAmount;
                                                    long dueAmount = (courseFee - payAmount);
                                                    feeDetailModelList.add(new FeeDetailModel("Rs. " + payAmount, "Rs. " + dueAmount, date));
                                                }
                                                feeDueTotal.setText("Rs. " + (courseFee - feePaid));
                                                feePaidTotal.setText("Rs. " + feePaid);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                Toast.makeText(FeeDetails.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            dialog.dismiss();
                            Toast.makeText(FeeDetails.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}