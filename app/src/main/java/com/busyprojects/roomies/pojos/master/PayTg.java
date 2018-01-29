package com.busyprojects.roomies.pojos.master;

/**
 * Created by sanket on 1/28/2018.
 */

public class PayTg
{
    private String roomyName;
    private String amountTg;


    public PayTg() {
    }

    public PayTg(String roomyName, String amountTg) {
        this.roomyName = roomyName;
        this.amountTg = amountTg;
    }

    public String getRoomyName() {
        return roomyName;
    }

    public void setRoomyName(String roomyName) {
        this.roomyName = roomyName;
    }

    public String getAmountTg() {
        return amountTg;
    }

    public void setAmountTg(String amountTg) {
        this.amountTg = amountTg;
    }
}
