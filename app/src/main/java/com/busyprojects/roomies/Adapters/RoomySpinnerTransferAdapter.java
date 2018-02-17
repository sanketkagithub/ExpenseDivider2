package com.busyprojects.roomies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.pojos.master.Roomy;

import java.util.List;

/**
 * Created by sanket on 1/27/2018.
 */

public class RoomySpinnerTransferAdapter extends ArrayAdapter
{Context context;
 List<Roomy> listRoomName;


    public RoomySpinnerTransferAdapter(Context context, List<Roomy> listRoomName) {
        super(context, R.layout.spinner_text, listRoomName);
        this.context = context;
        this.listRoomName = listRoomName;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolderRoomySpinner viewHolderRoomySpinner = null;
        if (convertView==null)
        {
          viewHolderRoomySpinner  = new ViewHolderRoomySpinner();

           convertView = LayoutInflater.from(context).inflate(R.layout.spinner_text,parent,false);

          viewHolderRoomySpinner.tvRoomyName = convertView.findViewById(R.id.tv_spin_roomy_name);
            convertView.setTag(viewHolderRoomySpinner);

        }else
        {
            viewHolderRoomySpinner = (ViewHolderRoomySpinner) convertView.getTag();

        }
            viewHolderRoomySpinner.tvRoomyName.setText(listRoomName.get(position).getName());



        return  convertView;
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolderRoomyTransferSpinner viewHolderRoomySpinner = null;
        if (convertView==null)
        {
            viewHolderRoomySpinner  = new ViewHolderRoomyTransferSpinner();

            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_text,parent,false);

            viewHolderRoomySpinner.tvRoomyName = convertView.findViewById(R.id.tv_spin_roomy_name);

            convertView.setTag(viewHolderRoomySpinner);

        }else
        {
           viewHolderRoomySpinner = (ViewHolderRoomyTransferSpinner) convertView.getTag();

        }
        viewHolderRoomySpinner.tvRoomyName.setText(listRoomName.get(position).getName());


        return  convertView;
    }
}

class  ViewHolderRoomyTransferSpinner
{
    TextView tvRoomyName;
}


