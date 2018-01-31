package com.busyprojects.roomies.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.pojos.master.PayTg;

import java.util.List;

/**
 * Created by sanket on 12/23/2017.
 */

public class PaymentTakeGiveListAdapter extends ArrayAdapter {
    Context context;
    List<PayTg> payTgList;
    SharedPreferences sp;

    public PaymentTakeGiveListAdapter(Context context, List<PayTg> payTgList) {
        super(context, R.layout.row_payment, payTgList);
        this.context = context;
        this.payTgList = payTgList;


    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolderTg viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolderTg();

            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.row_take_give_payment, parent, false);
            viewHolder.tv_roomy_name = convertView.findViewById(R.id.tv_roomy_name_gt);
            viewHolder.tv_roomy_amount = convertView.findViewById(R.id.tv_roomy_amount_gt);
            viewHolder.tv_roomy_amount_variation = convertView.findViewById(R.id.tv_roomy_amount_variation);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderTg) convertView.getTag();
        }
        viewHolder.tv_roomy_name.setText(payTgList.get(position).getRoomyName());
        viewHolder.tv_roomy_amount.setText(payTgList.get(position).getAmountTg() + "/-");
        viewHolder.tv_roomy_amount_variation.setText(payTgList.get(position).getAmountVariation() + "/-");

        return convertView;
    }


}


class ViewHolderTg {
    TextView tv_roomy_name, tv_roomy_amount,tv_roomy_amount_variation;
}
