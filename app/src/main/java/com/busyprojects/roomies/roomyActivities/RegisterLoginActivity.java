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

    EditText et_register, et_login;
    //   TextView tv_log_reg_title;
    AnimationManager animationManager;


    DatabaseReference db_ref;

    DialogEffect dialogEffect;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    RuntimePermissionsCs runtimePermissionsCs;
    // boolean CheckInternetReceiver.isOnline(this);
    RelativeLayout rel_login_reg, rel_login, rel_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
//        CheckInternetReceiver.isOnline(this) = sp.getBoolean(SessionManager.IS_INTERNET, false);
        //CheckInternetReceiver.isOnline(this) = CheckInternetReceiver.isOnline(this);
        db_ref = Helper.getFirebaseDatabseRef();

        dialogEffect = new DialogEffect(context);

        et_login = findViewById(R.id.et_login);
        et_register = findViewById(R.id.et_register);

        //tv_log_reg_title = findViewById(R.id.tv_log_reg_title);
        animationManager = AnimationManager.getInstance();

        rel_login_reg = findViewById(R.id.rel_log_reg);
        rel_login = findViewById(R.id.rel_login);
        rel_register = findViewById(R.id.rel_register);


        if (Build.VERSION.SDK_INT >= 23) {
            runtimePermissionsCs = new RuntimePermissionsCs(this);
            runtimePermissionsCs.getPermissions();
        }
    }


    String mobileLogin;


    public void loginRoomyold(View view) {

        if (CheckInternetReceiver.isOnline(this)) {
            mobileLogin = et_login.getText().toString();

            if (mobileLogin.equals("")) {
                Toast.makeText(context, "Please Check Empty Fields", Toast.LENGTH_SHORT).show();

            } else {

                dialogEffect.showDialog();
                final List<String> userListMobile = new ArrayList<>();
                // TODO: 1/28/2018 validate login mobile
                db_ref.child(Helper.USER)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                dialogEffect.cancelDialog();
                                userListMobile.clear();

                                for (DataSnapshot dataSnapshot1 :
                                        dataSnapshot.getChildren()) {
                                    User user = dataSnapshot1.getValue(User.class);
                                    userListMobile.add(user.getMobile());
                                }

                                if (userListMobile.contains(mobileLogin)) {

                                    // TODO: 1/28/2018 save main roomate (user) in session
                                    spe = sp.edit();
                                    spe.putString(SessionManager.MOBILE, mobileLogin);
                                    spe.apply();

                                    SharedPreferences spUc = getSharedPreferences(SessionManager.FILE_UC, MODE_PRIVATE);

                                    spe = spUc.edit();
                                    spe.putString(SessionManager.LOGGED_MOBILE_UC, mobileLogin);
                                    spe.apply();


                                    saveRoomyMacAddressForNotification();


                                    startActivity(new Intent(context, HomeActivity.class));


                                } else {

                                    // TODO: 3/24/2018  open admin
                                    if (mobileLogin.equals("0000")) {


                                    } else {
                                        Toast.makeText(context, "Invalid Login", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }

        } else {
            Helper.showCheckInternet(context);

        }
    }


    void saveRoomyMacAddressForNotification() {
        String macAddress = Helper.getMacAddr();

        if (macAddress.equals("")) {
            macAddress = "emulatorMacAdd";
        }
        spe = sp.edit();
        spe.putString(SessionManager.MAC_ADDRESS, macAddress);
        spe.apply();


        SharedPreferences spUc = getSharedPreferences(SessionManager.FILE_UC, MODE_PRIVATE);
        int payNotfSize = spUc.getInt(SessionManager.PNID_LIST, 0);

        System.out.println(payNotfSize + " payNotfSize ");

        String pnId = Helper.randomString(10);
        PaymentNotification paymentNotification = new PaymentNotification();
        paymentNotification.setMacAddress(macAddress);
        paymentNotification.setMobileLogged(mobileLogin);
        paymentNotification.setPnid(pnId);

        HashMap<String, Payment> paymentMap = new HashMap<>();

        // TODO: 3/1/2018  dummy Payment
        Payment payment = new Payment();
        payment.setPid("xdvxcvcxcv");
        payment.setMobileLogged(mobileLogin);

        for (int i = 0; i < payNotfSize; i++) {
            paymentMap.put(i + "defaultKeys", payment);
        }


        paymentNotification.setPaymentList(paymentMap);

        db_ref.child(Helper.PAYMENT_NOTIFICATION)
                .child(macAddress)
                .setValue(paymentNotification);


        //getRoomsAllMacAddressListInSession();


    }

/*
    public void registerRoomy(View view) {

if (CheckInternetReceiver.isOnline(this)) {
    final String mobileReg = et_register.getText().toString();

    if (mobileReg.equals("")) {
        Toast.makeText(context, "Please Check Empty Fields", Toast.LENGTH_SHORT).show();
    } else {


        db_ref.child(Helper.USER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        List<String> userList = new ArrayList<>();

                        for (DataSnapshot dataSnapshot1 :
                                dataSnapshot.getChildren()) {

                            User userDb = dataSnapshot1.getValue(User.class);

                            userList.add(userDb.getMobile());

                        }


                        if (userList.contains(mobileReg)) {
                            Toast.makeText(context, "User Already Exists", Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO: 2/25/2018 save user
                            String uid = Helper.randomString(10);
                            User user = new User();
                            user.setUid(uid);
                            user.setMobile(mobileReg);

                            db_ref.child(Helper.USER).child(uid)
                                    .setValue(user);
                            Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show();

                            sp.edit().putString(SessionManager.MOBILE, mobileReg).apply();


                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

}else
{
    Helper.showCheckInternet(context);

}
    }
*/


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

        Toast.makeText(context, "" + RuntimePermissionsCs.hasPermissions(this, PERMISSIONS), Toast.LENGTH_SHORT).show();
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
