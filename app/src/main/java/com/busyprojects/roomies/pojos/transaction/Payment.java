package com.busyprojects.roomies.pojos.transaction;

import com.busyprojects.roomies.pojos.master.Roomy;

/**
 * Created by sanket on 1/27/2018.
 */

public class Payment
{
   private String pid;
   private String mobileLogged;
   private Roomy roomy;
   private long amount;
   private String paymentDateTime;


   public Payment() {
   }


   public Payment(String pid, String mobileLogged, Roomy roomy, long amount, String paymentDateTime) {
      this.pid = pid;
      this.mobileLogged = mobileLogged;
      this.roomy = roomy;
      this.amount = amount;
      this.paymentDateTime = paymentDateTime;
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

   public Roomy getRoomy() {
      return roomy;
   }

   public void setRoomy(Roomy roomy) {
      this.roomy = roomy;
   }

   public long getAmount() {
      return amount;
   }

   public void setAmount(long amount) {
      this.amount = amount;
   }

   public String getPaymentDateTime() {
      return paymentDateTime;
   }

   public void setPaymentDateTime(String paymentDateTime) {
      this.paymentDateTime = paymentDateTime;
   }


}
