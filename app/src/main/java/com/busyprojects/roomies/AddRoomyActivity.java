package com.busyprojects.roomies;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.helper.ToastManager;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AddRoomyActivity extends AppCompatActivity {

    DialogEffect dialogEffect;
    DatabaseReference db_ref;
    Context context = AddRoomyActivity.this;
    String mobileLogged;
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    EditText et_name, et_mobile;

    RelativeLayout rel_iv_roomy_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_roomy);
        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);
       Button but_save = findViewById(R.id.but_save);
       Button but_back = findViewById(R.id.but_back);
        rel_iv_roomy_home = findViewById(R.id.rel_iv_roomy_home);

        db_ref = Helper.getFirebaseDatabseRef();
        dialogEffect = new DialogEffect(context);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");
String appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);



        rel_iv_roomy_home.setBackgroundColor(Color.parseColor(appColor));
        but_save.setBackgroundColor(Color.parseColor(appColor));
        but_back.setBackgroundColor(Color.parseColor(appColor));
    }


    public void saveRoomy(View view) {


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
            roomy.setMobileLogged(mobileLogged);
            roomy.setRegistrationDateTime(registrationDateTime);

            db_ref.child(Helper.ROOMY).child(rid)
                    .setValue(roomy);

            ToastManager.showToast(context, Helper.REGISTERD);


            et_mobile.setText("");
            et_name.setText("");


            // TODO: 2/25/2018 reset transfer session 
            spe = sp.edit();
            spe.putBoolean(SessionManager.IS_TRANSFER, false);
            spe.apply();



          deleteAfterExistingTransfer();

        }

    }



    void deleteAfterExistingTransfer()
    {

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

    }

    public void backToHome(View view)
    {
        onBackPressed();
    }
}
