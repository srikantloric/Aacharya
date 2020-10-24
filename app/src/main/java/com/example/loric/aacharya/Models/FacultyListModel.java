package com.example.loric.aacharya.Models;

public class FacultyListModel {


    public static final int TITLE_LAYOUT_FACULTY = 0;
    public static final int ITEM_LAYOUT = 1;
    ///////////Title Layout///////////
    String title;
    ///////Item Layout//////////
    String imageUrl, imageTitle;
    private int viewType;

    public FacultyListModel(int viewType, String title) {
        this.viewType = viewType;
        this.title = title;
    }

    public FacultyListModel(int viewType, String imageUrl, String imageTitle) {
        this.viewType = viewType;
        this.imageUrl = imageUrl;
        this.imageTitle = imageTitle;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
    ///////////Title Layout///////////

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
