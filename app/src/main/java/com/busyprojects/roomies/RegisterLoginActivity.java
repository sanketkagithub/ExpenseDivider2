package com.busyprojects.roomies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterLoginActivity extends Activity {
    Context context = RegisterLoginActivity.this;

    EditText et_register, et_login;

    DatabaseReference db_ref;

    DialogEffect dialogEffect;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    RelativeLayout rel_login_reg, rel_login, rel_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);

        db_ref = Helper.getFirebaseDatabseRef();

        dialogEffect = new DialogEffect(context);

        et_login = findViewById(R.id.et_login);
        et_register = findViewById(R.id.et_register);

        rel_login_reg = findViewById(R.id.rel_log_reg);
        rel_login = findViewById(R.id.rel_login);
        rel_register = findViewById(R.id.rel_register);


        setInitialVisibility();
    }


    void setInitialVisibility() {

        rel_login_reg.setVisibility(View.VISIBLE);
        rel_login.setVisibility(View.GONE);
        rel_register.setVisibility(View.GONE);
    }


    public void loginRoomy(View view) {

        final String mobileLogin = et_login.getText().toString();

        if (mobileLogin.equals("")) {
            Toast.makeText(context, "Please Check Empty Fields", Toast.LENGTH_SHORT).show();

        } else {

            dialogEffect.showDialog();
            final List<String> userListMobile = new ArrayList<>();
            // TODO: 1/28/2018 validate login mobile
            db_ref.child(Helper.USER)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dialogEffect.cancelDialog();
                            userListMobile.clear();

                            for (DataSnapshot dataSnapshot1 :
                                    dataSnapshot.getChildren()) {
                                User user = dataSnapshot1.getValue(User.class);
                                userListMobile.add(user.getMobile());
                            }

                            if (userListMobile.contains(mobileLogin)) {

                                // TODO: 1/28/2018 save main roomate (user) in session
                                spe = sp.edit();
                                spe.putString(SessionManager.MOBILE, mobileLogin);
                                spe.apply();


                                startActivity(new Intent(context, HomeActivity.class));
                            } else {
                                Toast.makeText(context, "Invalid Login", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }


    }


    public void registerRoomy(View view) {
        String mobileReg = et_register.getText().toString();

        if (mobileReg.equals("")) {
            Toast.makeText(context, "Please Check Empty Fields", Toast.LENGTH_SHORT).show();
        } else {
            String uid = Helper.randomString(10);


            User user = new User();
            user.setUid(uid);
            user.setMobile(mobileReg);

            db_ref.child(Helper.USER).child(uid)
                    .setValue(user);
            Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show();

            sp.edit().putString(SessionManager.MOBILE, mobileReg).apply();
        }

    }

    void setLoginVisibilities() {
        String mobile = sp.getString(SessionManager.MOBILE, "");

        et_login.setText(mobile);

        rel_login_reg.setVisibility(View.GONE);

        rel_login.setVisibility(View.VISIBLE);
        rel_register.setVisibility(View.GONE);
    }

    void setRegisterationVisibilities() {


        rel_login_reg.setVisibility(View.GONE);

        rel_login.setVisibility(View.GONE);
        rel_register.setVisibility(View.VISIBLE);

    }


    public void showLogin(View view) {
        setLoginVisibilities();
    }


    public void showRegister(View view) {
        setRegisterationVisibilities();
    }
}
