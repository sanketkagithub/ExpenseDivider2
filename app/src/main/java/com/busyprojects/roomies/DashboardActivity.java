package com.busyprojects.roomies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.busyprojects.roomies.AdminActivities.AllCustomersActivity;
import com.busyprojects.roomies.CustomerActivities.LoginRegisterActivity;

public class DashboardActivity extends AppCompatActivity {

    Context con = DashboardActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }


    public void go_to_customeractivity(View view)
    {
        startActivity(new Intent(con, LoginRegisterActivity.class));

    }


    public void go_to_adminactivity(View view)
    {

        startActivity(new Intent(con, AllCustomersActivity.class));


    }



}
