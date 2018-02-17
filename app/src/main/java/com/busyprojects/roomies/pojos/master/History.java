package com.busyprojects.roomies.pojos.master;

import com.busyprojects.roomies.pojos.transaction.Payment;

import java.util.List;

/**
 * Created by sanket on 2/5/2018.
 */

public class History {

    String hid;
    String mobileLogged;
    String dateTime;
    List<PayTg> eachPaymentList;
    List<Payment> paymentList;


    public History() {
    }


    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getMobileLogged() {
        return mobileLogged;
    }

    public void setMobileLogged(String mobileLogged) {
        this.mobileLogged = mobileLogged;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<PayTg> getEachPaymentList() {
        return eachPaymentList;
    }

    public void setEachPaymentList(List<PayTg> eachPaymentList) {
        this.eachPaymentList = eachPaymentList;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }
}