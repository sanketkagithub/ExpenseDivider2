package com.busyprojects.roomies.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sanket on 3/10/2018.
 */

public class CheckInternetReceiver extends BroadcastReceiver
{



 public static  boolean isInternet;


    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sp = context.getSharedPreferences(SessionManager.FILE_WTC,Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();

     //   Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();



      if (isOnline(context))
      {
//          spe.putBoolean(SessionManager.IS_INTERNET,true);
//          spe.apply();

          isInternet = true;
      }else
      {
//          spe.putBoolean(SessionManager.IS_INTERNET,false);
//          spe.apply();

          isInternet = false;
      }

      //  Toast.makeText(context, isInternet + " ", Toast.LENGTH_SHORT).show();


    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }



}
