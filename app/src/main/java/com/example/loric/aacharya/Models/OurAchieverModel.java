package com.example.loric.aacharya.Models;

public class OurAchieverModel {
    String achieverPic,achieverName,achieverPosition;

    public OurAchieverModel(String achieverPic, String achieverName, String achieverPosition) {
        this.achieverPic = achieverPic;
        this.achieverName = achieverName;
        this.achieverPosition = achieverPosition;
    }

    public String getAchieverPic() {
        return achieverPic;
    }

    public void setAchieverPic(String achieverPic) {
        this.achieverPic = achieverPic;
    }

    public String getAchieverName() {
        return achieverName;
    }

    public void setAchieverName(String achieverName) {
        this.achieverName = achieverName;
    }

    public String getAchieverPosition() {
        return achieverPosition;
    }

    public void setAchieverPosition(String achieverPosition) {
        this.achieverPosition = achieverPosition;
    }
}
