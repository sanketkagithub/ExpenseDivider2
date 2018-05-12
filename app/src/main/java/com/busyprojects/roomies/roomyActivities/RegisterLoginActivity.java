package com.busyprojects.roomies.roomyActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.AnimationManager;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.RuntimePermissionsCs;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.User;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.busyprojects.roomies.pojos.transaction.PaymentNotification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterLoginActivity extends Activity {
    Context context = RegisterLoginActivity.this;
    //AnimationManager animationManager;



   // SharedPreferences.Editor spe;

    RuntimePermissionsCs runtimePermissionsCs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);


        //animationManager = AnimationManager.getInstance();



        if (Build.VERSION.SDK_INT >= 23) {
            runtimePermissionsCs = new RuntimePermissionsCs(this);
            runtimePermissionsCs.getPermissions();
        }
    }






    String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.ACCESS_WIFI_STATE};

    void requestPermission() {
        ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, 1);

    }


    public void goToLoginActivity(View view) {
        //animationManager.animateButton(view, context);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!RuntimePermissionsCs.hasPermissions(this, PERMISSIONS)) {
                requestPermission();
            } else {
                enterRoomLogin();
            }


        } else {
            enterRoomLogin();
        }

    }

    void enterRoomLogin() {

        Intent intentLogin = new Intent(context, RoomLoginActivity.class);
        intentLogin.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentLogin);

    }

    public void goToRegisterActivity(View view) {
        //animationManager.animateButton(view, context);
        Intent intentLogin = new Intent(context, RoomRegistrationActivity.class);
        intentLogin.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentLogin);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

        finishAffinity();
        finish();
    }
}
