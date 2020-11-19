package com.example.loric.aacharya.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Adapters.FacultyChatUserListAdapter;
import com.example.loric.aacharya.DbQueries;
import com.example.loric.aacharya.Models.FacultyChatUserModel;
import com.example.loric.aacharya.PublicChatRoom;
import com.example.loric.aacharya.R;

import java.util.ArrayList;
import java.util.List;

public class SChatFragment extends Fragment {
    public static FacultyChatUserListAdapter adapter;
    RecyclerView recyclerView;
    private List<String> stringList = new ArrayList<>();
    private CardView groupDiscussBtn;

    private List<FacultyChatUserModel> facultyChatUserModelList = new ArrayList<>();

    public SChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_s_chat, container, false);

        Dialog loadingDialog = new Dialog(getContext());
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.loading_dialog_layout);
        loadingDialog.show();

        groupDiscussBtn = view.findViewById(R.id.grp_discussion_schat_btn);

        groupDiscussBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PublicChatRoom.class);
                startActivity(intent);
            }
        });
        recyclerView = view.findViewById(R.id.rv_faculty_chat_userlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        DbQueries.setChatUserListForFaculty(getContext(), false,loadingDialog);
        adapter = new FacultyChatUserListAdapter(DbQueries.facultyChatUserModelList);
        recyclerView.setAdapter(adapter);
        return view;
    }
}