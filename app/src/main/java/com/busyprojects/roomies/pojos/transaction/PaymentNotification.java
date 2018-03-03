package com.busyprojects.roomies.pojos.transaction;

import java.util.HashMap;

/**
 * Created by sanket on 2/27/2018.
 */

public class PaymentNotification {
    private String pnid;
    private String mobileLogged;
    private String macAddress;
    private HashMap<String,Payment> paymentList;

    // TODO: 3/1/2018 hashMap 

    public String getPnid() {
        return pnid;
    }

    public void setPnid(String pnid) {
        this.pnid = pnid;
    }

    public String getMobileLogged() {
        return mobileLogged;
    }

    public void setMobileLogged(String mobileLogged) {
        this.mobileLogged = mobileLogged;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public HashMap<String, Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(HashMap<String, Payment> paymentList) {
        this.paymentList = paymentList;
    }
}