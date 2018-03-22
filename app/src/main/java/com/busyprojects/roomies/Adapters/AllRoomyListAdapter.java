package com.busyprojects.roomies.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.busyprojects.roomies.roomyActivities.EditRoomyActivity;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sanket on 12/23/2017.
 */

public class AllRoomyListAdapter extends ArrayAdapter {
    Context context;
    List<Roomy> roomyList;
    SharedPreferences sp;

    DialogEffect dialogEffect;
    String mobileLogged;
    DatabaseReference db_ref;

    Helper helper;

    String appColor;

    public AllRoomyListAdapter(Context context, List<Roomy> roomyList) {
        super(context, R.layout.row_all_roomy, roomyList);
        this.context = context;
        this.roomyList = roomyList;

        helper = new Helper();

        dialogEffect = new DialogEffect(context);

        sp = context.getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);

        mobileLogged = sp.getString(SessionManager.MOBILE, "");
        appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);

        db_ref = Helper.getFirebaseDatabseRef();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolderTg viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolderTg();

            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.row_all_roomy, parent, false);
            viewHolder.tv_roomy_mobile = convertView.findViewById(R.id.tv_roomy_mobile);
            viewHolder.tv_roomy_name_all_roomy = convertView.findViewById(R.id.tv_roomy_name_all_roomy);
            viewHolder.iv_call_roomy = convertView.findViewById(R.id.iv_call_roomy);
            viewHolder.iv_edit_roomy = convertView.findViewById(R.id.iv_edit_roomy);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderTg) convertView.getTag();
        }
        viewHolder.tv_roomy_name_all_roomy.setText(roomyList.get(position).getName());
        viewHolder.tv_roomy_mobile.setText(roomyList.get(position).getMobile() );


        viewHolder.iv_call_roomy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + roomyList.get(position).getMobile()));
                context.startActivity(intent);


            }
        });

        viewHolder.iv_edit_roomy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             Intent intentEditRoomy = new Intent(context, EditRoomyActivity.class);

           SharedPreferences.Editor spe = sp.edit();
             spe.putString(SessionManager.SELECT_ROOMY_TO_EDIT,roomyList.get(position).getName());
             spe.putString(SessionManager.SELECT_MOBILE_TO_EDIT,roomyList.get(position).getMobile());
             spe.putString(SessionManager.SELECT_RID_TO_EDIT,roomyList.get(position).getRid());
             spe.apply();

             intentEditRoomy.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
             context.startActivity(intentEditRoomy);


            }
        });


        return convertView;
    }


    class ViewHolderTg {
        TextView tv_roomy_name_all_roomy, tv_roomy_mobile;
        ImageView iv_call_roomy,iv_edit_roomy;
    }


}




