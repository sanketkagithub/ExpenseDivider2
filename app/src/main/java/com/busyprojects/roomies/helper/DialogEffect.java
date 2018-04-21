package com.busyprojects.roomies.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.busyprojects.roomies.R;

/**
 * Created by sanket on 1/14/2018.
 */

public class DialogEffect {
    Dialog dialog;
    Context context;

    public DialogEffect(Context context) {
        this.context = context;
        dialog = new Dialog(context);
    }

    public void showDialog()
    {
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View v = LayoutInflater.from(context).inflate(R.layout.progress_bar, null);

           /*ImageView iv_centre_right = v.findViewById(R.id.iv_centre_right);
           ImageView iv_centre_left = v.findViewById(R.id.iv_centre_left);
           ImageView iv_centre_down = v.findViewById(R.id.iv_centre_down);
           */

            dialog.setContentView(v);
            dialog.setCancelable(false);

         //   animateSharing(iv_centre_right,iv_centre_left,iv_centre_down);
            dialog.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        }

    void animateSharing(ImageView iv_centre_right,ImageView iv_centre_left,ImageView iv_centre_down) {

        iv_centre_right.startAnimation(AnimationUtils.loadAnimation(context, R.anim.share_money_launcher_right_prog));
        iv_centre_left.startAnimation(AnimationUtils.loadAnimation(context, R.anim.share_money_launcher_left_prog));
        iv_centre_down.startAnimation(AnimationUtils.loadAnimation(context, R.anim.share_money_launcher_down_prog));



    }


    public void cancelDialog() {
        dialog.dismiss();
    }


}
