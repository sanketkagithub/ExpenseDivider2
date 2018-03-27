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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sanket on 12/23/2017.
 */

public class PaymentTakeGiveListAdapter extends ArrayAdapter {
    Context context;
    List<PayTg> payTgList;
    SharedPreferences sp;

    DialogEffect dialogEffect;
    String mobileLogged;
    DatabaseReference db_ref;

    String[] transferAmount = new String[1];

    Helper helper;

    String appColor;
    public PaymentTakeGiveListAdapter(Context context, List<PayTg> payTgList) {
        super(context, R.layout.row_take_give_payment, payTgList);
        this.context = context;
        this.payTgList = payTgList;

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
            convertView = li.inflate(R.layout.row_take_give_payment, parent, false);
            viewHolder.tv_roomy_name = convertView.findViewById(R.id.tv_roomy_name_gt);
            viewHolder.tv_roomy_amount = convertView.findViewById(R.id.tv_roomy_amount_gt);
            viewHolder.tv_roomy_amount_variation = convertView.findViewById(R.id.tv_roomy_amount_variation);
            viewHolder.iv_call_roomy = convertView.findViewById(R.id.iv_call_roomy);
            viewHolder.iv_roomy_variation = convertView.findViewById(R.id.iv_roomy_variation);
          //  viewHolder.but_transfer = convertView.findViewById(R.id.but_transfer);
            viewHolder.tv_payment_info = convertView.findViewById(R.id.tv_payment_info);
       //     viewHolder.ll_take_give = convertView.findViewById(R.id.ll_take_give);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderTg) convertView.getTag();
        }
        viewHolder.tv_roomy_name.setText(payTgList.get(position).getRoomyName());
        viewHolder.tv_roomy_amount.setText(payTgList.get(position).getAmountTg() + " ₹");

        double amountVariation = payTgList.get(position).getAmountVariation();

       // double amountVarRoundOff = helper.getRoundedOffValue(amountVariation);

        viewHolder.tv_roomy_amount_variation.setText(amountVariation + " ₹");



        String takeGive;
        if (amountVariation > 0) {

            int takeColor = context.getResources().getColor(R.color.take_light);
            int take = context.getResources().getColor(R.color.take);

            viewHolder.tv_roomy_amount_variation.setTextColor(take);
            viewHolder.tv_payment_info.setTextColor(take);
            viewHolder.iv_roomy_variation.setImageResource(R.drawable.take);

            takeGive = "get";


        }else if (amountVariation==0)
        {
            int doneColor = context.getResources().getColor(R.color.done_light);
            int done = context.getResources().getColor(R.color.done);

            viewHolder.tv_roomy_amount_variation.setTextColor(done);
            viewHolder.tv_payment_info.setTextColor(done);
            viewHolder.iv_roomy_variation.setImageResource(R.drawable.done);

            takeGive = "done";


        }

        else {
            int giveColor = context.getResources().getColor(R.color.give_light);
            int give = context.getResources().getColor(R.color.give);

            viewHolder.tv_roomy_amount_variation.setTextColor(give);
            viewHolder.tv_payment_info.setTextColor(give);
            viewHolder.iv_roomy_variation.setImageResource(R.drawable.give);

            takeGive = "give";
        }

        String message = payTgList.get(position).getRoomyName()
                + " will " + takeGive + " "+
                 amountVariation + " ₹";

        if (amountVariation!=0) {
            viewHolder.tv_payment_info.setText(message);
        }else
        {
            String messageDone = payTgList.get(position).getRoomyName() + " is done";

            viewHolder.tv_payment_info.setText(messageDone);

        }

        viewHolder.iv_call_roomy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+payTgList.get(position).getMobile()));
                context.startActivity(intent);


            }
        });


        return convertView;
    }






}


class ViewHolderTg {
    TextView tv_roomy_name, tv_roomy_amount, tv_roomy_amount_variation,
            tv_payment_info;

    ImageView iv_roomy_variation;
    LinearLayout ll_take_give;
    ImageView iv_call_roomy;
}


