package com.busyprojects.roomies;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.busyprojects.roomies.Adapters.HistoryDatesAdapter;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.History;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryDateActivity extends AppCompatActivity {

    ListView lv_history_dates;

    Context context = HistoryDateActivity.this;

    SharedPreferences sp;
    String mobileLogged;

    DatabaseReference dbRef;
    DialogEffect dialogEffect;

    ImageView iv_no_history_record_found;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_date);

        dialogEffect = new DialogEffect(this);
        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");

        lv_history_dates = findViewById(R.id.lv_history_dates);
       TextView tv_history_date = findViewById(R.id.tv_history_date);
        lv_history_dates.setVisibility(View.VISIBLE);
        iv_no_history_record_found = findViewById(R.id.iv_no_history_record_found);
        iv_no_history_record_found.setVisibility(View.GONE);
        dbRef = Helper.getFirebaseDatabseRef();

        setHistoryDatesList(lv_history_dates);

        String appColor =  sp.getString(SessionManager.APP_COLOR,SessionManager.DEFAULT_APP_COLOR);

        tv_history_date.setTextColor(Color.parseColor(appColor)); }


    List<History> historyList;

    void setHistoryDatesList(final ListView lv_history_dates) {

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


                        if (historyList.size()!=0) {
                            historyList = new Helper().getSortedHistoryList(historyList);

                            lv_history_dates.setVisibility(View.VISIBLE);
                            iv_no_history_record_found.setVisibility(View.GONE);
                            HistoryDatesAdapter historyDatesAdapter = new HistoryDatesAdapter(context, historyList);
                            lv_history_dates.setAdapter(historyDatesAdapter);
                        }else
                        {
                            lv_history_dates.setVisibility(View.GONE);
                            iv_no_history_record_found.setVisibility(View.VISIBLE);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    public void deleteHistoryList(View view) {
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


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
