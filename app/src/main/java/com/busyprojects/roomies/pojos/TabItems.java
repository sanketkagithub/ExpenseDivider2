package com.busyprojects.roomies.pojos;

import java.util.List;

/**
 * Created by sanket on 1/13/2018.
 */

public class TabItems
{
    String tabTitle;
    List<CountryCity> countryCityList;

    public TabItems() {
    }

    public TabItems(String tabTitle, List<CountryCity> countryCityList) {
        this.tabTitle = tabTitle;
        this.countryCityList = countryCityList;
    }

    public String getTabTitle() {
        return tabTitle;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    public List<CountryCity> getCountryCityList() {
        return countryCityList;
    }

    public void setCountryCityList(List<CountryCity> countryCityList) {
        this.countryCityList = countryCityList;
    }
}
