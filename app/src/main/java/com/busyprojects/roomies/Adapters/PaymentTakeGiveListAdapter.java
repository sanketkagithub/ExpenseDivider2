package com.busyprojects.roomies.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    String appColor;
    public PaymentTakeGiveListAdapter(Context context, List<PayTg> payTgList) {
        super(context, R.layout.row_payment, payTgList);
        this.context = context;
        this.payTgList = payTgList;

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
            viewHolder.iv_roomy_variation = convertView.findViewById(R.id.iv_roomy_variation);
            viewHolder.but_transfer = convertView.findViewById(R.id.but_transfer);
            viewHolder.tv_payment_info = convertView.findViewById(R.id.tv_payment_info);
       //     viewHolder.ll_take_give = convertView.findViewById(R.id.ll_take_give);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderTg) convertView.getTag();
        }
        viewHolder.tv_roomy_name.setText(payTgList.get(position).getRoomyName());
        viewHolder.tv_roomy_amount.setText(payTgList.get(position).getAmountTg() + "/-");

        long amountVariation = payTgList.get(position).getAmountVariation();
        viewHolder.tv_roomy_amount_variation.setText(amountVariation + "/-");

        viewHolder.but_transfer.setBackgroundColor(Color.parseColor(appColor));


        String takeGive;
        if (amountVariation > 0) {

            int takeColor = context.getResources().getColor(R.color.take_light);
            int take = context.getResources().getColor(R.color.take);

            viewHolder.tv_roomy_amount_variation.setTextColor(take);
            viewHolder.tv_payment_info.setTextColor(take);
            viewHolder.iv_roomy_variation.setImageResource(R.drawable.take);

            takeGive = "get";

           // viewHolder.ll_take_give.setBackgroundColor(takeColor);

        }else if (amountVariation==0)
        {
            int doneColor = context.getResources().getColor(R.color.done_light);
            int done = context.getResources().getColor(R.color.done);

            viewHolder.tv_roomy_amount_variation.setTextColor(done);
            viewHolder.tv_payment_info.setTextColor(done);
            viewHolder.iv_roomy_variation.setImageResource(R.drawable.done);

            takeGive = "done";

          //  viewHolder.ll_take_give.setBackgroundColor(doneColor);

        }

        else {
            int giveColor = context.getResources().getColor(R.color.give_light);
            int give = context.getResources().getColor(R.color.give);

            viewHolder.tv_roomy_amount_variation.setTextColor(give);
            viewHolder.tv_payment_info.setTextColor(give);
            viewHolder.iv_roomy_variation.setImageResource(R.drawable.give);

            takeGive = "give";
         //   viewHolder.ll_take_give.setBackgroundColor(giveColor);
        }

        String message = payTgList.get(position).getRoomyName()
                + " will " + takeGive +
                " " + payTgList.get(position).getAmountVariation() + "₹";

        message = message.replace("-", "");
        if (amountVariation!=0) {
            viewHolder.tv_payment_info.setText(message);
        }else
        {
            String messageDone = payTgList.get(position).getRoomyName() + " is done";

            viewHolder.tv_payment_info.setText(messageDone);

        }
        viewHolder.but_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTransferPayment(payTgList.get(position).getRoomyName()
                        , String.valueOf(position));

                posstitionFrom = position;

                fromAmount = payTgList.get(position).getAmountVariation();


            }
        });

        return convertView;
    }


    int posstitionFrom;
    long fromAmount, toAmount;

    Dialog dialog;

    public void showTransferPayment(String nameFrom, final String possitionFrom) {

        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View v = LayoutInflater.from(context).inflate(R.layout.transfer_layout, null);
        dialog.setContentView(v);

        dialog.show();

        TextView tvFrom = v.findViewById(R.id.tv_from);
        final EditText et_transfer_amount = v.findViewById(R.id.et_transfer_amount);

        tvFrom.setText(nameFrom);

        final Spinner spin_roomy = v.findViewById(R.id.spinner_roomy);


        final int[] possitonTo = new int[1];
        spin_roomy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                String toMobile = roomyListTransferSpinner.get(i).getMobile();

                for (int j = 0; j < payTgList.size(); j++) {

                    if (toMobile.equals(payTgList.get(j).getMobile())) {
                        toAmount = payTgList.get(j).getAmountVariation();

                        possitonTo[0] = j;
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button but_transferMoney = v.findViewById(R.id.but_transferMoney);


        getAllRoomieesTransferSpinner(spin_roomy);

        but_transferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transferAmount[0] = et_transfer_amount.getText().toString();


                setFromToAmount(Integer.parseInt(possitionFrom),
                        possitonTo[0], fromAmount, toAmount,
                        transferAmount[0]);

            }
        });


    }


    List<Roomy> roomyListTransferSpinner;

    void getAllRoomieesTransferSpinner(final Spinner spinnerRoomy) {

        dialogEffect.showDialog();
        db_ref.child(Helper.ROOMY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dialogEffect.cancelDialog();

                roomyListTransferSpinner = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 :
                        dataSnapshot.getChildren()) {

                    // TODO: 1/27/2018  add one by one roomy in list
                    Roomy roomy = dataSnapshot1.getValue(Roomy.class);

                    if (mobileLogged.equals(roomy.getMobileLogged())) {
                        roomyListTransferSpinner.add(roomy);
                    }

                }

                RoomySpinnerTransferAdapter roomySpinnerAdapterr = new RoomySpinnerTransferAdapter(context, roomyListTransferSpinner);
                spinnerRoomy.setAdapter(roomySpinnerAdapterr);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    void setFromToAmount(int possitionFrom, int possitionTo,
                         long amountFrom, long amountTo,
                         String amountTransfer) {


        long amountTransferL = Long.parseLong(amountTransfer);

        // TODO: 2/3/2018 save from  (add)

        long updateAmountVarFrom = amountFrom + amountTransferL;

        db_ref.child(Helper.EACH_TOTAL_PAMENT)
                .child(mobileLogged)
                .child(String.valueOf(possitionFrom))
                .child(Helper.AMOUNT_VARIATION)
                .setValue(updateAmountVarFrom);

        // TODO: 2/3/2018 save to  to  (sub)

        long updateAmountVarTo = amountTo - amountTransferL;

        db_ref.child(Helper.EACH_TOTAL_PAMENT)
                .child(mobileLogged)
                .child(String.valueOf(possitionFrom))
                .child(Helper.AMOUNT_VARIATION)
                .setValue(updateAmountVarTo);


        System.out.println("fromPos " + possitionFrom + " \n " +
                "possitionTo  " + possitionTo + "\n" +
                "amountFrom " + amountFrom + "\n" +
                "amountTo  " + amountTo + "\n" +
                "amountTransfer " + amountTransfer);


        dialog.dismiss();

    }


}


class ViewHolderTg {
    TextView tv_roomy_name, tv_roomy_amount, tv_roomy_amount_variation,
            tv_payment_info;

    ImageView iv_roomy_variation;
    Button but_transfer;
    LinearLayout ll_take_give;
}


