package com.busyprojects.roomies.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.SessionManager;

import java.util.List;

/**
 * Created by sanket on 1/27/2018.
 */

public class RoomySpinnerAdapterr extends ArrayAdapter {
    Context context;
    List<String> listRoomName;

    String appColor;
    SharedPreferences sp;

    public RoomySpinnerAdapterr(Context context, List<String> listRoomName) {
        super(context, R.layout.spinner_text, listRoomName);
        this.context = context;
        this.listRoomName = listRoomName;

        sp = context.getSharedPreferences(SessionManager.FILE_WTC, Context.MODE_PRIVATE);
        appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);


    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolderRoomySpinner viewHolderRoomySpinner = null;
        if (convertView == null) {
            viewHolderRoomySpinner = new ViewHolderRoomySpinner();

            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_text, parent, false);

            viewHolderRoomySpinner.tvRoomyName = convertView.findViewById(R.id.tv_spin_roomy_name);
            convertView.setTag(viewHolderRoomySpinner);

        } else {
            viewHolderRoomySpinner = (ViewHolderRoomySpinner) convertView.getTag();

        }
        viewHolderRoomySpinner.tvRoomyName.setText(listRoomName.get(position));
        viewHolderRoomySpinner.tvRoomyName.setTextColor(Color.parseColor(appColor));

        return convertView;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolderRoomySpinner viewHolderRoomySpinner = null;
        if (convertView == null) {
            viewHolderRoomySpinner = new ViewHolderRoomySpinner();

            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_text, parent, false);

            viewHolderRoomySpinner.tvRoomyName = convertView.findViewById(R.id.tv_spin_roomy_name);

            convertView.setTag(viewHolderRoomySpinner);

        } else {
            viewHolderRoomySpinner = (ViewHolderRoomySpinner) convertView.getTag();

        }
        viewHolderRoomySpinner.tvRoomyName.setText(listRoomName.get(position));


        return convertView;
    }
}

class ViewHolderRoomySpinner {
    TextView tvRoomyName;
}
