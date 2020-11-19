package com.example.loric.aacharya;


import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.loric.aacharya.Fragments.FChatFrag;
import com.example.loric.aacharya.Fragments.SChatFragment;
import com.example.loric.aacharya.Models.FacultyChatUserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DbQueries {

    public static final List<FacultyChatUserModel> facultyChatUserModelList = new ArrayList<>();
    final static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    final static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static ListenerRegistration listenerRegistration;
    public static boolean isQrScannedAndMatched = false;





    public static void setChatUserListForFaculty(final Context context, boolean removeListener, final Dialog loading) {
        if (removeListener) {
            if (listenerRegistration != null) {
                listenerRegistration.remove();
            }
            loading.dismiss();
        } else {
            listenerRegistration = firebaseFirestore.collection("USERS")
                    .whereEqualTo("isStudent", true)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.getDocuments() != null) {
                                facultyChatUserModelList.clear();
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    facultyChatUserModelList.add(new FacultyChatUserModel(snapshot.getString("userName"), snapshot.getString("userProfile"), "science", snapshot.getId()));
                                }
                                SChatFragment.adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            loading.dismiss();
                        }
                    });


        }
    }

    public static void setChatUserListForStudents(final Context context, boolean removeListener, final Dialog loading) {
        if (removeListener) {
            if (listenerRegistration != null) {
                listenerRegistration.remove();
            }
            loading.dismiss();
        } else {
            listenerRegistration = firebaseFirestore.collection("USERS")
                    .whereEqualTo("isStudent", false)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.getDocuments() != null) {
                                facultyChatUserModelList.clear();
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    facultyChatUserModelList.add(new FacultyChatUserModel(snapshot.getString("userName"), snapshot.getString("userProfile"), "science", snapshot.getId()));
                                }
                                FChatFrag.adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            loading.dismiss();
                        }
                    });


        }
    }


}
