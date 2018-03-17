package com.busyprojects.roomies.pojos.master;

/**
 * Created by sanket on 1/28/2018.
 */

public class PayTg {
    private String roomyName;
    private double amountTg;
    private double amountVariation;
    private String mobile;
    private String mobileLogged;
   private  String payTgId;


    public String getRoomyName() {
        return roomyName;
    }

    public void setRoomyName(String roomyName) {
        this.roomyName = roomyName;
    }

    public double getAmountTg() {
        return amountTg;
    }

    public void setAmountTg(double amountTg) {
        this.amountTg = amountTg;
    }

    public double getAmountVariation() {
        return amountVariation;
    }

    public void setAmountVariation(double amountVariation) {
        this.amountVariation = amountVariation;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobileLogged() {
        return mobileLogged;
    }

    public void setMobileLogged(String mobileLogged) {
        this.mobileLogged = mobileLogged;
    }

    public String getPayTgId() {
        return payTgId;
    }

    public void setPayTgId(String payTgId) {
        this.payTgId = payTgId;
    }
}