package com.example.loric.aacharya.Models;

public class CoursesOfferedModel {
    String courseName,courseDetail,courseDetailExtra;

    public CoursesOfferedModel(String courseName, String courseDetail, String courseDetailExtra) {
        this.courseName = courseName;
        this.courseDetail = courseDetail;
        this.courseDetailExtra = courseDetailExtra;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(String courseDetail) {
        this.courseDetail = courseDetail;
    }

    public String getCourseDetailExtra() {
        return courseDetailExtra;
    }

    public void setCourseDetailExtra(String courseDetailExtra) {
        this.courseDetailExtra = courseDetailExtra;
    }
}
