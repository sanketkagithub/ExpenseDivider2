package com.busyprojects.roomies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.busyprojects.roomies.helper.AnimationManager;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.Roomy;
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


    AnimationManager animationManager = null;


    int totalRoomates;

    DialogEffect dialogEffect;
    Context context = HomeActivity.this;


    String mobileLogged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        animationManager = AnimationManager.getInstance();

        dialogEffect = new DialogEffect(context);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");
        totalRoomates = sp.getInt(SessionManager.TOTAL_ROOMMATES, 0);


        fb_db = FirebaseDatabase.getInstance();
        db_ref = fb_db.getReference();

        setTotalRoomieesCountInSession();
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

        startActivity(new Intent(context, TestPaymentActivity.class));
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


    @Override
    public void onBackPressed() {
        // super.onBackPressed();

    }


    public void viewHistory(View view)
    {
        startActivity(new Intent(this,HistoryDateActivity.class));
    }
}




