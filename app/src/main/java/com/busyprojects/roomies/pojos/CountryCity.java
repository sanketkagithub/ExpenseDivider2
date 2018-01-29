package com.busyprojects.roomies.pojos;

/**
 * Created by sanket on 12/17/2017.
 */

public class CountryCity
{
    private String city;
    private  String country;

    

    public CountryCity() {
    }

    public CountryCity(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
