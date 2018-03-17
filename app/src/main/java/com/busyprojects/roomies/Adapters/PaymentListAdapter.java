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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sanket on 12/23/2017.
 */

public class PaymentListAdapter extends ArrayAdapter {
    Context context;
    List<Payment> paymentList;
    SharedPreferences sp;

    String emailNewUser;

    Animation animation = null;

    public PaymentListAdapter(Context context, List<Payment> paymentList) {
        super(context, R.layout.row_payment, paymentList);
        this.context = context;
        this.paymentList = paymentList;


    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.row_payment, parent, false);
            viewHolder.tv_roomy_name = convertView.findViewById(R.id.tv_roomy_name);
            viewHolder.tv_payment_date = convertView.findViewById(R.id.tv_payment_date);
            viewHolder.tv_roomy_amount = convertView.findViewById(R.id.tv_roomy_amount);
            //  viewHolder.but_call_roomy_p = convertView.findViewById(R.id.but_call_roomy_p);
            viewHolder.tv_paying_item = convertView.findViewById(R.id.tv_paying_item);
            viewHolder.ll_parent = convertView.findViewById(R.id.ll_payment_parent);
            viewHolder.tv_from = convertView.findViewById(R.id.tv_from);
            viewHolder.ll_item = convertView.findViewById(R.id.ll_item);
            viewHolder.ll_transfer = convertView.findViewById(R.id.ll_transfer);
            viewHolder.tv_to = convertView.findViewById(R.id.tv_to);
            viewHolder.iv_main_paying_item = convertView.findViewById(R.id.iv_main_paying_item);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_roomy_name.setText(paymentList.get(position).getRoomy().getName());
        viewHolder.tv_roomy_amount.setText(paymentList.get(position).getAmount() + " ₹");
        viewHolder.tv_payment_date.setText(paymentList.get(position).getPaymentDateTime());


        if (!paymentList.get(position).isTransferPayment()) {
            viewHolder.tv_paying_item.setText(paymentList.get(position).getPayingItem());
            viewHolder.ll_transfer.setVisibility(View.GONE);
            viewHolder.ll_item.setVisibility(View.VISIBLE);

            try {



                viewHolder.iv_main_paying_item.setImageResource(R.drawable.no_record);
                if (!paymentList.get(position).getPayinItemUrl().equals("")) {
                    Picasso.with(context).load(paymentList.
                            get(position).getPayinItemUrl())
                            .into(viewHolder.iv_main_paying_item, new Callback() {
                                @Override
                                public void onSuccess() {



                                }

                                @Override
                                public void onError() {

                                }
                            });
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        } else {
            viewHolder.ll_transfer.setVisibility(View.VISIBLE);
            viewHolder.ll_item.setVisibility(View.GONE);

            viewHolder.tv_paying_item.setText(paymentList.get(position).getToRoomy());

            viewHolder.tv_from.setText(paymentList.get(position).getRoomy().getName());
            viewHolder.tv_to.setText(paymentList.get(position).getToRoomy());

        }

//        viewHolder.but_call_roomy_p.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intentDial = new Intent(Intent.ACTION_DIAL);
//                intentDial.setData(Uri.parse("tel:" + paymentList.get(position).getRoomy().getMobile()));
//                context.startActivity(intentDial);
//
//
//            }
//        });

        return convertView;
    }


}

class ViewHolder {
    TextView tv_roomy_name, tv_roomy_amount, tv_paying_item,
            tv_payment_date, tv_from, tv_to;
    LinearLayout ll_parent, ll_item, ll_transfer;
    ImageView iv_main_paying_item;

    //  Button but_call_roomy_p;
}
