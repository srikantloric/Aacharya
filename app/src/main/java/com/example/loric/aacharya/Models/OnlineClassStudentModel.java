package com.example.loric.aacharya.Models;

public class OnlineClassStudentModel {
    String teacherName,teacherSubject,teacherImage,aboutClass,meetCode,meetingTiming,documentId;

    public OnlineClassStudentModel(String teacherName, String teacherSubject, String teacherImage, String aboutClass, String meetCode, String meetingTiming, String documentId) {
        this.teacherName = teacherName;
        this.teacherSubject = teacherSubject;
        this.teacherImage = teacherImage;
        this.aboutClass = aboutClass;
        this.meetCode = meetCode;
        this.meetingTiming = meetingTiming;
        this.documentId = documentId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherSubject() {
        return teacherSubject;
    }

    public void setTeacherSubject(String teacherSubject) {
        this.teacherSubject = teacherSubject;
    }

    public String getTeacherImage() {
        return teacherImage;
    }

    public void setTeacherImage(String teacherImage) {
        this.teacherImage = teacherImage;
    }

    public String getAboutClass() {
        return aboutClass;
    }

    public void setAboutClass(String aboutClass) {
        this.aboutClass = aboutClass;
    }

    public String getMeetCode() {
        return meetCode;
    }

    public void setMeetCode(String meetCode) {
        this.meetCode = meetCode;
    }

    public String getMeetingTiming() {
        return meetingTiming;
    }

    public void setMeetingTiming(String meetingTiming) {
        this.meetingTiming = meetingTiming;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
