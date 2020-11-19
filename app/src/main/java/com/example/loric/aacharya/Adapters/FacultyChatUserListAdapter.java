package com.example.loric.aacharya.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.Models.FacultyChatUserModel;
import com.example.loric.aacharya.R;
import com.example.loric.aacharya.StudentsDashboard.FChatActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacultyChatUserListAdapter extends RecyclerView.Adapter<FacultyChatUserListAdapter.ViewHolder> {

    List<FacultyChatUserModel> facultyChatUserModelList;

    public FacultyChatUserListAdapter(List<FacultyChatUserModel> facultyChatUserModelList) {
        this.facultyChatUserModelList = facultyChatUserModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.f_chat_users_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FacultyChatUserModel model = facultyChatUserModelList.get(position);
        holder.setData(model.getUserProfile(), model.getUserName(), model.getUserSubject());

    }

    @Override
    public int getItemCount() {
        return facultyChatUserModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView userName, UserSubject;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.f_profile_image);
            userName = itemView.findViewById(R.id.f_name);
            UserSubject = itemView.findViewById(R.id.f_subject_detail);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), FChatActivity.class);
                    intent.putExtra("USER_NAME", facultyChatUserModelList.get(getAdapterPosition()).getUserName());
                    intent.putExtra("USER_ID", facultyChatUserModelList.get(getAdapterPosition()).getUserId());
                    intent.putExtra("USER_PROFILE", facultyChatUserModelList.get(getAdapterPosition()).getUserProfile());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        private void setData(String imageUrl, String user, String subject) {
            Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
            userName.setText(user);
            UserSubject.setText(subject);

        }

    }
}
