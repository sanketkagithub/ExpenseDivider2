package com.busyprojects.roomies.roomyActivities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AppInfoActivity extends Activity {

    TextView tv_app_info;

    Context context = AppInfoActivity.this;

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
        setContentView(R.layout.activity_app_info);

        dialogEffect = new DialogEffect(this);
        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");

        tv_app_info = findViewById(R.id.tv_app_info);

        dbRef = Helper.getFirebaseDatabseRef();


        String appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);
        int deletePayment = sp.getInt(SessionManager.IV_DELETE, R.drawable.delete_payment);


        setAppInfo();

    }


    void setAppInfo() {
        dialogEffect.showDialog();
        dbRef.child(Helper.APP_INFO)
                .child(Helper.INFO)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dialogEffect.cancelDialog();
                        String appInfoText;
                        try {

                            appInfoText = dataSnapshot.getValue(String.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            appInfoText = "omg";
                        }

                        tv_app_info.setText(appInfoText);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


}
