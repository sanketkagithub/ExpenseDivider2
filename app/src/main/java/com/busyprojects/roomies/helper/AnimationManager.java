package com.busyprojects.roomies.helper;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.busyprojects.roomies.R;

/**
 * Created by sanket on 2/10/2018.
 */

public class AnimationManager
{
   static AnimationManager animationManager=null;

    private AnimationManager()
    {

    }

 public  static AnimationManager getInstance()
   {
       if (animationManager==null) {
      animationManager =new AnimationManager();
       }
       return animationManager;

   }

  public   void animateViewForEmptyField(View view, Context context)
    {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.invalid_login));
    }


}
