package com.busyprojects.roomies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.roomyActivities.HistoryDetailsActivity;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.History;

import java.util.List;

/**
 * Created by sanket on 12/23/2017.
 */

public class HistoryDatesAdapter extends ArrayAdapter {
    Context context;
    List<History> historyList;
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    String emailNewUser;

    Animation animation = null;

    public HistoryDatesAdapter(Context context, List<History> historyList) {
        super(context, R.layout.row_history_date, historyList);
        this.context = context;
        this.historyList = historyList;


    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolderHistoryDate viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolderHistoryDate();

            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.row_history_date, parent, false);
            viewHolder.tv_history_date = convertView.findViewById(R.id.tv_history_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderHistoryDate) convertView.getTag();
        }
        viewHolder.tv_history_date.setText(historyList.get(position).getDateTime());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sp = context.getSharedPreferences(SessionManager.FILE_WTC,Context.MODE_PRIVATE);
                spe =sp.edit();
                spe.putString(SessionManager.HID,historyList.get(position).getHid());
                spe.apply();
                context.startActivity(new Intent(context, HistoryDetailsActivity.class));


            }
        });


        return convertView;
    }


}


class ViewHolderHistoryDate {
    TextView tv_history_date;


}


