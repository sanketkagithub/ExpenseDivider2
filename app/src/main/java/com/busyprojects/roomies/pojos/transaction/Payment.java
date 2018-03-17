package com.busyprojects.roomies.pojos.transaction;

import com.busyprojects.roomies.pojos.master.Roomy;

/**
 * Created by sanket on 1/27/2018.
 */

public class Payment {
    private String pid;
    private String mobileLogged;
    private String payingItem;
    private Roomy roomy;
    private String payinItemUrl;
    private double amount;
    private String paymentDateTime;

    private boolean isTransferPayment;
    private String toRoomy;

    public Payment() {
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMobileLogged() {
        return mobileLogged;
    }

    public void setMobileLogged(String mobileLogged) {
        this.mobileLogged = mobileLogged;
    }

    public String getPayingItem() {
        return payingItem;
    }

    public void setPayingItem(String payingItem) {
        this.payingItem = payingItem;
    }

    public Roomy getRoomy() {
        return roomy;
    }

    public void setRoomy(Roomy roomy) {
        this.roomy = roomy;
    }

    public String getPayinItemUrl() {
        return payinItemUrl;
    }

    public void setPayinItemUrl(String payinItemUrl) {
        this.payinItemUrl = payinItemUrl;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(String paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public boolean isTransferPayment() {
        return isTransferPayment;
    }

    public void setTransferPayment(boolean transferPayment) {
        isTransferPayment = transferPayment;
    }

    public String getToRoomy() {
        return toRoomy;
    }

    public void setToRoomy(String toRoomy) {
        this.toRoomy = toRoomy;
    }
}