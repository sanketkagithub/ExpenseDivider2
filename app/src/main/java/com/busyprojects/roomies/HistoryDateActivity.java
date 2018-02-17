package com.busyprojects.roomies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.busyprojects.roomies.Adapters.HistoryDatesAdapter;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_date);

        sp = getSharedPreferences(SessionManager.FILE_WTC,MODE_PRIVATE);
       mobileLogged = sp.getString(SessionManager.MOBILE,"");

        lv_history_dates = findViewById(R.id.lv_history_dates);
     dbRef = Helper.getFirebaseDatabseRef();

    setHistoryDatesList(lv_history_dates);
    }


     List<History> historyList;

    void setHistoryDatesList(final ListView lv_history_dates)
    {

        dbRef.child(Helper.HISTORY)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        historyList = new ArrayList<>();

                        for (DataSnapshot dataSnapshot1:
                             dataSnapshot.getChildren()) {


                        History history =    dataSnapshot1.getValue(History.class);

                            if (mobileLogged.equals(history.getMobileLogged())) {
                                historyList.add(history);
                            }


                        }


                      historyList = new Helper().getSortedHistoryList(historyList);

                        HistoryDatesAdapter historyDatesAdapter = new HistoryDatesAdapter(context,historyList);
                        lv_history_dates.setAdapter(historyDatesAdapter);



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



    }


}
