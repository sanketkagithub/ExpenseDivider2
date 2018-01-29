package com.busyprojects.roomies.Adapters;

import android.content.Context;
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
import com.busyprojects.roomies.pojos.transaction.Payment;

import java.util.List;

/**
 * Created by sanket on 12/23/2017.
 */

public class PaymentTakeGiveListAdapter extends ArrayAdapter {
    Context context;
    List<Payment> paymentList;
    SharedPreferences sp;

    String emailNewUser;

    Animation animation = null;

    public PaymentTakeGiveListAdapter(Context context, List<Payment> paymentList) {
        super(context, R.layout.row_payment, paymentList);
        this.context = context;
        this.paymentList = paymentList;


    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolderTg viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolderTg();

            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.row_payment, parent, false);
            viewHolder.tv_roomy_name = convertView.findViewById(R.id.tv_roomy_name);
            viewHolder.tv_roomy_amount = convertView.findViewById(R.id.tv_roomy_amount);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderTg) convertView.getTag();
        }
        viewHolder.tv_roomy_name.setText(paymentList.get(position).getRoomy().getName());
        viewHolder.tv_roomy_amount.setText(paymentList.get(position).getAmount() + "/-");

        return convertView;
    }


}


class ViewHolderTg {
    TextView tv_roomy_name, tv_roomy_amount;
           }
