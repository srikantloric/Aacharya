package com.example.loric.aacharya.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Adapters.FacultyChatUserListAdapter;
import com.example.loric.aacharya.DbQueries;
import com.example.loric.aacharya.R;

public class FChatFrag extends Fragment {



    public static FacultyChatUserListAdapter adapter;
    RecyclerView recyclerView;

    public FChatFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_f_chat, container, false);
        Dialog loadingDialog = new Dialog(getContext());
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.loading_dialog_layout);
        loadingDialog.show();
        recyclerView = view.findViewById(R.id.rv_faculty_chat_userlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        DbQueries.setChatUserListForStudents(getContext(), false,loadingDialog);
        adapter = new FacultyChatUserListAdapter(DbQueries.facultyChatUserModelList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}