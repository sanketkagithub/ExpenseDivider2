package com.busyprojects.roomies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.helper.ToastManager;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.google.firebase.database.DatabaseReference;

public class AddRoomyActivity extends AppCompatActivity {

    DialogEffect dialogEffect;
    DatabaseReference db_ref;
    Context context = AddRoomyActivity.this;
    String mobileLogged;
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    EditText et_name, et_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_roomy);
        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);

        db_ref = Helper.getFirebaseDatabseRef();
        dialogEffect = new DialogEffect(context);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");


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


        }

    }


}
