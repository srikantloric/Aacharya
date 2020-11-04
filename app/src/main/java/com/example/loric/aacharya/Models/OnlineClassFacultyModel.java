package com.example.loric.aacharya.Models;

public class OnlineClassFacultyModel {
    String classDate,classTime;
    boolean isLive;
    String documentId;

    public OnlineClassFacultyModel(String classDate, String classTime, boolean isLive, String documentId) {
        this.classDate = classDate;
        this.classTime = classTime;
        this.isLive = isLive;
        this.documentId = documentId;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
