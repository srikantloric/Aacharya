package com.example.loric.aacharya.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.Models.OnlineClassStudentModel;
import com.example.loric.aacharya.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OnlineClassStudentAdapter extends RecyclerView.Adapter<OnlineClassStudentAdapter.ViewHolder> {


    Context context;
    List<OnlineClassStudentModel> onlineClassStudentModelList;

    public OnlineClassStudentAdapter(Context context, List<OnlineClassStudentModel> onlineClassStudentModelList) {
        this.context = context;
        this.onlineClassStudentModelList = onlineClassStudentModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_class_student_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setData(onlineClassStudentModelList.get(position).getTeacherImage()
                , onlineClassStudentModelList.get(position).getTeacherName()
                , onlineClassStudentModelList.get(position).getTeacherSubject(),
                onlineClassStudentModelList.get(position).getAboutClass()
                , onlineClassStudentModelList.get(position).getMeetCode()
                , onlineClassStudentModelList.get(position).getMeetingTiming());
        holder.setVisibility(position);
    }

    @Override
    public int getItemCount() {
        return onlineClassStudentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName, userSubject, about, meetingTime;
        CircleImageView circleImageView;
        EditText code;
        Button join;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.teacher_name);
            userSubject = itemView.findViewById(R.id.teacher_subject);
            about = itemView.findViewById(R.id.about_class);
            circleImageView = itemView.findViewById(R.id.teacher_profile);
            code = itemView.findViewById(R.id.google_meet_code);
            meetingTime = itemView.findViewById(R.id.meetingTiming);
            join = itemView.findViewById(R.id.join_now_btn);
        }

        private void setData(String imageUrl, String user, String subject, String aboutT, String codeT, String timing) {
            Glide.with(itemView.getContext()).load(imageUrl).into(circleImageView);
            userName.setText(user);
            userSubject.setText(subject);
            about.setText(aboutT);
            code.setText(codeT);
            meetingTime.setText(timing);
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = codeT;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
        }


        private void setVisibility(int Position) {
            FirebaseFirestore.getInstance().collection("ONLINE_CLASSES")
                    .document(onlineClassStudentModelList.get(Position).getDocumentId())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value != null) {
                                boolean isLive = value.getBoolean("isLive");
                                if (isLive) {
                                    join.setEnabled(true);
                                    join.setAlpha(1);
                                } else {
                                    join.setEnabled(false);
                                    join.setAlpha((float) 0.6);
                                }

                            } else {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
