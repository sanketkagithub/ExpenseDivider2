package com.busyprojects.roomies;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.busyprojects.roomies.helper.AnimationManager;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity {


    FirebaseDatabase fb_db;
    DatabaseReference db_ref, cc_ref, key_ref_cc, cc_inner_ref;        // TODO : one database ref

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    TextView tv_login_message, tv_notification_count;
    ImageView iv_notification;
    AnimationManager animationManager = null;


    int totalRoomates;

    DialogEffect dialogEffect;
    Context context = HomeActivity.this;


    String mobileLogged;

    RelativeLayout rel_iv_roomy_home;
    Button but_add_roomy;
    Button but_pay;
    Button but_view_payment;
    Button but_history;
    Button but_logout;
    String appColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        animationManager = AnimationManager.getInstance();

        dialogEffect = new DialogEffect(context);
        tv_login_message = findViewById(R.id.tv_login_message);

        rel_iv_roomy_home = findViewById(R.id.rel_iv_roomy_home);

        but_add_roomy = findViewById(R.id.but_add_roomy);

        iv_notification = findViewById(R.id.iv_notification);
        tv_notification_count = findViewById(R.id.tv_notification_count);

        iv_notification.setVisibility(View.GONE);
        tv_notification_count.setVisibility(View.GONE);

        but_pay = findViewById(R.id.but_pay);
        but_view_payment = findViewById(R.id.but_view_payment);
        but_history = findViewById(R.id.but_history);
        but_logout = findViewById(R.id.but_logout);
        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");
        totalRoomates = sp.getInt(SessionManager.TOTAL_ROOMMATES, 0);

        appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);

        fb_db = FirebaseDatabase.getInstance();
        db_ref = fb_db.getReference();

        setTotalRoomieesCountInSession();

        tv_login_message.setText("Every Roommate of this room must login with " + mobileLogged);

        tv_login_message.setTextColor(Color.parseColor(appColor));

        //  rel_iv_roomy_home.setBackgroundColor(Color.parseColor("#F5F1F1"));

        // TODO: 2/27/2018 save mac address 
       String macAddress = Helper.getMacAddr();
       spe= sp.edit();
        spe.putString(SessionManager.MAC_ADDRESS,macAddress);
        spe.apply();


        setItemsColor();

        getNewNotifyObjects();
    }


    List<String> roomysList;

    private void setTotalRoomieesCountInSession() {

        dialogEffect.showDialog();
        db_ref.child(Helper.ROOMY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                roomysList = new ArrayList<>();
                dialogEffect.cancelDialog();


                for (DataSnapshot dataSnapshot1 :
                        dataSnapshot.getChildren()) {

                    // TODO: 1/27/2018  add one by one roomy in list
                    Roomy roomy = dataSnapshot1.getValue(Roomy.class);


                    if (mobileLogged.equals(roomy.getMobileLogged())) {
                        if (roomy != null) {
                            roomysList.add(roomy.getName());
                        }
                    }

                }


                // TODO: 2/24/2018 save roomies  count

                spe = sp.edit();
                spe.putInt(SessionManager.TOTAL_ROOMMATES, roomysList.size());
                spe.apply();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void addRoomy(View view) {

        startActivity(new Intent(this, AddRoomyActivity.class));


    }


    public void payNow(View view) {

        startActivity(new Intent(this, PayNowActivity.class));


    }


    public void viewPayment(View view) {
        // TODO: 1/27/2018 get All Sessions list  firstly

        startActivity(new Intent(context, PaymentActivity.class));
//        rel_iv_roomy_home.setVisibility(View.GONE);
//
//        rel_add_roomy_layout.setVisibility(View.GONE);
//
//        rel_add_roomy.setVisibility(View.GONE);
//        rel_pay_roomy.setVisibility(View.GONE);
//        rel_view_payment.setVisibility(View.GONE);
//        rel_view_history.setVisibility(View.GONE);
//        rel_all_paymen_lay.setVisibility(View.VISIBLE);
//
//
//        setPaymentsListAndPayTgList();
//
//        but_divide.setVisibility(View.VISIBLE);
//        tv_each_payment.setVisibility(View.GONE);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

        finishAffinity();
    }


    public void viewHistory(View view) {
        startActivity(new Intent(this, HistoryDateActivity.class));
    }

    public void logout(View view) {

        spe = sp.edit();
        spe.clear();
        spe.apply();

        startActivity(new Intent(this, RegisterLoginActivity.class));
        finish();
    }

    Dialog dialog;

    public void changeAppColor(View view) {
        dialog = new Dialog(context);
        View v = LayoutInflater.from(context).inflate(R.layout.change_color_layout, null);
        dialog.setContentView(v);


        dialog.show();


    }


    public void selectedColor(View view) {

        spe = sp.edit();


        Resources resources = getResources();
        Toast.makeText(context, "==> " + getResources().getColor(R.color.done_light), Toast.LENGTH_SHORT).show();

        switch (view.getId()) {
            case R.id.iv_sky_blue:

                spe.putString(SessionManager.APP_COLOR, resources.getString(R.string.sky_bluish));
                spe.putInt(SessionManager.IV_ROOMY, R.drawable.profile_bluish);
                spe.putInt(SessionManager.IV_MOBILE, R.drawable.mob_blu);

                spe.putInt(SessionManager.IV_RUPEE, R.drawable.rupee_blu);
                spe.putInt(SessionManager.IV_PAYING_ITEM, R.drawable.cart_blu);

                spe.putInt(SessionManager.IV_DELETE, R.drawable.del_blu);
                spe.putInt(SessionManager.IV_TRANSFER, R.drawable.tp_blu);


                spe.apply();

                appColor = sp.getString(SessionManager.APP_COLOR, resources.getString(R.string.default_color));
                setItemsColor();
                dialog.dismiss();
                break;

            case R.id.iv_greenish:
                spe.putString(SessionManager.APP_COLOR, resources.getString(R.string.greenish));
                spe.putInt(SessionManager.IV_ROOMY, R.drawable.prof_greenish);
                spe.putInt(SessionManager.IV_MOBILE, R.drawable.mob_green);
                spe.putInt(SessionManager.IV_RUPEE, R.drawable.rupee_green);
                spe.putInt(SessionManager.IV_PAYING_ITEM, R.drawable.cart_green);

                spe.putInt(SessionManager.IV_DELETE, R.drawable.del_green);
                spe.putInt(SessionManager.IV_TRANSFER, R.drawable.tp_green);
                spe.apply();

                appColor = sp.getString(SessionManager.APP_COLOR, resources.getString(R.string.default_color));
                setItemsColor();
                dialog.dismiss();
                break;

            case R.id.iv_yellowish:
                spe.putString(SessionManager.APP_COLOR, resources.getString(R.string.yellowish));
                spe.putInt(SessionManager.IV_ROOMY, R.drawable.prof_yellowish);
                spe.putInt(SessionManager.IV_MOBILE, R.drawable.mob_yellow);

                spe.putInt(SessionManager.IV_RUPEE, R.drawable.rupee_yell);
                spe.putInt(SessionManager.IV_PAYING_ITEM, R.drawable.cart_yellow);

                spe.putInt(SessionManager.IV_DELETE, R.drawable.del_yell);
                spe.putInt(SessionManager.IV_TRANSFER, R.drawable.tp_yell);
                spe.apply();
                appColor = sp.getString(SessionManager.APP_COLOR, resources.getString(R.string.default_color));

                setItemsColor();
                dialog.dismiss();
                break;

            case R.id.iv_blackish:
                spe.putString(SessionManager.APP_COLOR, resources.getString(R.string.blackish));
                spe.putInt(SessionManager.IV_ROOMY, R.drawable.prof_blackish);
                spe.putInt(SessionManager.IV_MOBILE, R.drawable.mob_black);

                spe.putInt(SessionManager.IV_RUPEE, R.drawable.rupee_black);
                spe.putInt(SessionManager.IV_PAYING_ITEM, R.drawable.cart_black);

                spe.putInt(SessionManager.IV_DELETE, R.drawable.del_black);
                spe.putInt(SessionManager.IV_TRANSFER, R.drawable.tp_black);

                spe.apply();

                appColor = sp.getString(SessionManager.APP_COLOR, resources.getString(R.string.default_color));
                setItemsColor();
                dialog.dismiss();
                break;

            case R.id.iv_redish:
                spe.putString(SessionManager.APP_COLOR, resources.getString(R.string.redish));
                spe.putInt(SessionManager.IV_ROOMY, R.drawable.prof_redish);
                spe.putInt(SessionManager.IV_MOBILE, R.drawable.mob_red);

                spe.putInt(SessionManager.IV_RUPEE, R.drawable.rupee_red);
                spe.putInt(SessionManager.IV_PAYING_ITEM, R.drawable.cart_red);

                spe.putInt(SessionManager.IV_DELETE, R.drawable.del_red);
                spe.putInt(SessionManager.IV_TRANSFER, R.drawable.tp_red);
                spe.apply();

                appColor = sp.getString(SessionManager.APP_COLOR, resources.getString(R.string.default_color));

                setItemsColor();

                dialog.dismiss();
                break;
            case R.id.iv_pinkish:
                spe.putString(SessionManager.APP_COLOR, resources.getString(R.string.pinkish));
                spe.putInt(SessionManager.IV_ROOMY, R.drawable.prof_pinkish);
                spe.putInt(SessionManager.IV_MOBILE, R.drawable.mob_pink);

                spe.putInt(SessionManager.IV_RUPEE, R.drawable.rupee_pink);
                spe.putInt(SessionManager.IV_PAYING_ITEM, R.drawable.cart_pink);

                spe.putInt(SessionManager.IV_DELETE, R.drawable.del_pink);
                spe.putInt(SessionManager.IV_TRANSFER, R.drawable.tp_pink);

                spe.apply();

                appColor = sp.getString(SessionManager.APP_COLOR, resources.getString(R.string.default_color));

                dialog.dismiss();
                setItemsColor();
                break;
            case R.id.iv_voiletish:
                spe.putString(SessionManager.APP_COLOR, resources.getString(R.string.voiletish));
                spe.putInt(SessionManager.IV_ROOMY, R.drawable.prof_violetish);
                spe.putInt(SessionManager.IV_MOBILE, R.drawable.mob_violet);

                spe.putInt(SessionManager.IV_RUPEE, R.drawable.rupee_vio);
                spe.putInt(SessionManager.IV_PAYING_ITEM, R.drawable.cart_vio);

                spe.putInt(SessionManager.IV_DELETE, R.drawable.del_vio);
                spe.putInt(SessionManager.IV_TRANSFER, R.drawable.tp_vio);
                spe.apply();


                appColor = sp.getString(SessionManager.APP_COLOR, resources.getString(R.string.default_color));

                setItemsColor();
                dialog.dismiss();
                break;


        }

    }

    void setItemsColor() {
        tv_login_message.setTextColor(Color.parseColor(appColor));

        rel_iv_roomy_home.setBackgroundColor(Color.parseColor(appColor));
        but_add_roomy.setBackgroundColor(Color.parseColor(appColor));
        but_pay.setBackgroundColor(Color.parseColor(appColor));
        but_view_payment.setBackgroundColor(Color.parseColor(appColor));
        but_history.setBackgroundColor(Color.parseColor(appColor));
        but_logout.setBackgroundColor(Color.parseColor(appColor));

    }

    List<Payment> paymentListNotify;

    void getNewNotifyObjects() {
        db_ref.child(Helper.PAYMENT_NOTIFICATION)
                .child(mobileLogged)
                .child(Helper.PAYMENT_LIST)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        paymentListNotify = new ArrayList<>();


                        for (DataSnapshot dataSnapshot1 :
                                dataSnapshot.getChildren()) {

                            try {
                                Payment payment = dataSnapshot1.getValue(Payment.class);
                                paymentListNotify.add(payment);

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }


                        }

                        if (paymentListNotify.size() > 0) {

                            iv_notification.setVisibility(View.VISIBLE);
                            tv_notification_count.setVisibility(View.VISIBLE);


                            tv_notification_count.setText(paymentListNotify.size() + "");
                        }else
                        {
                            iv_notification.setVisibility(View.GONE);
                            tv_notification_count.setVisibility(View.GONE);

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


    public void showNotifiedData(View view) {
        db_ref.child(Helper.PAYMENT_NOTIFICATION)
                .child(mobileLogged)
                .removeValue();

        startActivity(new Intent(this, PaymentActivity.class));

    }


}




