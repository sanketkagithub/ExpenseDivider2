package com.busyprojects.roomies.roomyActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.RuntimePermissionsCs;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class LoginActivity extends Activity {
    Context context = LoginActivity.this;

    EditText et_roommate_login;
    TextView tv_log_reg_title;


    DatabaseReference db_ref;

    DialogEffect dialogEffect;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    RuntimePermissionsCs runtimePermissionsCs;
    // boolean CheckInternetReceiver.isOnline(this);

    String roomNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);

        db_ref = Helper.getFirebaseDatabseRef();

        dialogEffect = new DialogEffect(context);

        roomNo = Helper.getRoomNoFromSession(context);

        et_roommate_login = findViewById(R.id.et_roommate_login);

        tv_log_reg_title = findViewById(R.id.tv_log_reg_title);


        if (Build.VERSION.SDK_INT >= 23) {
            runtimePermissionsCs = new RuntimePermissionsCs(this);
            runtimePermissionsCs.getPermissions();
        }
    }


    void clearFields() {

        et_roommate_login.setText("");

    }


    public void loginRoomy(View view) {

        String roomyNoIp = et_roommate_login.getText().toString().trim();

        if (roomyNoIp.equals("")) {
            Toast.makeText(context, "Please check All Empty Fields.", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {


            if (CheckInternetReceiver.isOnline(context)) {
                validateRoomyMobile(roomyNoIp);
            } else {
                Helper.showCheckInternet(context);
            }
        }


    }


    void validateRoomyMobile(final String roomyMobileIp) {

        db_ref.child(Helper.ROOMY)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        List<String> roomyMobList = new LinkedList<>();
                        List<Roomy> roomyList = new LinkedList<>();

                        for (DataSnapshot dataSnapshot1 :
                                dataSnapshot.getChildren()) {


                            Roomy roomy = dataSnapshot1.getValue(Roomy.class);

                            if (roomy.getMobileLogged().equals(roomNo)) {
                                roomyMobList.add(roomy.getMobile());
                                roomyList.add(roomy);
                            }

                        }


                        if (roomyMobList.contains(roomyMobileIp)) {
                            Helper.setRoomyMobileIsSession(context, roomyMobileIp);

                            for (int i = 0; i < roomyList.size(); i++) {

                                if (roomyList.get(i).getMobile().equals(roomyMobileIp)) {
                                    Helper.setRoomyNameIsSession(context, roomyList.get(i).getName());

                                }

                            }


                            startActivity(new Intent(context, HomeActivity.class));


                        } else {
                            Toast.makeText(context, "Invalid Roomate Mobile", Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


}
