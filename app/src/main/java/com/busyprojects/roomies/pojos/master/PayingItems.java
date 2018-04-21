package com.busyprojects.roomies.pojos.master;

import android.content.Context;

import com.busyprojects.roomies.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanket on 3/11/2018.
 */

public  class PayingItems {
    private static  PayingItems ourInstance;

    private Map<String,String> itemsMap;
   public static PayingItems getInstance() {


       if (ourInstance==null)
       {
           ourInstance = new PayingItems();
       }
        return ourInstance;
    }

    private PayingItems() {
    }




  public   Map<String,String> getItemsMap()
    {
        return itemsMap;
    }




}
