package com.example.loric.aacharya.Models;

import java.util.Date;

public class VacancyModel {
    String vacancyTitle;
    String vacancyDes;
    Date vacancyPostDate;
    String vacancyPdf;
    boolean isOver;

    public VacancyModel(String vacancyTitle, String vacancyDes, Date vacancyPostDate, String vacancyPdf, boolean isOver) {
        this.vacancyTitle = vacancyTitle;
        this.vacancyDes = vacancyDes;
        this.vacancyPostDate = vacancyPostDate;
        this.vacancyPdf = vacancyPdf;
        this.isOver = isOver;
    }

    public String getVacancyTitle() {
        return vacancyTitle;
    }

    public void setVacancyTitle(String vacancyTitle) {
        this.vacancyTitle = vacancyTitle;
    }

    public String getVacancyDes() {
        return vacancyDes;
    }

    public void setVacancyDes(String vacancyDes) {
        this.vacancyDes = vacancyDes;
    }

    public Date getVacancyPostDate() {
        return vacancyPostDate;
    }

    public void setVacancyPostDate(Date vacancyPostDate) {
        this.vacancyPostDate = vacancyPostDate;
    }

    public String getVacancyPdf() {
        return vacancyPdf;
    }

    public void setVacancyPdf(String vacancyPdf) {
        this.vacancyPdf = vacancyPdf;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }
}
