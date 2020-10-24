package com.example.loric.aacharya.Adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.loric.aacharya.Models.ImageSliderModel;
import com.example.loric.aacharya.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;


public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderViewHolder> {

    Context context;
    List<ImageSliderModel> imageSliderModelList;

    public ImageSliderAdapter(Context context, List<ImageSliderModel> imageSliderModelList) {
        this.context = context;
        this.imageSliderModelList = imageSliderModelList;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_item_layout, parent, false);

        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        ImageSliderModel imageSliderModel = imageSliderModelList.get(position);
        viewHolder.setData(imageSliderModel.getImageUrl());
    }

    @Override
    public int getCount() {
        return imageSliderModelList.size();
    }

    public class SliderViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView sliderImageView;

        public SliderViewHolder(View itemView) {
            super(itemView);
            sliderImageView = itemView.findViewById(R.id.SliderImageView);
        }

        private void setData(String imageUrl) {
            Log.d("pop", "setData: "+imageUrl);
            Glide.with(itemView.getContext()).load(imageUrl).into(sliderImageView);
        }

    }
}
