package com.busyprojects.roomies.roomyActivities;

import android.app.Activity;
import android.app.Dialog;
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
import com.busyprojects.roomies.helper.AnimationManager;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.RuntimePermissionsCs;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.busyprojects.roomies.pojos.master.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomRegistrationActivity extends Activity {
    Context context = RoomRegistrationActivity.this;

    EditText et_reg_room_no;
    TextView tv_log_reg_title;

    private AnimationManager animationManager;

    DatabaseReference db_ref;

    DialogEffect dialogEffect;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    RuntimePermissionsCs runtimePermissionsCs;
   // boolean CheckInternetReceiver.isOnline(this);
   // RelativeLayout rel_login_reg, rel_login, rel_register;

    boolean isTransfer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_register);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
//        CheckInternetReceiver.isOnline(this) = sp.getBoolean(SessionManager.IS_INTERNET, false);
        //CheckInternetReceiver.isOnline(this) = CheckInternetReceiver.isOnline(this);
        db_ref = Helper.getFirebaseDatabseRef();

        animationManager  = AnimationManager.getInstance();
        isTransfer = sp.getBoolean(SessionManager.IS_TRANSFER, false);
        dialogEffect = new DialogEffect(context);

        et_reg_room_no = findViewById(R.id.et_reg_room_no);
        tv_log_reg_title = findViewById(R.id.tv_log_reg_title);

      /*  rel_login_reg = findViewById(R.id.rel_log_reg);
        rel_login = findViewById(R.id.rel_login);
        rel_register = findViewById(R.id.rel_register);


        setInitialVisibility();
*/



    }


    void setInitialVisibility() {

        /*rel_login_reg.setVisibility(View.VISIBLE);
        rel_login.setVisibility(View.GONE);
        rel_register.setVisibility(View.GONE);*/
    }


    String mobileLogin,rooomyMobile;



    public void registerRoomNo(View view) {
        animationManager.animateButton(view,context);

if (CheckInternetReceiver.isOnline(this)) {
    final String roomNo = et_reg_room_no.getText().toString().trim();

    if (roomNo.equals(""))
    {
        et_reg_room_no.setText("");
        animationManager.animateViewForEmptyField(et_reg_room_no,context);
        Toast.makeText(context, "Please Check Empty Fields", Toast.LENGTH_SHORT).show();
    } else {

        if (CheckInternetReceiver.isOnline(context)) {

           dialogEffect.showDialog();
            db_ref.child(Helper.USER)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dialogEffect.cancelDialog();
                            List<String> userList = new ArrayList<>();

                            for (DataSnapshot dataSnapshot1 :
                                    dataSnapshot.getChildren()) {

                                User userDb = dataSnapshot1.getValue(User.class);

                                userList.add(userDb.getMobile());

                            }


                            if (userList.contains(roomNo)) {
                                Toast.makeText(context, "Room Already Exists", Toast.LENGTH_SHORT).show();


                                // TODO: 3/31/2018  take remoteIstransfer



                            } else {

                                // TODO: 2/25/2018 save room
                                String uid = Helper.randomString(10);
                                User user = new User();
                                user.setUid(uid);
                                user.setMobile(roomNo);


                                // saveRoomNo(User)
                                db_ref.child(Helper.USER).child(uid)
                                        .setValue(user);
                                Toast.makeText(context, "New Room Registered Successfully", Toast.LENGTH_SHORT).show();


                                Toast.makeText(context, "New Room ", Toast.LENGTH_SHORT).show();

                            }
                            // TODO: 3/31/2018 save roomy no in session

                            Helper.setRoomNoIsSession(context,roomNo);


                            startActivity(new Intent(context,RegistrationActivity.class));

                  /*      //save roomy
                        sp.edit().putString(SessionManager.MOBILE, roomNo).apply();


                        saveRoommate(roomNo);

                        Toast.makeText(context, "Roommate Registered Successfully", Toast.LENGTH_SHORT).show();
*/


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

}else
{
    Helper.showCheckInternet(context);

}
    }




    Dialog dialodDeleteTranserAlert;





    void deleteAfterExistingTransfer(final Roomy roomy) {

        if (CheckInternetReceiver.isOnline(this)) {

            dialogEffect.showDialog();

            db_ref.child(Helper.AFTER_TRANSFER).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dialogEffect.cancelDialog();
                    try {


                        for (DataSnapshot dataSnapshot1 :
                                dataSnapshot.getChildren()) {

                            PayTg payTg = dataSnapshot1.getValue(PayTg.class);

                            if (payTg.getMobileLogged().equals(roomy.getMobileLogged())) {
                                db_ref.child(Helper.AFTER_TRANSFER)
                                        .child(payTg.getPayTgId())
                                        .removeValue();

                            }


                        }

                    } catch (Exception e) {
                        spe = sp.edit();
                        spe.putBoolean(SessionManager.IS_TRANSFER, false);
                        spe.apply();
                        Helper.setRemoteIstransfer(roomy.getMobileLogged(),false);

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






}
