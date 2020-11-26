package com.example.loric.aacharya.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.loric.aacharya.EventGallery.PhotoFullScreenActivity;
import com.example.loric.aacharya.Models.EventGalleryModel;
import com.example.loric.aacharya.R;

import java.util.List;

import static com.example.loric.aacharya.Models.EventGalleryModel.IMAGES_LAYOUT;
import static com.example.loric.aacharya.Models.EventGalleryModel.TITLE_LAYOUT;

public class EventGalleryAdapter extends RecyclerView.Adapter {

    List<EventGalleryModel> eventGalleryModelList;

    public EventGalleryAdapter(List<EventGalleryModel> eventGalleryModelList) {
        this.eventGalleryModelList = eventGalleryModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (eventGalleryModelList.get(position).getViewTypes()) {
            case 0:
                return TITLE_LAYOUT;
            case 1:
                return IMAGES_LAYOUT;
            default:
                return -1;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TITLE_LAYOUT:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_gallary_title_item_layout, parent, false);
                return new TitleLayout(view);
            case IMAGES_LAYOUT:
                View imageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_gallary_item_layout, parent, false);
                return new ImageLayout(imageView);
            default:
                return null;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (eventGalleryModelList.get(position).getViewTypes()) {
            case TITLE_LAYOUT:
                ((TitleLayout) holder).setTitle(eventGalleryModelList.get(position).getImageUrl());
                break;
            case IMAGES_LAYOUT:
                ((ImageLayout) holder).setImageView(eventGalleryModelList.get(position).getImageUrl());
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return eventGalleryModelList.size();
    }
    class ImageLayout extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageLayout(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.faculty_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), PhotoFullScreenActivity.class);
                    intent.putExtra("IMAGE_URL", eventGalleryModelList.get(getAdapterPosition()).getImageUrl());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void setImageView(String imageUri) {

            Glide.with(itemView.getContext()).load(imageUri).apply(new RequestOptions().placeholder(R.drawable.loading_image_placeholder)).centerCrop().into(imageView);
        }
    }
    class TitleLayout extends RecyclerView.ViewHolder {
        TextView title;

        public TitleLayout(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_title);

        }
        private void setTitle(String getTitle) {
            title.setText(getTitle);
        }

    }
}
