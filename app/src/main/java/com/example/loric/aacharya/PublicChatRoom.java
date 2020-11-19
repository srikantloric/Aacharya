package com.example.loric.aacharya;

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

import com.example.loric.aacharya.Adapters.PublicChatRoomAdapter;
import com.example.loric.aacharya.Models.ChatRoomModel;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class PublicChatRoom extends AppCompatActivity {
    List<ChatRoomModel> chatRoomModelList = new ArrayList<>();
    private ImageView backBtn;
    private RecyclerView recyclerView;
    private PublicChatRoomAdapter adapter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
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
    private String USER_NAME = "";
    private String USER_IMAGE = "";
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_chat_room);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        /////Image Picker Dialog//////
        selectImageDialog = new Dialog(this);
        selectImageDialog.setContentView(R.layout.image_picker_dialog_layout);
        selectImageDialog.setCancelable(true);
        selectImageBtn = selectImageDialog.findViewById(R.id.image_select);
        sendImageBtn = selectImageDialog.findViewById(R.id.send_btn);
        uploadProgress = selectImageDialog.findViewById(R.id.upload_progress);
        tapToSelectText = selectImageDialog.findViewById(R.id.tap_to_select);
        /////Image Picker Dialog//////


        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog_layout);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        if (USER_IMAGE == "" && USER_NAME == "") {
            CurrentUserData();
        } else {
            loadingDialog.dismiss();
        }
        initView();
        setHeader();
        loadMessages();
        sentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(messageEditText.getText())) {
                    Toast.makeText(PublicChatRoom.this, "Cannot sent empty text..", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(messageEditText.getText().toString(), false);
                    messageEditText.setText("");
                }
            }
        });
    }

    private void CurrentUserData() {
        firebaseFirestore.collection("USERS")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            USER_NAME = task.getResult().getString("userName");
                            USER_IMAGE = task.getResult().getString("userProfile");
                        } else {
                            Toast.makeText(PublicChatRoom.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });

    }

    private void sendMessage(String messageEditText, boolean isImage) {
        Map<String, Object> message = new HashMap<>();
        message.put("messageText", messageEditText);
        message.put("sender", firebaseAuth.getCurrentUser().getUid());
        message.put("sentAt", FieldValue.serverTimestamp());
        message.put("isImage", isImage);
        message.put("sender_image", USER_IMAGE);
        message.put("sender_name", USER_NAME);
        firebaseFirestore.collection("MESSAGE")
                .document("PUBLIC_CHAT_ROOM")
                .collection("messages")
                .document()
                .set(message);


    }

    private void loadMessages() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new PublicChatRoomAdapter(chatRoomModelList);
        recyclerView.setAdapter(adapter);
        firebaseFirestore.collection("MESSAGE")
                .document("PUBLIC_CHAT_ROOM")
                .collection("messages")
                .orderBy("sentAt", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        chatRoomModelList.clear();
                        if (value.getDocuments() != null) {
                            for (DocumentSnapshot snapshot : value.getDocuments()) {

                                if (snapshot.getString("sender").equals(firebaseAuth.getCurrentUser().getUid())) {
                                    if (snapshot.getBoolean("isImage")) {
                                        chatRoomModelList.add(new ChatRoomModel(3,
                                                snapshot.getString("messageText"),
                                                snapshot.getDate("sentAt"),
                                                snapshot.getString("sender_name"),
                                                snapshot.getString("sender_image")
                                        ));
                                    } else {
                                        chatRoomModelList.add(new ChatRoomModel(1,
                                                snapshot.getString("messageText"),
                                                snapshot.getDate("sentAt"),
                                                snapshot.getString("sender_name"),
                                                snapshot.getString("sender_image")
                                        ));
                                    }
                                } else {
                                    if (snapshot.getBoolean("isImage")) {
                                        chatRoomModelList.add(new ChatRoomModel(2,
                                                snapshot.getString("messageText"),
                                                snapshot.getDate("sentAt"),
                                                snapshot.getString("sender_name"),
                                                snapshot.getString("sender_image")
                                        ));
                                    } else {
                                        chatRoomModelList.add(new ChatRoomModel(0,
                                                snapshot.getString("messageText"),
                                                snapshot.getDate("sentAt"),
                                                snapshot.getString("sender_name"),
                                                snapshot.getString("sender_image")
                                        ));
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(PublicChatRoom.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setHeader() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initView() {
        backBtn = findViewById(R.id.back_btn_fchat);
        recyclerView = findViewById(R.id.rv_fchat_gd);
        sentBtn = findViewById(R.id.sentBtnGd);
        messageEditText = findViewById(R.id.messageInputGd);
        attachBtn = findViewById(R.id.attach_btn_gd);
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
                            Toast.makeText(PublicChatRoom.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
