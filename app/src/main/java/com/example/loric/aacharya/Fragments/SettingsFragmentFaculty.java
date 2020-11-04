package com.example.loric.aacharya.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.DashboardPublic;
import com.example.loric.aacharya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragmentFaculty extends Fragment {
    FirebaseAuth firebaseAuth;
    private CircleImageView circleImageView;
    private TextView userName, userRollNo, userFathersName, userAddress, userContactNo, userCourseDetail;
    private FirebaseFirestore firebaseFirestore;
    private Dialog dialog;

    private Button logOutBtn;

    public SettingsFragmentFaculty() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings_faculty, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        initViews(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading_dialog_layout);
        dialog.setCancelable(false);
        setData();

        return view;
    }

    private void setData() {
        dialog.show();
        firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Glide.with(getContext()).load(task.getResult().getString("userProfile")).into(circleImageView);

                            if (task.getResult().getString("userName") != null) {
                                userName.setText(task.getResult().getString("userName"));
                            }
                            if (task.getResult().getString("userSubject") != null) {
                                userRollNo.setText(task.getResult().getString("userSubject"));
                            }
                            if (task.getResult().getString("userAddress") != null) {
                                userAddress.setText(task.getResult().getString("userAddress"));
                            }

                            if (task.getResult().getString("userContactNo") != null) {
                                userContactNo.setText(task.getResult().getString("userContactNo"));
                            }
                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();

                    }
                });

    }

    private void initViews(View view) {
        circleImageView = view.findViewById(R.id.profile_pic_user);
        userName = view.findViewById(R.id.name_tv);
        userRollNo = view.findViewById(R.id.rollno_tv);
        userFathersName = view.findViewById(R.id.fathersname_tv);
        userAddress = view.findViewById(R.id.address_tv);
        userContactNo = view.findViewById(R.id.contact_no_tv);
        userCourseDetail = view.findViewById(R.id.course_fee_detail_tv);
        logOutBtn = view.findViewById(R.id.logout_btn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), DashboardPublic.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

    }
}