package com.example.loric.aacharya.Models;

import java.util.Date;

public class FChatMessageModel {

    public static final int INCOMING_MSG_LAYOUT=0;
    public static final int OUTGOING_MSG_LAYOUT=1;

    int viewType;
    private String messageText;
    private Date dateTime;

    public FChatMessageModel(int viewType, String messageText, Date dateTime) {
        this.viewType = viewType;
        this.messageText = messageText;
        this.dateTime = dateTime;
    }

    public static int getIncomingMsgLayout() {
        return INCOMING_MSG_LAYOUT;
    }

    public static int getOutgoingMsgLayout() {
        return OUTGOING_MSG_LAYOUT;
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
}
