package com.example.loric.aacharya.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.Models.OurAchieverModel;
import com.example.loric.aacharya.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OurAchieverAdapter extends RecyclerView.Adapter<OurAchieverAdapter.ViewHolder> {

    List<OurAchieverModel> ourAchieverModelList;

    public OurAchieverAdapter(List<OurAchieverModel> ourAchieverModelList) {
        this.ourAchieverModelList = ourAchieverModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.our_achievement_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(ourAchieverModelList.get(position).getAchieverPic(), ourAchieverModelList.get(position).getAchieverName(), ourAchieverModelList.get(position).getAchieverPosition());
    }

    @Override
    public int getItemCount() {
        return ourAchieverModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView name, position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.achiever_profile);
            name = itemView.findViewById(R.id.achiever_name);
            position = itemView.findViewById(R.id.achiever_position);
        }

        private void setData(String imageUrl, String Name, String Position) {
            Glide.with(itemView.getContext()).load(imageUrl).into(circleImageView);
            name.setText(Name);
            position.setText(Position);
        }
    }
}
