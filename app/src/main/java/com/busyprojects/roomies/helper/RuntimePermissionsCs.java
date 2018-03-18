package com.busyprojects.roomies.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by sanket on 1/19/2018.
 */

public class RuntimePermissionsCs
{


    int PERMISSION_ALL = 1;
    Context context;



    public RuntimePermissionsCs(Context context) {
        this.context = context;
    }

    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_WIFI_STATE};


   public void getPermissions()
    {

        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, PERMISSION_ALL);
        } else {
            Toast.makeText(context, "granted", Toast.LENGTH_SHORT).show();
        }


    }


    public static boolean hasPermissions(Context context, String... permissions)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null)
        {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


}
