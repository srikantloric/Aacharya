package com.example.loric.aacharya.Models;

public class StudentNotesModel {
    String bookTitle,bookDesc,bookPrice,bookImage,docId,bookUrl;
    boolean isPurchases;

    public StudentNotesModel(String bookTitle, String bookDesc, String bookPrice, String bookImage, String docId, String bookUrl, boolean isPurchases) {
        this.bookTitle = bookTitle;
        this.bookDesc = bookDesc;
        this.bookPrice = bookPrice;
        this.bookImage = bookImage;
        this.docId = docId;
        this.bookUrl = bookUrl;
        this.isPurchases = isPurchases;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public boolean isPurchases() {
        return isPurchases;
    }

    public void setPurchases(boolean purchases) {
        isPurchases = purchases;
    }
}
