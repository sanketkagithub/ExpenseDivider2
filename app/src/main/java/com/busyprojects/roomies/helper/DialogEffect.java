package com.busyprojects.roomies.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

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
            dialog.setContentView(v);
            dialog.setCancelable(false);
            dialog.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        }

    public void cancelDialog() {
        dialog.dismiss();
    }


}
