package com.example.loric.aacharya.Fragments;

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
import com.example.loric.aacharya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragmentFaculty extends Fragment {


    private TextView facultyName, facultySubject;
    private CardView onlineClassBtn, attendanceBtn, personalDetailBtn;
    private CircleImageView circleImageView;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public HomeFragmentFaculty() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_faculty, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        initView(view);

        getServerData();


        return view;
    }

    private void initView(View view) {


        facultyName = view.findViewById(R.id.faculty_name_tv);
        facultySubject = view.findViewById(R.id.faculty_subject);
        circleImageView = view.findViewById(R.id.f_circleImageView);
    }

    private void getServerData() {
        firebaseFirestore.collection("USERS").document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            facultySubject.setVisibility(View.VISIBLE);
                            facultyName.setVisibility(View.VISIBLE);
                            circleImageView.setVisibility(View.VISIBLE);

                            String profileImageUrl = task.getResult().getString("userProfile");
                            if (circleImageView != null) {

                                Glide.with(getContext()).load(profileImageUrl).into(circleImageView);
                            }
                            facultyName.setText(task.getResult().getString("userName"));
                            facultySubject.setText("( " + task.getResult().getString("faculty_subject") + " )");
                        } else {
                            facultySubject.setVisibility(View.GONE);
                            facultyName.setVisibility(View.GONE);
                            circleImageView.setVisibility(View.GONE);
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}