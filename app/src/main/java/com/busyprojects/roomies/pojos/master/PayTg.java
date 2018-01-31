package com.busyprojects.roomies.pojos.master;

/**
 * Created by sanket on 1/28/2018.
 */

public class PayTg {
    private String roomyName;
    private long amountTg;
    private long amountVariation;


    public PayTg() {
    }

    public PayTg(String roomyName, long amountTg, long amountVariation) {
        this.roomyName = roomyName;
        this.amountTg = amountTg;
        this.amountVariation = amountVariation;
    }

    public String getRoomyName() {
        return roomyName;
    }

    public void setRoomyName(String roomyName) {
        this.roomyName = roomyName;
    }

    public long getAmountTg() {
        return amountTg;
    }

    public void setAmountTg(long amountTg) {
        this.amountTg = amountTg;
    }

    public long getAmountVariation() {
        return amountVariation;
    }

    public void setAmountVariation(long amountVariation) {
        this.amountVariation = amountVariation;
    }
}