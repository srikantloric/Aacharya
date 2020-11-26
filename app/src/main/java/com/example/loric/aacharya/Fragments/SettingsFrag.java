package com.example.loric.aacharya.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

public class SettingsFrag extends Fragment {
    TextView logOutBtn;
    FirebaseAuth firebaseAuth;
    private CircleImageView circleImageView;
    private TextView userName, userRollNo, userFathersName, userAddress, userContactNo, userCourseDetail;
    private FirebaseFirestore firebaseFirestore;
    private Dialog dialog;
    private CardView nameContainer, addressContainer;


    public SettingsFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading_dialog_layout);
        dialog.setCancelable(false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        logOutBtn = view.findViewById(R.id.logout_btn);
        circleImageView = view.findViewById(R.id.profile_pic_user);
        userName = view.findViewById(R.id.name_tv);
        userRollNo = view.findViewById(R.id.rollno_tv);
        userFathersName = view.findViewById(R.id.fathersname_tv);
        userAddress = view.findViewById(R.id.address_tv);
        userContactNo = view.findViewById(R.id.contact_no_tv);
        userCourseDetail = view.findViewById(R.id.course_fee_detail_tv);
        nameContainer = view.findViewById(R.id.name_container);
        addressContainer = view.findViewById(R.id.address_container);

        nameContainer.animate().translationY(-500);
        addressContainer.animate().translationY(-500);
        logOutBtn.animate().translationY(500);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("SignOut")
                        .setMessage("Do you really want to logout?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .update("isLoggedIn", false)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseAuth.getInstance().signOut();
                                                    Intent intent = new Intent(getContext(), DashboardPublic.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                } else {
                                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

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
                                userName.setText(task.getResult().getString("userName").toUpperCase());
                            }
                            if (task.getResult().getString("userRollNo") != null) {
                                userRollNo.setText("Roll no: " + task.getResult().getString("userRollNo"));
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

                        nameContainer.setVisibility(View.VISIBLE);
                        addressContainer.setVisibility(View.VISIBLE);
                        logOutBtn.setVisibility(View.VISIBLE);
                        nameContainer.animate().translationY(0);
                        addressContainer.animate().translationY(0);
                        logOutBtn.animate().translationY(0);
                        dialog.dismiss();

                    }
                });

    }
}