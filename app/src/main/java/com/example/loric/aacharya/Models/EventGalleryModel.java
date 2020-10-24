package com.example.loric.aacharya.Models;

public class EventGalleryModel {

    public static final int TITLE_LAYOUT=0;
    public static final int IMAGES_LAYOUT = 1;
    String imageUrl;
    private int ViewTypes;

    public EventGalleryModel(int viewTypes, String imageUrl) {
        ViewTypes = viewTypes;
        this.imageUrl = imageUrl;
    }

    public int getViewTypes() {
        return ViewTypes;
    }

    public void setViewTypes(int viewTypes) {
        ViewTypes = viewTypes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
