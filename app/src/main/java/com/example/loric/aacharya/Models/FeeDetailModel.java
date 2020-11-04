package com.example.loric.aacharya.Models;

import java.util.Date;

public class FeeDetailModel {
    String paidAmount,DueAmount;
    Date paymentDate;

    public FeeDetailModel(String paidAmount, String dueAmount, Date paymentDate) {
        this.paidAmount = paidAmount;
        DueAmount = dueAmount;
        this.paymentDate = paymentDate;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getDueAmount() {
        return DueAmount;
    }

    public void setDueAmount(String dueAmount) {
        DueAmount = dueAmount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
