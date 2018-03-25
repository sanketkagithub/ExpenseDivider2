package com.busyprojects.roomies.roomyActivities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.helper.ToastManager;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AddRoomyActivity extends Activity {

    DialogEffect dialogEffect;
    DatabaseReference db_ref;
    Context context = AddRoomyActivity.this;
    String mobileLogged;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    boolean isTransfer;

    EditText et_name, et_mobile;

    RelativeLayout rel_iv_roomy_home;
    ImageView iv_name, iv_call;
String appColor;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_roomy);
        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);
        Button but_save = findViewById(R.id.but_save);
        Button but_back = findViewById(R.id.but_back);

        iv_name = findViewById(R.id.iv_name);
        iv_call = findViewById(R.id.iv_call);

        rel_iv_roomy_home = findViewById(R.id.rel_iv_roomy_home);

        db_ref = Helper.getFirebaseDatabseRef();
        dialogEffect = new DialogEffect(context);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");
        isTransfer = sp.getBoolean(SessionManager.IS_TRANSFER, false);
         appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);


        rel_iv_roomy_home.setBackgroundColor(Color.parseColor(appColor));
        but_save.setBackgroundColor(Color.parseColor(appColor));
        but_back.setBackgroundColor(Color.parseColor(appColor));

        setApptheme();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void setApptheme() {

        int profile = sp.getInt(SessionManager.IV_ROOMY, R.drawable.name);
        int mobile = sp.getInt(SessionManager.IV_MOBILE, R.drawable.call);

        iv_name.setImageResource(profile);
        iv_call.setImageResource(mobile);


        if (Build.VERSION.SDK_INT>=21) {
            //SessionManager.setCursorColor(et_name,Color.parseColor(appColor));
            et_name.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(appColor)));
            et_mobile.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(appColor)));
        }
    }


    public void saveRoomyNdeletePaymenPAytfIfIsTransfer(View view) {
        saveRoommate();

    }

    void deletePaymentNpayTgAtIfTransfered()
    {


        // TODO: 2/5/2018 delete current Payment

        dialogEffect.showDialog();
        db_ref.child(Helper.PAYMENT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {


                    for (DataSnapshot dataSnapshot1 :
                            dataSnapshot.getChildren()) {

                        Payment payment = dataSnapshot1.getValue(Payment.class);

                        if (payment.getMobileLogged().equals(mobileLogged)) {

                            db_ref.child(Helper.PAYMENT)
                                    .child(payment.getPid())
                                    .removeValue();

                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        db_ref.child(Helper.AFTER_TRANSFER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dialogEffect.cancelDialog();
                try {


                    for (DataSnapshot dataSnapshot1 :
                            dataSnapshot.getChildren()) {

                        PayTg payTg = dataSnapshot1.getValue(PayTg.class);

                        if (payTg.getMobileLogged().equals(mobileLogged)) {
                            db_ref.child(Helper.AFTER_TRANSFER)
                                    .child(payTg.getPayTgId())
                                    .removeValue();

                        }


                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        db_ref.child(Helper.IS_TRANSFER).child(mobileLogged).setValue(false);

        Toast.makeText(context, "Data Cleared Successfully", Toast.LENGTH_SHORT).show();


    }


    void saveRoommate()
    {

        if (CheckInternetReceiver.isOnline(this)) {
            dialogEffect.showDialog();
            String rid = Helper.randomString(10);
            String nameS = et_name.getText().toString();
            String mobileS = et_mobile.getText().toString();
            String registrationDateTime = Helper.getCurrentDateTime();


            if (nameS.equals("") || mobileS.equals("")) {
                ToastManager.showToast(context, Helper.EMPTY_FIELD);
            } else {
                dialogEffect.cancelDialog();

                // TODO: 1/27/2018 save unique roomy here
                Roomy roomy = new Roomy();
                roomy.setMobile(mobileS);
                roomy.setName(nameS);
                roomy.setRid(rid);
                roomy.setMacAddress(Helper.getMacAddr());
                roomy.setMobileLogged(mobileLogged);
                roomy.setRegistrationDateTime(registrationDateTime);

                db_ref.child(Helper.ROOMY).child(rid)
                        .setValue(roomy);

                ToastManager.showToast(context, Helper.REGISTERD);


                et_mobile.setText("");
                et_name.setText("");



                // TODO: 3/25/2018 delete pp if isTransfered
                if (isTransfer)
                {


                    deleteAfterExistingTransfer();
                    // TODO: 2/25/2018 reset transfer session
                    spe = sp.edit();
                    spe.putBoolean(SessionManager.IS_TRANSFER, false);
                    spe.apply();


                }


                onBackPressed();

            }

           // deletePaymentNpayTgAtIfTransfered();
        }else
        {
            Helper.showCheckInternet(this);
        }

    }


    void deleteAfterExistingTransfer() {

        if (CheckInternetReceiver.isOnline(this)) {

            dialogEffect.showDialog();

            db_ref.child(Helper.AFTER_TRANSFER).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dialogEffect.cancelDialog();
                    try {


                        for (DataSnapshot dataSnapshot1 :
                                dataSnapshot.getChildren()) {

                            PayTg payTg = dataSnapshot1.getValue(PayTg.class);

                            if (payTg.getMobileLogged().equals(mobileLogged)) {
                                db_ref.child(Helper.AFTER_TRANSFER)
                                        .child(payTg.getPayTgId())
                                        .removeValue();

                            }


                        }

                    } catch (Exception e) {
                        spe = sp.edit();
                        spe.putBoolean(SessionManager.IS_TRANSFER, false);
                        spe.apply();

                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else
        {
            Helper.showCheckInternet(context);
        }
    }

    public void backToHome(View view) {
        onBackPressed();
    }
}
