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
import com.busyprojects.roomies.helper.AnimationManager;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class EditRoomyActivity extends Activity {

    DialogEffect dialogEffect;
    DatabaseReference db_ref;
    Context context = EditRoomyActivity.this;
    String mobileLogged;
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    EditText et_name, et_mobile;

    AnimationManager animationManager;

    RelativeLayout rel_name, rel_mobile, rel_iv_roomy_home;
    ImageView iv_name, iv_call;
    String appColor, roomyToEdit, mobileToEdit, ridToEdit;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_roomy);
        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);
        Button but_save = findViewById(R.id.but_save);
        Button but_back = findViewById(R.id.but_back);

        iv_name = findViewById(R.id.iv_name);
        iv_call = findViewById(R.id.iv_call);

        rel_iv_roomy_home = findViewById(R.id.rel_iv_roomy_home);
        rel_name = findViewById(R.id.rel_name);
        rel_mobile = findViewById(R.id.rel_mobile);

        db_ref = Helper.getFirebaseDatabseRef();
        dialogEffect = new DialogEffect(context);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);
        roomyToEdit = sp.getString(SessionManager.SELECT_ROOMY_TO_EDIT, "");
        mobileToEdit = sp.getString(SessionManager.SELECT_MOBILE_TO_EDIT, "");
        ridToEdit = sp.getString(SessionManager.SELECT_RID_TO_EDIT, "");


        rel_iv_roomy_home.setBackgroundColor(Color.parseColor(appColor));
        but_save.setBackgroundColor(Color.parseColor(appColor));
        but_back.setBackgroundColor(Color.parseColor(appColor));

        animationManager = AnimationManager.getInstance();
        setApptheme();
        setOldData();
    }

    void setOldData() {
        et_name.setText(roomyToEdit);
        et_mobile.setText(mobileToEdit);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void setApptheme() {

        int profile = sp.getInt(SessionManager.IV_ROOMY, R.drawable.name);
        int mobile = sp.getInt(SessionManager.IV_MOBILE, R.drawable.call);

        iv_name.setImageResource(profile);
        iv_call.setImageResource(mobile);


        if (Build.VERSION.SDK_INT >= 21) {
            //SessionManager.setCursorColor(et_name,Color.parseColor(appColor));
            et_name.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(appColor)));
            et_mobile.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(appColor)));
        }
    }


    boolean isTransfer;

    public void saveRoomy(View view) {

        saveUpdatedRoommate();

    }

    void deletePaymentNpayTgAtIfTransfered() {


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


        db_ref.child(Helper.IS_TRANSFER).child(mobileLogged).setValue(false);

        Toast.makeText(context, "Data Cleared Successfully", Toast.LENGTH_SHORT).show();


    }


    void saveUpdatedRoommate() {

        if (CheckInternetReceiver.isOnline(this)) {

            String name = et_name.getText().toString();
            String mobile = et_mobile.getText().toString();


            if (name.equals("") || mobile.equals("")) {


                if (name.equals("")) {

                    animationManager.animateViewForEmptyField(rel_name, context);
                }

                if (mobile.equals("")) {
                    animationManager.animateViewForEmptyField(rel_mobile, context);
                }
            } else {

                // TODO: 3/18/2018 update roomy

                db_ref.child(Helper.ROOMY)
                        .child(ridToEdit)
                        .child(Helper.NAME).setValue(name);
                db_ref.child(Helper.ROOMY)
                        .child(ridToEdit)
                        .child(Helper.MOBILE).setValue(mobile);

                onBackPressed();

                Toast.makeText(context, "Roomy Updated successfully", Toast.LENGTH_SHORT).show();
            }

        } else {
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

        } else {
            Helper.showCheckInternet(context);
        }
    }

    public void backToHome(View view) {
        onBackPressed();
    }
}
