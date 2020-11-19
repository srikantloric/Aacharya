package com.example.loric.aacharya.Models;
import java.util.Date;
public class ChatRoomModel {
    public static final int INCOMING_MSG_LAYOUT = 0;
    public static final int OUTGOING_MSG_LAYOUT = 1;
    public static final int INCOMING_IMAGE_LAYOUT = 2;
    public static final int OUTGOING_IMAGE_LAYOUT = 3;

    int viewType;
    private String messageText;
    private Date dateTime;
    private String senderName;
    private String senderImage;

    public ChatRoomModel(int viewType, String messageText, Date dateTime, String senderName, String senderImage) {
        this.viewType = viewType;
        this.messageText = messageText;
        this.dateTime = dateTime;
        this.senderName = senderName;
        this.senderImage = senderImage;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }
}
