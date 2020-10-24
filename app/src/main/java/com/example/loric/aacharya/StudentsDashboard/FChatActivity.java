package com.example.loric.aacharya.StudentsDashboard;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.Adapters.FChatMessagingAdapter;
import com.example.loric.aacharya.Models.FChatMessageModel;
import com.example.loric.aacharya.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FChatActivity extends AppCompatActivity {

    private ImageView backBtn;
    private CircleImageView circleImageView;
    private TextView userName;
    private String USER_NAME, USER_PROFILE, USER_ID;
    private RecyclerView recyclerView;
    private FChatMessagingAdapter adapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private List<FChatMessageModel> fChatMessageModelList = new ArrayList<>();

    private EditText messageEditText;
    private ImageView sentBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_f_chat);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        initView();
        setHeader();
        chatHandler();
        loadMessages();
        sentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(messageEditText.getText())) {
                    Toast.makeText(FChatActivity.this, "Cannot sent empty text..", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(messageEditText.getText().toString());
                    messageEditText.setText("");
                }
            }
        });


    }

    private String setChatNode(String S_UID, String R_UID) {
        String finalNodeId = null;

        if (S_UID.compareTo(R_UID) < 1) {
            finalNodeId = S_UID + R_UID;
        } else if (R_UID.compareTo(S_UID) < 1) {
            finalNodeId = R_UID + S_UID;
        }
        return finalNodeId;
    }

    private void sendMessage(String messageEditText) {
        Map<String, Object> message = new HashMap<>();
        message.put("messageText", messageEditText);
        message.put("sender", firebaseAuth.getCurrentUser().getUid());
        message.put("receiver", USER_ID);
        message.put("sentAt", FieldValue.serverTimestamp());
        firebaseFirestore.collection("MESSAGE")
                .document(setChatNode(firebaseAuth.getCurrentUser().getUid(), USER_ID))
                .collection("messages")
                .document()
                .set(message);


    }

    private void loadMessages() {
        firebaseFirestore.collection("MESSAGE")
                .document(setChatNode(firebaseAuth.getCurrentUser().getUid(), USER_ID))
                .collection("messages")
                .orderBy("sentAt", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.getDocuments() != null) {
                            fChatMessageModelList.clear();
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                if (snapshot.getString("sender").equals(firebaseAuth.getCurrentUser().getUid())) {
                                    fChatMessageModelList.add(new FChatMessageModel(1, snapshot.getString("messageText"), snapshot.getDate("sentAt")));
                                } else {
                                    fChatMessageModelList.add(new FChatMessageModel(0, snapshot.getString("messageText"), snapshot.getDate("sentAt")));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(FChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void chatHandler() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new FChatMessagingAdapter(fChatMessageModelList);
        recyclerView.setAdapter(adapter);


    }

    private void setHeader() {
        USER_NAME = getIntent().getStringExtra("USER_NAME");
        USER_PROFILE = getIntent().getStringExtra("USER_PROFILE");
        USER_ID = getIntent().getStringExtra("USER_ID");
        Glide.with(this).load(USER_PROFILE).into(circleImageView);
        userName.setText(USER_NAME);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        backBtn = findViewById(R.id.back_btn_fchat);
        circleImageView = findViewById(R.id.user_profile_view);
        userName = findViewById(R.id.user_name);
        recyclerView = findViewById(R.id.rv_fchat);
        sentBtn = findViewById(R.id.sentBtn);
        messageEditText = findViewById(R.id.messageInput);
    }
}