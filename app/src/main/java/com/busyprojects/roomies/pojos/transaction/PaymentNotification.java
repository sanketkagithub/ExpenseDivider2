package com.busyprojects.roomies.pojos.transaction;

import java.util.List;

/**
 * Created by sanket on 2/27/2018.
 */

public class PaymentNotification
{
  //  private String pnid;
    private String mobileLogged;
    
    private List<Payment> paymentList;

    public String getMobileLogged() {
        return mobileLogged;
    }

    public void setMobileLogged(String mobileLogged) {
        this.mobileLogged = mobileLogged;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }
}
