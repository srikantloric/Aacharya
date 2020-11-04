package com.example.loric.aacharya.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loric.aacharya.Models.OnlineClassFacultyModel;
import com.example.loric.aacharya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OnlineClassFacultyAdapter extends RecyclerView.Adapter<OnlineClassFacultyAdapter.ViewHolder> {


    List<OnlineClassFacultyModel> onlineClassFacultyModelList;

    public OnlineClassFacultyAdapter(List<OnlineClassFacultyModel> onlineClassFacultyModelList) {
        this.onlineClassFacultyModelList = onlineClassFacultyModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_class_rv_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(onlineClassFacultyModelList.get(position).getClassDate(), onlineClassFacultyModelList.get(position).getClassTime(), onlineClassFacultyModelList.get(position).isLive());
        Log.d("lol", "onBindViewHolder: " + onlineClassFacultyModelList.get(position).getDocumentId());

    }

    @Override
    public int getItemCount() {
        return onlineClassFacultyModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView scheduledDateAndTime;
        Switch aSwitch;
        ImageView deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduledDateAndTime = itemView.findViewById(R.id.date_and_time);
            aSwitch = itemView.findViewById(R.id.make_live_btn);
            deleteBtn = itemView.findViewById(R.id.delete_item_btn);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseFirestore.getInstance().collection("ONLINE_CLASSES")
                            .document(onlineClassFacultyModelList.get(getAdapterPosition()).getDocumentId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseFirestore.getInstance().collection("USERS")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .collection("ONLINE_CLASSES")
                                            .document(onlineClassFacultyModelList.get(getAdapterPosition()).getDocumentId())
                                            .delete();
                                }
                            });
                }
            });

            aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore.getInstance().collection("USERS")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("ONLINE_CLASSES")
                            .document(onlineClassFacultyModelList.get(getAdapterPosition()).getDocumentId())
                            .update("isLive", aSwitch.isChecked())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    FirebaseFirestore.getInstance().collection("ONLINE_CLASSES")
                                            .document(onlineClassFacultyModelList.get(getAdapterPosition()).getDocumentId())
                                            .update("isLive", aSwitch.isChecked());
                                }
                            });

                }
            });
        }

        private void setData(String Date, String time, boolean isLive) {
            scheduledDateAndTime.setText(Date + " " + time);
            if (isLive) {
                aSwitch.setChecked(true);
            } else {
                aSwitch.setChecked(false);
            }
        }

    }
}
