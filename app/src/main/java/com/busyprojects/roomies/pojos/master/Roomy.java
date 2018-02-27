package com.busyprojects.roomies.pojos.master;

import com.busyprojects.roomies.pojos.transaction.Payment;

import java.util.List;

/**
 * Created by sanket on 1/27/2018.
 */

public class Roomy {
    private String rid;
    private String mobileLogged;
    private String name;
    private String mobile;
    private String registrationDateTime;
    private String macAddress;
    private List<Payment> paymentList;


    public Roomy() {
    }

    public Roomy(String rid, String mobileLogged, String name, String mobile, String registrationDateTime, String macAddress, List<Payment> paymentList) {
        this.rid = rid;
        this.mobileLogged = mobileLogged;
        this.name = name;
        this.mobile = mobile;
        this.registrationDateTime = registrationDateTime;
        this.macAddress = macAddress;
        this.paymentList = paymentList;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getMobileLogged() {
        return mobileLogged;
    }

    public void setMobileLogged(String mobileLogged) {
        this.mobileLogged = mobileLogged;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegistrationDateTime() {
        return registrationDateTime;
    }

    public void setRegistrationDateTime(String registrationDateTime) {
        this.registrationDateTime = registrationDateTime;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }
}