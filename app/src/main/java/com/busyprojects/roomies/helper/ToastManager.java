package com.busyprojects.roomies.helper;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.busyprojects.roomies.R;

/**
 * Created by sanket on 1/20/2018.
 */

public class ToastManager
{
    Context context;

    public ToastManager(Context context) {
        this.context = context;
    }

   public void showCustomToast(String message)
    {
        Toast toast = new Toast(context);

        View view =LayoutInflater.from(context).inflate(R.layout.toast_view,null);
       TextView tv_toast = view.findViewById(R.id.tv_toast);
                tv_toast.setText(message);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

    }


    public  static void showToast(Context context ,String tag)
    {
        switch (tag)
        {
            case Helper.REGISTERD:
                Toast.makeText(context, "One Roomy Registered", Toast.LENGTH_SHORT).show();
                break;
            case Helper.EMPTY_FIELD:
                Toast.makeText(context, "Please check All Empty Fields", Toast.LENGTH_SHORT).show();

        }

    }
}
