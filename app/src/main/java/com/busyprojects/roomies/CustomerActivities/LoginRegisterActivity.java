package com.busyprojects.roomies.CustomerActivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.busyprojects.roomies.R;

public class LoginRegisterActivity extends AppCompatActivity {

    Context con = LoginRegisterActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
    }

    public void go_to_login_activity(View view)
    {

        startActivity(new Intent(con,LoginCusActivity.class));
    }


    public void go_to_register_activity(View view)
    {

        startActivity(new Intent(con,RegisterCusActivity.class));
    }
}
