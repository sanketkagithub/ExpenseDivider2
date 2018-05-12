package com.busyprojects.roomies.roomyActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.AnimationManager;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;

import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class RoomLoginActivity extends Activity {
    Context context = RoomLoginActivity.this;

    EditText et_log_room_no;
    TextView tv_log_reg_title;


    DatabaseReference db_ref;

    DialogEffect dialogEffect;
    private AnimationManager animationManager;

    SharedPreferences sp;

    //RelativeLayout rel_login_reg, rel_login, rel_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_login);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);

        db_ref = Helper.getFirebaseDatabseRef();

        dialogEffect = new DialogEffect(context);

        animationManager  = AnimationManager.getInstance();
        et_log_room_no = findViewById(R.id.et_log_room_no);

        tv_log_reg_title = findViewById(R.id.tv_log_reg_title);

//        rel_login_reg = findViewById(R.id.rel_log_reg);
//        rel_login = findViewById(R.id.rel_login);
//        rel_register = findViewById(R.id.rel_register);



    }







    public void roomLogin(View view)
    {

       String roomNoIp = et_log_room_no.getText().toString().trim();

       if (roomNoIp.equals(""))
       {
           et_log_room_no.setText("");

           animationManager.animateViewForEmptyField(et_log_room_no,context);
           Toast.makeText(context, "Please check Empty Fields", Toast.LENGTH_SHORT).show();
       }else
       {
           if (CheckInternetReceiver.isOnline(context)) {
               validateRoomNo(roomNoIp);
           }else
           {
               Helper.showCheckInternet(context);
           }
       }




    }


   void validateRoomNo(final String roomNoInp)
    {

        dialogEffect.showDialog();
        db_ref.child(Helper.USER)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dialogEffect.cancelDialog();
                        LinkedList roomyNoList = new LinkedList();

                        // TODO: 3/31/2018 add all roomNo from Db
                        for (DataSnapshot dataSnapshot1:
                             dataSnapshot.getChildren()) {

                          User user =  dataSnapshot1.getValue(User.class);

                            assert user != null;
                            roomyNoList.add(user.getMobile());

                        }


                        if (roomyNoList.contains(roomNoInp))
                        {
                            Helper.setRoomNoIsSession(context,roomNoInp);
                            startActivity(new Intent(context,LoginActivity.class));
                        }else
                        {
                            Toast.makeText(context, "This Room Number does not exist.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }




}
