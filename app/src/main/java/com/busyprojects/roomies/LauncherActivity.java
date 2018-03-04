package com.busyprojects.roomies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.busyprojects.roomies.helper.SessionManager;

public class LauncherActivity extends Activity {

    Context context = LauncherActivity.this;
    ImageView iv_centre_right, iv_centre_left, iv_centre_down;
    SharedPreferences sp;
    String mobileLogged, appColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        iv_centre_right = findViewById(R.id.iv_centre_right);
        iv_centre_left = findViewById(R.id.iv_centre_left);
        iv_centre_down = findViewById(R.id.iv_centre_down);
        final RelativeLayout rel_launcher_back = findViewById(R.id.rel_launcher_back);

          getScreenInfo();

        Resources resources = getResources();

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");
        appColor = sp.getString(SessionManager.APP_COLOR, resources.getString(R.string.default_color));

        rel_launcher_back.setBackgroundColor(Color.parseColor(appColor));


        animateSharing();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {




                if (mobileLogged.equals("")) {
                    startActivity(new Intent(context, RegisterLoginActivity.class));

                } else {

                    startActivity(new Intent(context, HomeActivity.class));
                }


                finish();
            }
        }, 1500);
    }
    public void getScreenInfo(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

     int   heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        float density = metrics.density;
        int    densityDpi = metrics.densityDpi;

        System.out.println("heightPixels " +heightPixels + "\n" + "widthPixels " +widthPixels + "\n"

                + density + "\n" + densityDpi);

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
