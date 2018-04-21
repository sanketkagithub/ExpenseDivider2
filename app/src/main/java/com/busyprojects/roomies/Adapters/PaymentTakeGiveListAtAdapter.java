package com.busyprojects.roomies.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.RelativeLayout;
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
import com.busyprojects.roomies.roomyActivities.PaymentActivity;
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
    Helper helper;

    public PaymentTakeGiveListAtAdapter(Context context, List<PayTg> payTgList) {
        super(context, R.layout.row_take_give_payment_at, payTgList);
        this.context = context;
        this.payTgList = payTgList;

        helper = new Helper();
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
            viewHolder.but_call_roomy = convertView.findViewById(R.id.but_call_roomy);
            viewHolder.tv_roomy_amount_variation = convertView.findViewById(R.id.tv_roomy_amount_variation);
            viewHolder.iv_roomy_variation = convertView.findViewById(R.id.iv_roomy_variation);
            viewHolder.rel_but_transfer = convertView.findViewById(R.id.rel_but_transfer);
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

        //double amountVarRoundOff = helper.getRoundedOffValue(amountVariation);

        viewHolder.tv_roomy_amount_variation.setText(amountVariation + " ₹");

        viewHolder.but_transfer.setBackgroundColor(Color.parseColor(appColor));
        viewHolder.but_call_roomy.setBackgroundColor(Color.parseColor(appColor));


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
            viewHolder.rel_but_transfer.setVisibility(View.GONE);

        } else if (amountVariation == 0.0) {
            int doneColor = context.getResources().getColor(R.color.done_light);
            int done = context.getResources().getColor(R.color.done);

            viewHolder.tv_roomy_amount_variation.setTextColor(done);
            viewHolder.tv_payment_info.setTextColor(done);
            viewHolder.iv_roomy_variation.setImageResource(R.drawable.done);
            viewHolder.rel_but_transfer.setVisibility(View.GONE);
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
            viewHolder.rel_but_transfer.setVisibility(View.VISIBLE);
        }

        String message = payTgList.get(position).getRoomyName()
                + " will " + takeGive +
                " " + amountVariation + " ₹";

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

                animationManager.animateButton(view,context);
                showTransferPayment(payTgList.get(position));

            }
        });

        viewHolder.but_call_roomy.setOnClickListener(new View.OnClickListener() {
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

        String removed_AmVar = fromPayTg.getAmountVariation() + "";
               removed_AmVar = removed_AmVar.replace("-","");

        et_transfer_amount.setText(removed_AmVar +"");

        tv_from_bottom.setText(fromPayTg.getRoomyName());

        fromMobile = fromPayTg.getMobile();

        final Spinner spin_roomy = v.findViewById(R.id.spinner_roomy);

        getAllRoomieesTransferSpinner(spin_roomy);


        spin_roomy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                animationManager.animateButton(view,context);

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


                animationManager.animateButton(view,context);


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


                    try {
                        double amountToTransferDouble = Double.parseDouble(amountToTransfer);

                        amountToTransferDouble = helper.getRoundedOffValue(amountToTransferDouble);
                        //  long amountToTransferLong = Double.parseDouble(amountToTransfer);

                        setIsTransfer(true);

                        getFromToAmountVar(fromMobile, amountToTransferDouble, toMobile);
                        setTransferPayMent(fromPayTg.getRoomyName(), selectedFromRoomy.getName());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(context, "Please Enter valid Amount", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });




    }


    void setTransferPayMent(String roomyFrom, String roomyTo) {

        dialogEffect.showDialog();

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


     double amtRf =  helper.getRoundedOffValue(Double.parseDouble(amountToTransfer)) ;
        payment.setAmount(amtRf);

        payment.setMobileLogged(mobileLogged);
        payment.setPaymentDateTime(Helper.getCurrentDateTime());
        payment.setToRoomy(roomyTo);

        db_ref.child(Helper.PAYMENT)
                .child(pid)
                .setValue(payment);

        dialogEffect.cancelDialog();
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

    private void getFromToAmountVar(final String fromMobile, double amountToTransfer, final String toMobile) {

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

            dialogEffect.showDialog();
            db_ref.child(Helper.AFTER_TRANSFER)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dialogEffect.cancelDialog();
                            try {


                                for (DataSnapshot dataSnapshot1 :
                                        dataSnapshot.getChildren()) {


                                    PayTg payTg = dataSnapshot1.getValue(PayTg.class);

                                    if (payTg.getMobileLogged().equals(mobileLogged)) {

                                        if (payTg.getMobile().equals(fromMobile)) {
                                            amountVarFrom = helper.getRoundedOffValue(payTg.getAmountVariation());
                                            fromTotalPaid = helper.getRoundedOffValue(payTg.getAmountTg());
                                        }

                                        if (payTg.getMobile().equals(toMobile)) {

                                            amountVarTo = helper.getRoundedOffValue(payTg.getAmountVariation());
                                            toTotalPaid = helper.getRoundedOffValue(payTg.getAmountTg());

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

            double updatedFromAmountVar;
            double updatedToAmountVar;
            final double updatedTotalPaidFrom;
            final double updatedTotalPaidTo;

            updatedFromAmountVar = helper.getRoundedOffValue(amountVarFrom) + helper.getRoundedOffValue(Double.parseDouble(amountToTransfer));
            updatedToAmountVar = helper.getRoundedOffValue(amountVarTo) - helper.getRoundedOffValue(Double.parseDouble(amountToTransfer));

            if (updatedFromAmountVar<=0.99 && updatedFromAmountVar>=-0.99)
            {
                updatedFromAmountVar = 0.0;
            }

            if (updatedToAmountVar<=0.99 && updatedToAmountVar>=-0.99)
            {
                updatedToAmountVar = 0.0;
            }


            updatedTotalPaidFrom = helper.getRoundedOffValue(fromTotalPaid) + helper.getRoundedOffValue(Double.parseDouble(amountToTransfer));
            updatedTotalPaidTo = helper.getRoundedOffValue(toTotalPaid) - helper.getRoundedOffValue(Double.parseDouble(amountToTransfer));


            for (int i = 0; i < payTgList.size(); i++) {

                if (payTgList.get(i).getMobileLogged().equals(mobileLogged)) {
                    // TODO: 2/19/2018 set from values

                    if (payTgList.get(i).getMobile().equals(fromMobile)) {
                        db_ref.child(Helper.AFTER_TRANSFER)
                                .child(payTgList.get(i).getPayTgId())
                                .child(Helper.TOTAL_PAID)
                                .setValue(helper.getRoundedOffValue(updatedTotalPaidFrom));


                        db_ref.child(Helper.AFTER_TRANSFER)
                                .child(payTgList.get(i).getPayTgId())
                                .child(Helper.AMOUNT_VARIATION)
                                .setValue(helper.getRoundedOffValue(updatedFromAmountVar));
                    }

                    // TODO: 2/19/2018 set to values
                    if (payTgList.get(i).getMobile().equals(toMobile)) {
                        db_ref.child(Helper.AFTER_TRANSFER)
                                .child(payTgList.get(i).getPayTgId())
                                .child(Helper.AMOUNT_VARIATION)
                                .setValue(helper.getRoundedOffValue(updatedToAmountVar));

                        db_ref.child(Helper.AFTER_TRANSFER)
                                .child(payTgList.get(i).getPayTgId())
                                .child(Helper.TOTAL_PAID)
                                .setValue(helper.getRoundedOffValue(updatedTotalPaidTo));

                    }
                }

            }


            dialog.dismiss();

            Intent intent = new Intent(context,PaymentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                   context.startActivity(intent);

                   Activity a =  (Activity) context;
            a.finish();

            Toast.makeText(context, "Amount Transfered Successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Please Select One Roomy", Toast.LENGTH_SHORT).show();
        }
    }


    void setIsTransfer(boolean isTransfer)
    {
        // TODO: 2/25/2018 reset transfer session
       SharedPreferences.Editor spe = sp.edit();
        spe.putBoolean(SessionManager.IS_TRANSFER, isTransfer);
        spe.apply();

        Helper.setRemoteIstransfer(mobileLogged,isTransfer);


    }
}


class ViewHolderTgAt {
    TextView tv_roomy_name, tv_roomy_amount, tv_roomy_amount_variation,
            tv_payment_info;

    ImageView iv_roomy_variation;

    RelativeLayout rel_but_transfer;
    Button but_transfer;
    Button but_call_roomy;

    LinearLayout ll_take_give;
}



