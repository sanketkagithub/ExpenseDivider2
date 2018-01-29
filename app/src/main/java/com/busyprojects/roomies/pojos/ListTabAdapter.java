package com.busyprojects.roomies.pojos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.busyprojects.roomies.R;

/**
 * Created by sanket on 1/13/2018.
 */

public class ListTabAdapter extends ArrayAdapter
{
    Context context;
    List<CountryCity> countryCityList;

    public ListTabAdapter(Context context, List<CountryCity> countryCityList) {
        super(context, R.layout.row_list, countryCityList);
        this.context = context;
        this.countryCityList = countryCityList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_list,parent,false);
       TextView tv = view.findViewById(R.id.tv_country);
        tv.setText(countryCityList.get(position).getCountry());

        return view;
    }
}
