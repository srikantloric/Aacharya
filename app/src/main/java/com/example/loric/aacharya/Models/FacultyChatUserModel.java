package com.example.loric.aacharya.Models;

public class FacultyChatUserModel {
    String userName;
    String userProfile;
    String userSubject;
    String userId;

    public FacultyChatUserModel(String userName, String userProfile, String userSubject, String userId) {
        this.userName = userName;
        this.userProfile = userProfile;
        this.userSubject = userSubject;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserSubject() {
        return userSubject;
    }

    public void setUserSubject(String userSubject) {
        this.userSubject = userSubject;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
