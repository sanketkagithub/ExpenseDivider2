package com.busyprojects.roomies.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Toast;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.AnimationManager;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.busyprojects.roomies.pojos.transaction.Payment;
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

public class PaymentTakeGiveListAtAdapter extends ArrayAdapter {
    Context context;
    List<PayTg> payTgList;
    SharedPreferences sp;

    DialogEffect dialogEffect;
    String mobileLogged;
    DatabaseReference db_ref;

    String[] transferAmount = new String[1];
    AnimationManager animationManager;
    String appColor;

    public PaymentTakeGiveListAtAdapter(Context context, List<PayTg> payTgList) {
        super(context, R.layout.row_take_give_payment_at, payTgList);
        this.context = context;
        this.payTgList = payTgList;

        dialogEffect = new DialogEffect(context);

        animationManager = AnimationManager.getInstance();

        sp = context.getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");
        appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);

        db_ref = Helper.getFirebaseDatabseRef();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolderTgAt viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolderTgAt();

            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.row_take_give_payment_at, parent, false);
            viewHolder.tv_roomy_name = convertView.findViewById(R.id.tv_roomy_name_gt);
            viewHolder.tv_roomy_amount = convertView.findViewById(R.id.tv_roomy_amount_gt);
            viewHolder.iv_call_roomy = convertView.findViewById(R.id.iv_call_roomy);
            viewHolder.tv_roomy_amount_variation = convertView.findViewById(R.id.tv_roomy_amount_variation);
            viewHolder.iv_roomy_variation = convertView.findViewById(R.id.iv_roomy_variation);
            viewHolder.but_transfer = convertView.findViewById(R.id.but_transfer);
            viewHolder.tv_payment_info = convertView.findViewById(R.id.tv_payment_info);
            viewHolder.ll_take_give = convertView.findViewById(R.id.ll_take_give);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderTgAt) convertView.getTag();
        }
        viewHolder.tv_roomy_name.setText(payTgList.get(position).getRoomyName());
        viewHolder.tv_roomy_amount.setText(payTgList.get(position).getAmountTg() + " ₹");

        double amountVariation = payTgList.get(position).getAmountVariation();
        viewHolder.tv_roomy_amount_variation.setText(amountVariation + " ₹");

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
            viewHolder.but_transfer.setVisibility(View.GONE);

        } else if (amountVariation == 0.0) {
            int doneColor = context.getResources().getColor(R.color.done_light);
            int done = context.getResources().getColor(R.color.done);

            viewHolder.tv_roomy_amount_variation.setTextColor(done);
            viewHolder.tv_payment_info.setTextColor(done);
            viewHolder.iv_roomy_variation.setImageResource(R.drawable.done);
            viewHolder.but_transfer.setVisibility(View.GONE);

            takeGive = "done";

            // viewHolder.ll_take_give.setBackgroundColor(doneColor);

        } else {
            int giveColor = context.getResources().getColor(R.color.give_light);
            int give = context.getResources().getColor(R.color.give);

            viewHolder.tv_roomy_amount_variation.setTextColor(give);
            viewHolder.tv_payment_info.setTextColor(give);
            viewHolder.iv_roomy_variation.setImageResource(R.drawable.give);

            takeGive = "give";
            //  viewHolder.ll_take_give.setBackgroundColor(giveColor);
            viewHolder.but_transfer.setVisibility(View.VISIBLE);
        }

        String message = payTgList.get(position).getRoomyName()
                + " will " + takeGive +
                " " + payTgList.get(position).getAmountVariation() + " ₹";

        message = message.replace("-", "");
        if (amountVariation != 0) {
            viewHolder.tv_payment_info.setText(message);
        } else {
            String messageDone = payTgList.get(position).getRoomyName() + " is done";

            viewHolder.tv_payment_info.setText(messageDone);

        }
        viewHolder.but_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTransferPayment(payTgList.get(position));

            }
        });

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

    Roomy selectedFromRoomy;

    Dialog dialog;

    String fromMobile, toMobile;
    String amountToTransfer;

    public void showTransferPayment(final PayTg fromPayTg) {

        dialog = new Dialog(context);
        View v = LayoutInflater.from(context).inflate(R.layout.transfer_layout, null);
        dialog.setContentView(v);

        dialog.show();

        TextView tvFrom = v.findViewById(R.id.tv_from);
        final EditText et_transfer_amount = v.findViewById(R.id.et_transfer_amount);
        final TextView tv_from_bottom = v.findViewById(R.id.tv_from_bottom);
        final TextView tv_to_bottom = v.findViewById(R.id.tv_to_bottom);

        // TODO: 2/11/2018 from
        tvFrom.setText(fromPayTg.getRoomyName());

        tv_from_bottom.setText(fromPayTg.getRoomyName());

        fromMobile = fromPayTg.getMobile();

        final Spinner spin_roomy = v.findViewById(R.id.spinner_roomy);

        getAllRoomieesTransferSpinner(spin_roomy);


        spin_roomy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                toMobile = roomyListTransferSpinner.get(i).getMobile();
                selectedFromRoomy = roomyListTransferSpinner.get(i);

                tv_to_bottom.setText(roomyListTransferSpinner.get(i).getName());

                if (roomyListTransferSpinner.get(i).getMobile().equals(fromMobile)) {
                    spin_roomy.setSelection(0);
                    Toast.makeText(context, "Please  Select Different Roomy", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button but_transferMoney = v.findViewById(R.id.but_transferMoney);


        but_transferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: 2/25/2018 final transfer


                amountToTransfer = et_transfer_amount.getText().toString();

                if (amountToTransfer.equals("") || spin_roomy.getSelectedItemPosition() == 0) {

                    if (amountToTransfer.equals("")) {
                        animationManager.animateViewForEmptyField(et_transfer_amount, context);

                    }

                    if (spin_roomy.getSelectedItemPosition() == 0) {
                        animationManager.animateViewForEmptyField(spin_roomy, context);

                    }

                    Toast.makeText(context, "Please Check Empty field", Toast.LENGTH_SHORT).show();


                } else {

                    getFromToAmountVar(fromMobile, Long.parseLong(amountToTransfer), toMobile);
                    setTransferPayMent(fromPayTg.getRoomyName(), selectedFromRoomy.getName());
                }

            }
        });


    }


    void setTransferPayMent(String roomyFrom, String roomyTo) {
        String pid = Helper.randomString(10);

        Payment payment = new Payment();

        Roomy roomyFromR = new Roomy();
        roomyFromR.setMobileLogged("*");
        roomyFromR.setRegistrationDateTime("");
        roomyFromR.setName(roomyFrom);
        payment.setRoomy(roomyFromR);
        payment.setPayingItem("*");
        payment.setPid(pid);
        payment.setTransferPayment(true);
        payment.setAmount(Long.parseLong(amountToTransfer));
        payment.setMobileLogged(mobileLogged);
        payment.setPaymentDateTime(Helper.getCurrentDateTime());
        payment.setToRoomy(roomyTo);

        db_ref.child(Helper.PAYMENT)
                .child(pid)
                .setValue(payment);
    }


    List<Roomy> roomyListTransferSpinner;

    private void getAllRoomieesTransferSpinner(final Spinner spinnerRoomy) {

        if (CheckInternetReceiver.isOnline(context)) {
            dialogEffect.showDialog();
            db_ref.child(Helper.ROOMY).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dialogEffect.cancelDialog();

                    roomyListTransferSpinner = new ArrayList<>();

                    // TODO: 2/25/2018 add default select text in roomy spinner
                    Roomy roomyDefault = new Roomy();
                    roomyDefault.setRegistrationDateTime("");
                    roomyDefault.setMobileLogged("");
                    roomyDefault.setMobile("");
                    roomyDefault.setName(Helper.SELECT_ROOMY);
                    roomyDefault.setRid("");

                    roomyListTransferSpinner.add(roomyDefault);

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
        }else
        {
            Helper.showCheckInternet(context);
        }

    }

    private double amountVarFrom;
    private double amountVarTo;
    private double fromTotalPaid;
    private double toTotalPaid;

    private void getFromToAmountVar(final String fromMobile, long amountToTransfer, final String toMobile) {

//        // TODO: 2/3/2018 save from  (add)
//
//        long updateAmountVarFrom = amountFrom + amountTransferL;
//
//        db_ref.child(Helper.EACH_TOTAL_PAMENT)
//                .child(mobileLogged)
//                .child(String.valueOf(possitionFrom))
//                .child(Helper.AMOUNT_VARIATION)
//                .setValue(updateAmountVarFrom);
//
//        // TODO: 2/3/2018 save to  to  (sub)
//
//        long updateAmountVarTo = amountTo - amountTransferL;
//
//        db_ref.child(Helper.EACH_TOTAL_PAMENT)
//                .child(mobileLogged)
//                .child(String.valueOf(possitionFrom))
//                .child(Helper.AMOUNT_VARIATION)
//                .setValue(updateAmountVarTo);
//
//
        System.out.println("fromMobile " + fromMobile + " \n " +
                "toMobile " + toMobile + "\n" +
                "amountToTransfer " + amountToTransfer);

        if (CheckInternetReceiver.isOnline(context)) {
            // TODO: 2/11/2018 get fromAmountVar   &&  // TODO: 2/11/2018 get toAmountVar

            db_ref.child(Helper.AFTER_TRANSFER)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            try {


                                for (DataSnapshot dataSnapshot1 :
                                        dataSnapshot.getChildren()) {


                                    PayTg payTg = dataSnapshot1.getValue(PayTg.class);

                                    if (payTg.getMobileLogged().equals(mobileLogged)) {

                                        if (payTg.getMobile().equals(fromMobile)) {
                                            amountVarFrom = payTg.getAmountVariation();
                                            fromTotalPaid = payTg.getAmountTg();
                                        }

                                        if (payTg.getMobile().equals(toMobile)) {

                                            amountVarTo = payTg.getAmountVariation();
                                            toTotalPaid = payTg.getAmountTg();

                                        }

                                        setFromToAmountVar();


                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }else
        {
            Helper.showCheckInternet(context);

        }
    }


    void setFromToAmountVar() {

        if (!selectedFromRoomy.getMobile().equals("")) {

            final double updatedFromAmountVar;
            final double updatedToAmountVar;
            final double updatedTotalPaidFrom;
            final double updatedTotalPaidTo;

            updatedFromAmountVar = amountVarFrom + Long.parseLong(amountToTransfer);
            updatedToAmountVar = amountVarTo - Long.parseLong(amountToTransfer);

            updatedTotalPaidFrom = fromTotalPaid + Long.parseLong(amountToTransfer);
            updatedTotalPaidTo = toTotalPaid - Long.parseLong(amountToTransfer);


            for (int i = 0; i < payTgList.size(); i++) {

                if (payTgList.get(i).getMobileLogged().equals(mobileLogged)) {
                    // TODO: 2/19/2018 set from values

                    if (payTgList.get(i).getMobile().equals(fromMobile)) {
                        db_ref.child(Helper.AFTER_TRANSFER)
                                .child(payTgList.get(i).getPayTgId())
                                .child(Helper.TOTAL_PAID)
                                .setValue(updatedTotalPaidFrom);


                        db_ref.child(Helper.AFTER_TRANSFER)
                                .child(payTgList.get(i).getPayTgId())
                                .child(Helper.AMOUNT_VARIATION)
                                .setValue(updatedFromAmountVar);
                    }

                    // TODO: 2/19/2018 set to values
                    if (payTgList.get(i).getMobile().equals(toMobile)) {
                        db_ref.child(Helper.AFTER_TRANSFER)
                                .child(payTgList.get(i).getPayTgId())
                                .child(Helper.AMOUNT_VARIATION)
                                .setValue(updatedToAmountVar);

                        db_ref.child(Helper.AFTER_TRANSFER)
                                .child(payTgList.get(i).getPayTgId())
                                .child(Helper.TOTAL_PAID)
                                .setValue(updatedTotalPaidTo);

                    }
                }

            }


            dialog.dismiss();
            Toast.makeText(context, "Amount Transfered Successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Please Select One Roomy", Toast.LENGTH_SHORT).show();
        }
    }


}


class ViewHolderTgAt {
    TextView tv_roomy_name, tv_roomy_amount, tv_roomy_amount_variation,
            tv_payment_info;

    ImageView iv_roomy_variation;
    Button but_transfer;
    ImageView iv_call_roomy;

    LinearLayout ll_take_give;
}



