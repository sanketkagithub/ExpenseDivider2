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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.busyprojects.roomies.Adapters.HistoryDatesAdapter;
import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.History;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryDateActivity extends Activity {

    ListView lv_history_dates;

    Context context = HistoryDateActivity.this;

    SharedPreferences sp;
    String mobileLogged;

    DatabaseReference dbRef;
    DialogEffect dialogEffect;

    Button but_delete_payment;
    ImageView iv_no_history_record_found;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_date);

        dialogEffect = new DialogEffect(this);
        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");

        lv_history_dates = findViewById(R.id.lv_history_dates);
        but_delete_payment = findViewById(R.id.but_delete_payment);
        TextView tv_history_date = findViewById(R.id.tv_history_date);
        lv_history_dates.setVisibility(View.VISIBLE);
        iv_no_history_record_found = findViewById(R.id.iv_no_history_record_found);
        iv_no_history_record_found.setVisibility(View.GONE);
        dbRef = Helper.getFirebaseDatabseRef();

        setHistoryDatesList(lv_history_dates);

        String appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);
        int deletePayment =  sp.getInt(SessionManager.IV_DELETE,R.drawable.delete_payment);

        tv_history_date.setBackgroundColor(Color.parseColor(appColor));
        but_delete_payment.setBackground(getResources().getDrawable(deletePayment));
    }


    List<History> historyList;

    void setHistoryDatesList(final ListView lv_history_dates) {

        if (CheckInternetReceiver.isOnline(this)) {
            dialogEffect.showDialog();
            dbRef.child(Helper.HISTORY)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dialogEffect.cancelDialog();
                            historyList = new ArrayList<>();

                            for (DataSnapshot dataSnapshot1 :
                                    dataSnapshot.getChildren()) {


                                History history = dataSnapshot1.getValue(History.class);

                                if (mobileLogged.equals(history.getMobileLogged())) {
                                    historyList.add(history);
                                }


                            }


                            if (historyList.size() != 0) {
                                historyList = new Helper().getSortedHistoryList(historyList);

                                lv_history_dates.setVisibility(View.VISIBLE);
                                iv_no_history_record_found.setVisibility(View.GONE);

                                Collections.reverse(historyList);
                                HistoryDatesAdapter historyDatesAdapter = new HistoryDatesAdapter(context, historyList);
                                lv_history_dates.setAdapter(historyDatesAdapter);
                            } else {
                                lv_history_dates.setVisibility(View.GONE);
                                iv_no_history_record_found.setVisibility(View.VISIBLE);

                                but_delete_payment.setVisibility(View.GONE);
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


    public void deleteHistoryList(View view) {

    if (CheckInternetReceiver.isOnline(this)) {
        dialogEffect.showDialog();
        dbRef.child(Helper.HISTORY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dialogEffect.cancelDialog();
                        try {


                            for (DataSnapshot dataSnapshot1 :
                                    dataSnapshot.getChildren()) {

                                History history = dataSnapshot1.getValue(History.class);

                                if (history.getMobileLogged().equals(mobileLogged)) {
                                    dbRef.child(Helper.HISTORY).child(history.getHid())
                                            .removeValue();
                                }
                                Toast.makeText(context, "History deleted successfully", Toast.LENGTH_SHORT).show();


                            }
                        } catch (Exception e) {
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

    String hid;
    History history;
    Dialog dialogDeleteAlert;
    public void showDeleteRoomyListAlert(View view) {

        if (CheckInternetReceiver.isOnline(this)) {
            //animationManager.animateViewForEmptyField();
            dialogDeleteAlert = new Dialog(this);
            dialogDeleteAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View v = LayoutInflater.from(context).inflate(R.layout.delete_alert, null);
            dialogDeleteAlert.setContentView(v);
            dialogDeleteAlert.setCancelable(false);
            dialogDeleteAlert.show();

        }else
        {
            Helper.showCheckInternet(context);

        }
    }


}
