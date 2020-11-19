package com.example.loric.aacharya.StudentsDashboard;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.Adapters.FChatMessagingAdapter;
import com.example.loric.aacharya.ImageResizer;
import com.example.loric.aacharya.Models.FChatMessageModel;
import com.example.loric.aacharya.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class FChatActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
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
    private ImageView sentBtn, attachBtn;
    private ConstraintLayout parentLayout;
    private Uri filePath;

    private Button sendImageBtn;
    private ImageView selectImageBtn;
    private TextView tapToSelectText;
    private StorageReference mStorageRef;
    private ProgressBar uploadProgress;

    private Dialog selectImageDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_chat);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        selectImageDialog = new Dialog(this);
        selectImageDialog.setContentView(R.layout.image_picker_dialog_layout);
        selectImageDialog.setCancelable(true);
        selectImageBtn = selectImageDialog.findViewById(R.id.image_select);
        sendImageBtn = selectImageDialog.findViewById(R.id.send_btn);
        uploadProgress = selectImageDialog.findViewById(R.id.upload_progress);
        tapToSelectText = selectImageDialog.findViewById(R.id.tap_to_select);


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
                    sendMessage(messageEditText.getText().toString(), false);
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

    private void sendMessage(String messageEditText, boolean isImage) {
        Map<String, Object> message = new HashMap<>();
        message.put("messageText", messageEditText);
        message.put("sender", firebaseAuth.getCurrentUser().getUid());
        message.put("receiver", USER_ID);
        message.put("sentAt", FieldValue.serverTimestamp());
        message.put("isImage", isImage);
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
                                    if (snapshot.getBoolean("isImage")) {
                                        fChatMessageModelList.add(new FChatMessageModel(3, snapshot.getString("messageText"), snapshot.getDate("sentAt")));
                                    } else {
                                        fChatMessageModelList.add(new FChatMessageModel(1, snapshot.getString("messageText"), snapshot.getDate("sentAt")));
                                    }
                                } else {
                                    if (snapshot.getBoolean("isImage")) {
                                        fChatMessageModelList.add(new FChatMessageModel(2, snapshot.getString("messageText"), snapshot.getDate("sentAt")));
                                    } else {
                                        fChatMessageModelList.add(new FChatMessageModel(0, snapshot.getString("messageText"), snapshot.getDate("sentAt")));
                                    }
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
        attachBtn = findViewById(R.id.attach_btn);
        parentLayout = findViewById(R.id.parent_layout);


        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageDialog.show();

            }
        });

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
            }
        });

        sendImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendImageFcn();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void sendImageFcn() throws IOException {
        if (filePath != null) {

            Bitmap fullsizeBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);

            Bitmap reducedBitmap = ImageResizer.reduceBitmapSize(fullsizeBitmap, 786432);


            sendImageBtn.setEnabled(false);
            sendImageBtn.setAlpha((float) 0.6);
            tapToSelectText.setText("Sending..");
            tapToSelectText.setVisibility(View.VISIBLE);
            String imageName = UUID.randomUUID().toString();
            final StorageReference reference = mStorageRef.child("Chat_Image/" + imageName);


            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            reducedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            byte[] bitmapdata = bos.toByteArray();

            UploadTask uploadTask = reference.putBytes(bitmapdata);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    sendMessage(downloadUrl.toString(), true);

                    uploadProgress.setVisibility(View.GONE);
                    selectImageDialog.dismiss();

                    Snackbar snackbar = Snackbar.make(parentLayout, "Success", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    selectImageBtn.setImageResource(R.drawable.gradient_dashboard);
                    tapToSelectText.setVisibility(View.GONE);
                    sendImageBtn.setAlpha(1);
                    sendImageBtn.setEnabled(true);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            uploadProgress.setVisibility(View.VISIBLE);
                            uploadProgress.setProgress((int) progress);

                        }
                    });

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                selectImageBtn.setImageBitmap(bitmap);
                tapToSelectText.setVisibility(View.GONE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}