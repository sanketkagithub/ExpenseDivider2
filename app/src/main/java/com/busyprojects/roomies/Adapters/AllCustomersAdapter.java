package com.busyprojects.roomies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.pojos.CustomerDetails;

/**
 * Created by sanket on 4/25/2017.
 */

public class AllCustomersAdapter extends ArrayAdapter<CustomerDetails>
{
    Context context;

    int res_lay;

    ArrayList<CustomerDetails> al_cus;


    public AllCustomersAdapter(Context context, int res_lay, ArrayList<CustomerDetails> al_cus)
    {
        super(context, res_lay, al_cus);
        this.context = context;
        this.res_lay = res_lay;
        this.al_cus = al_cus;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    LayoutInflater li =    (LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


      View v =  li.inflate(res_lay,parent,false);

    TextView tv_name =    (TextView)  v.findViewById(R.id.tvname);
  TextView tv_mobile =    (TextView)  v.findViewById(R.id.tvmobile);
  TextView tv_email =    (TextView)  v.findViewById(R.id.tvemail);
  TextView tv_address =    (TextView)  v.findViewById(R.id.tvaddress);


      CustomerDetails cus_det =  al_cus.get(position);

        tv_name.setText(cus_det.getName());
        tv_mobile.setText(cus_det.getMobile());
        tv_email.setText(cus_det.getEmail());
        tv_address.setText(cus_det.getAddress());

        return  v;
    }
}
