package com.busyprojects.roomies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LauncherActivity extends Activity {

    Context context = LauncherActivity.this;
    ImageView iv_centre_right, iv_centre_left, iv_centre_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        iv_centre_right = findViewById(R.id.iv_centre_right);
        iv_centre_left = findViewById(R.id.iv_centre_left);
        iv_centre_down = findViewById(R.id.iv_centre_down);


        animateSharing();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(context, RegisterLoginActivity.class));
                finish();
            }
        }, 1500);
    }

    void animateSharing() {

        iv_centre_right.startAnimation(AnimationUtils.loadAnimation(this, R.anim.share_money_launcher_right));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_centre_left.startAnimation(AnimationUtils.loadAnimation(LauncherActivity.this, R.anim.share_money_launcher_left));


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_centre_down.startAnimation(AnimationUtils.loadAnimation(LauncherActivity.this, R.anim.share_money_launcher_down));
                    }
                }, 500);
            }
        }, 500);


    }

}
