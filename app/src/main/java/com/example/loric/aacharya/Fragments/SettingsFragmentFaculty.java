package com.example.loric.aacharya.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.loric.aacharya.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragmentFaculty extends Fragment {
    FirebaseAuth firebaseAuth;

    public SettingsFragmentFaculty() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings_faculty, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        getActivity().finish();
        return view;
    }
}