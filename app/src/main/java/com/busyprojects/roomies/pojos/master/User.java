package com.busyprojects.roomies.pojos.master;

/**
 * Created by sanket on 1/28/2018.
 */

public class User
{
    private String uid;
    private String mobile;


    public User() {
    }

    public User(String uid, String mobile) {
        this.uid = uid;
        this.mobile = mobile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
