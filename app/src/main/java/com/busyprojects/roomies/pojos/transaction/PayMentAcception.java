package com.busyprojects.roomies.pojos.transaction;

/**
 * Created by sanket on 3/8/2018.
 */

public class PayMentAcception
{
    private String pmaId;
    private String mobileLogged;
    private String macAddress;
    private String isAccepted;
    private String paymentStatus;


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

    public String getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
