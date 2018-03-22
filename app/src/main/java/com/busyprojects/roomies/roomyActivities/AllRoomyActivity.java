package com.busyprojects.roomies.roomyActivities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.busyprojects.roomies.Adapters.AllRoomyListAdapter;
import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllRoomyActivity extends Activity {

    ListView lv_all_roomy;

    Context context = AllRoomyActivity.this;

    SharedPreferences sp;
    String mobileLogged;


    DatabaseReference dbRef;
    DialogEffect dialogEffect;

    List<Roomy> roomyList;

   // Button but_delete_payment;
    ImageView iv_no_history_record_found;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_roomy);

        dialogEffect = new DialogEffect(this);
        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");

        lv_all_roomy = findViewById(R.id.lv_all_roomy);
        iv_no_history_record_found = findViewById(R.id.iv_no_history_record_found);
       TextView tv_roomy_title = findViewById(R.id.tv_roomy_title);
        iv_no_history_record_found.setVisibility(View.GONE);
        dbRef = Helper.getFirebaseDatabseRef();

        String appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);
        int deletePayment =  sp.getInt(SessionManager.IV_DELETE,R.drawable.delete_payment);

        tv_roomy_title.setBackgroundColor(Color.parseColor(appColor));


          setRoomyList();

    }




    void setRoomyList() {

        if (CheckInternetReceiver.isOnline(this)) {
            dialogEffect.showDialog();
            dbRef.child(Helper.ROOMY).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dialogEffect.cancelDialog();
                    roomyList  = new ArrayList<>();


                    for (DataSnapshot dataSnapshot1 :
                            dataSnapshot.getChildren()) {

                        // TODO: 1/27/2018  add one by one roomy in list
                        Roomy roomy = dataSnapshot1.getValue(Roomy.class);

                        if (mobileLogged.equals(roomy.getMobileLogged())) {
                            roomyList.add(roomy);

                        }

                    }


                    if (roomyList.size()>0) {
                        // TODO: 1/27/2018 set roomy list in spinner
                        AllRoomyListAdapter roomySpinnerAdapterr = new AllRoomyListAdapter(context, roomyList);
                        lv_all_roomy.setAdapter(roomySpinnerAdapterr);
                    }else
                    {
                        roomyList.clear();
                        iv_no_history_record_found.setVisibility(View.VISIBLE);
                    }
                    //          getSelectedRoomy();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Helper.showCheckInternet(context);

        }
    }




    Dialog dialogDeleteAlert;
    public void showRoomyDeleteListAlert(View view)
    {

        if (CheckInternetReceiver.isOnline(this)) {
            //animationManager.animateViewForEmptyField();
            dialogDeleteAlert = new Dialog(this);
            dialogDeleteAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View v = LayoutInflater.from(context).inflate(R.layout.delete_roomy_alert, null);
            dialogDeleteAlert.setContentView(v);
            dialogDeleteAlert.setCancelable(false);
            dialogDeleteAlert.show();

        }else
        {
            Helper.showCheckInternet(context);

        }
    }



    public void deleteRoomy(View view)
    {
        if (CheckInternetReceiver.isOnline(this)) {
            deleteAllRoomy();
            dialogDeleteAlert.dismiss();
        }else
        {
            Helper.showCheckInternet(context);

        }
    }



    void deleteAllRoomy()
    {


        // TODO: 2/5/2018 delete current Payment

        dialogEffect.showDialog();
        dbRef.child(Helper.ROOMY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {


                    for (DataSnapshot dataSnapshot1 :
                            dataSnapshot.getChildren()) {

                        Roomy roomy = dataSnapshot1.getValue(Roomy.class);

                        if (roomy.getMobileLogged().equals(mobileLogged)) {

                            dbRef.child(Helper.ROOMY)
                                    .child(roomy.getRid())
                                    .removeValue();

                        }



                        roomyList.clear();


                        Toast.makeText(context, "Roomies deleted Successfully", Toast.LENGTH_SHORT).show();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

             //  dbRef.child(Helper.IS_TRANSFER).child(mobileLogged).setValue(false);



    }

    public void cancelRoomyDelete(View view)
    {

        dialogEffect.cancelDialog();
    }



}
