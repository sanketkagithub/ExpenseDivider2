package com.busyprojects.roomies.roomyActivities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.busyprojects.roomies.Adapters.PaymentListAdapter;
import com.busyprojects.roomies.Adapters.PaymentTakeGiveListAdapter;
import com.busyprojects.roomies.Adapters.PaymentTakeGiveListAtAdapter;
import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.AnimationManager;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.helper.TinyDb;
import com.busyprojects.roomies.pojos.master.History;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PaymentActivity extends Activity {

    List<Payment> paymentList;
    // filtered paymentList

    List<Payment> paymentListFiltered;
    PaymentTakeGiveListAtAdapter paymentTakeGiveListAdapter;

    PaymentListAdapter paymentListAdapter;
    List<PayTg> payTgList;
    String mobileLogged;
    TextView tv_total_amount, tv_each_payment, tv_total_roomies;
    boolean isTransfer, isTransferRemote;
    ImageView iv_no_pay, iv_no_transfer;
    SharedPreferences.Editor spe;
    ListView lv_payments, lv_take_give;
    Helper helper;

    Button but_delete_payment, but_transfer_money;

    Context context = PaymentActivity.this;
    SharedPreferences sp;
    DialogEffect dialogEffect;
    DatabaseReference db_ref;

    //  EditText et_sv;
    double total;
    int totalRoommates;
    AnimationManager animationManager;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentListFiltered = new ArrayList<>();
        animationManager = AnimationManager.getInstance();
        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");
        isTransfer = sp.getBoolean(SessionManager.IS_TRANSFER, false);


        totalRoommates = sp.getInt(SessionManager.TOTAL_ROOMMATES, 0);

        helper = new Helper();

        // et_sv = findViewById(R.id.et_sv);
        but_delete_payment = findViewById(R.id.but_delete_payment);
        but_transfer_money = findViewById(R.id.but_transfer_money);


        //hideButtons();
        tv_total_amount = findViewById(R.id.tv_total_amount);
        TextView tv_transaction = findViewById(R.id.tv_transaction);
        TextView tv_each_paid = findViewById(R.id.tv_each_paid);
        LinearLayout ll_tot_each_roomy = findViewById(R.id.ll_tot_each_roomy);
        tv_each_payment = findViewById(R.id.tv_each_payment);
        tv_total_roomies = findViewById(R.id.tv_total_roomies);
        iv_no_pay = findViewById(R.id.iv_no_payment_record_found);
        iv_no_transfer = findViewById(R.id.iv_no_transfer_record_found);





        db_ref = Helper.getFirebaseDatabseRef();

        dialogEffect = new DialogEffect(this);
      //  dialogEffect.showDialog();

        getIsTransferRemote();


        lv_payments = findViewById(R.id.lv_payments);
        lv_take_give = findViewById(R.id.lv_take_give);

        iv_no_pay.setVisibility(View.GONE);
        lv_payments.setVisibility(View.VISIBLE);
        lv_take_give.setVisibility(View.VISIBLE);

        setPaymentList();
        String appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);
      //  int deletePayment = sp.getInt(SessionManager.IV_DELETE, R.drawable.delete_payment);
       // int transferPayment = sp.getInt(SessionManager.IV_TRANSFER, R.drawable.transfer_payment);


        // TODO: 2/27/2018  appColor
        tv_transaction.setTextColor(Color.parseColor(appColor));
        tv_each_paid.setTextColor(Color.parseColor(appColor));
        ll_tot_each_roomy.setBackgroundColor(Color.parseColor(appColor));

        try {

            //  if (Build.VERSION>=21)
            but_delete_payment.setBackgroundColor(Color.parseColor(appColor));
            but_transfer_money.setBackgroundColor(Color.parseColor(appColor));

        } catch (Exception e) {
            e.printStackTrace();
        }
//saveIsTransfer();

        setTotalRoomieesCountInSession();

    }






    // Filter Class
    public void filter_listview(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        paymentList.clear();
        if (charText.length() == 0) {
            paymentList.addAll(paymentListFiltered);
        } else {
            for (Payment pl : paymentListFiltered) {
                if (pl.getRoomy().getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    paymentList.add(pl);
                }
            }
        }
        paymentListAdapter.notifyDataSetChanged();
    }


    String hid;
    History history;
    Dialog dialogDeleteAlert;

    public void showSaveDeleteListAlert(View view) {
        animationManager.animateButton(view,context);

        if (CheckInternetReceiver.isOnline(this)) {
            //animationManager.animateViewForEmptyField();
            dialogDeleteAlert = new Dialog(this);
            dialogDeleteAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View v = LayoutInflater.from(context).inflate(R.layout.delete_alert, null);
            dialogDeleteAlert.setContentView(v);
            dialogDeleteAlert.setCancelable(false);
            dialogDeleteAlert.show();

        } else {
            Helper.showCheckInternet(context);

        }
    }


    boolean isAllTransactionsDeleted;

    void saveNdelete() {


        // TODO: 3/25/2018 reset isTransfer  
        // boolean isTransfer = sp.getBoolean(SessionManager.IS_TRANSFER, false);


        // TODO: 2/4/2018 save n delete list

        // TODO: 2/5/2018 save Payments to History

        // TODO: 3/25/2018 save isTransfer  in local
        spe = sp.edit();
        spe.putBoolean(SessionManager.IS_TRANSFER, false);
        spe.apply();
        Helper.setRemoteIstransfer(mobileLogged, false);


        hid = Helper.randomString(10);

        history = new History();


        dialogEffect.cancelDialog();

        history.setHid(hid);
        history.setMobileLogged(mobileLogged);
        history.setDateTime(Helper.getCurrentDateTime());


        if (payTgList.size() != 0) {
            history.setEachPaymentList(payTgList);

        }


        history.setPaymentList(paymentList);

        System.out.println(payTgList);

        // TODO: 2/18/2018 save to history
        db_ref.child(Helper.HISTORY)
                .child(hid)
                .setValue(history);


        // TODO: 2/5/2018 delete current Payment

        dialogEffect = new DialogEffect(this);
        dialogEffect.showDialog();
        db_ref.child(Helper.PAYMENT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {


                    for (DataSnapshot dataSnapshot1 :
                            dataSnapshot.getChildren()) {

                        Payment payment = dataSnapshot1.getValue(Payment.class);

                        if (payment.getMobileLogged().equals(mobileLogged)) {

                            db_ref.child(Helper.PAYMENT)
                                    .child(payment.getPid())
                                    .removeValue();

                        }

                        payTgList.clear();
                        lv_take_give.setVisibility(View.GONE);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        db_ref.child(Helper.AFTER_TRANSFER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                dialogEffect.cancelDialog();
                try {


                    for (DataSnapshot dataSnapshot1 :
                            dataSnapshot.getChildren()) {

                        PayTg payTg = dataSnapshot1.getValue(PayTg.class);

                        if (payTg.getMobileLogged().equals(mobileLogged)) {
                            db_ref.child(Helper.AFTER_TRANSFER)
                                    .child(payTg.getPayTgId())
                                    .removeValue();

                        }


                    }

                    payTgList.clear();
                    isAllTransactionsDeleted = true;

                } catch (Exception e) {
                    spe = sp.edit();
                    spe.putBoolean(SessionManager.IS_TRANSFER, false);
                    spe.apply();
                    Helper.setRemoteIstransfer(mobileLogged, false);

                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        db_ref.child(Helper.IS_TRANSFER).child(mobileLogged).setValue(false);

        Toast.makeText(context, "Data Cleared Successfully", Toast.LENGTH_SHORT).show();

        payTgList.clear();


    }


    void setPaymentList() {

        final DialogEffect dialogEffect = new DialogEffect(this);

        if (CheckInternetReceiver.isOnline(this)) {
            dialogEffect.showDialog();
            db_ref.child(Helper.PAYMENT)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dialogEffect.cancelDialog();

                            paymentList = new ArrayList<>();

                            // TODO: 2/17/2018 add paymentList
                            for (DataSnapshot dataSnapshot1 :
                                    dataSnapshot.getChildren()) {

                                Payment payment = dataSnapshot1.getValue(Payment.class);

                                if (mobileLogged.equals(payment.getMobileLogged())) {
                                    paymentList.add(payment);
                                }
                            }


                            setDeleteButtonVisibility(paymentList);
                            TinyDb tinyDb = new TinyDb(context);

                            ArrayList<String> roomyMobilesList = new ArrayList<>();

                            for (int i = 0; i < paymentList.size(); i++) {

                                roomyMobilesList.add(paymentList.get(i).getRoomy().getMobile());
                            }

                            tinyDb.putListString(SessionManager.ROOMY_MOBILE_LIST, roomyMobilesList);



                            paymentList = new Helper().getSortedTransactionList(paymentList);

                            //get the latest payment first
                            Collections.reverse(paymentList);

                            paymentListFiltered.addAll(paymentList);


                            if (paymentList.size() != 0) {
                                lv_payments.setVisibility(View.VISIBLE);
                                iv_no_pay.setVisibility(View.GONE);

                                paymentListAdapter = new PaymentListAdapter(context, paymentList);
                                lv_payments.setAdapter(paymentListAdapter);

                                //showButtons();


                                setEachAndTotalTexts();

                                // TODO: 2/17/2018 set payTgList
                                setPayTgList();

                            } else {

                                paymentList.clear();
                                paymentListAdapter = new PaymentListAdapter(context, paymentList);
                                paymentListAdapter.notifyDataSetChanged();
                                lv_payments.setAdapter(paymentListAdapter);


                                payTgList = new ArrayList<>();
                                paymentTakeGiveListAdapter = new PaymentTakeGiveListAtAdapter(context, payTgList);
                                paymentTakeGiveListAdapter.notifyDataSetChanged();
                                lv_take_give.setAdapter(paymentTakeGiveListAdapter);

                                lv_take_give.setVisibility(View.GONE);
                                iv_no_pay.setVisibility(View.VISIBLE);
                                iv_no_transfer.setVisibility(View.VISIBLE);

                             /*  new Handler().postDelayed(new Runnable() {
                                   @Override
                                   public void run() {

                                       lv_payments.setVisibility(View.GONE);
                                       iv_no_pay.setVisibility(View.VISIBLE);
                                       iv_no_transfer.setVisibility(View.VISIBLE);

                                   }
                               },1000);*/


                                //but_transfer_money.setVisibility(View.GONE);
                                //but_delete_payment.setVisibility(View.GONE);

                                //hideButtons();

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } else {
            Helper.showCheckInternet(context);

        }

    }

    void setPayTgList() {

        checkTransferNsetPayTgList();
    }


    public void saveAndShowAfterTransferPayTgList(View view) {
        animationManager.animateButton(view,context);

        savePayTgListToAfterTransfer();

        showPayTgListfromAfterTransfer();
    }


    void savePayTgListToAfterTransfer() {

        //but_transfer_money.setVisibility(View.GONE);

        System.out.println(payTgList);
        // TODO: 2/17/2018 save to AfterTransfer
        for (int i = 0; i < payTgList.size(); i++) {


            db_ref.child(Helper.AFTER_TRANSFER)
                    .child(payTgList.get(i).getPayTgId())
                    .setValue(payTgList.get(i));


        }

        db_ref.child(Helper.IS_TRANSFER).child(mobileLogged).setValue(true);

        // TODO: 3/25/2018 set isTransfer true 
        spe = sp.edit();
        spe.putBoolean(SessionManager.IS_TRANSFER, true);
        spe.apply();
        Helper.setRemoteIstransfer(mobileLogged, true);


    }

    private void showPayTgListfromAfterTransfer() {

       final DialogEffect dialogEffect = new DialogEffect(this);

        if (CheckInternetReceiver.isOnline(this)) {
            dialogEffect.showDialog();
            payTgList = new ArrayList<>();
            db_ref.child(Helper.AFTER_TRANSFER)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dialogEffect.cancelDialog();

                            payTgList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot1 :
                                    dataSnapshot.getChildren()) {

                                PayTg payTg = dataSnapshot1.getValue(PayTg.class);
                                if (payTg.getMobileLogged().equals(mobileLogged)) {
                                    payTgList.add(payTg);
                                }


                            }

                            if (payTgList.size() != 0) {


                               /* if (payTgList.size()!=totalRoommates)
                                {
                                    deletePaymentNpayTgAtIfTransfered();
                                }*/
                                payTgList = Helper.getSortedPaymentTakeGiveList(payTgList);


                                paymentTakeGiveListAdapter = new PaymentTakeGiveListAtAdapter(context, payTgList);
                                lv_take_give.setAdapter(paymentTakeGiveListAdapter);


                            } else {

                                payTgList.clear();
                                paymentTakeGiveListAdapter = new PaymentTakeGiveListAtAdapter(context, payTgList);
                                paymentTakeGiveListAdapter.notifyDataSetChanged();
                                lv_take_give.setAdapter(paymentTakeGiveListAdapter);

                                lv_take_give.setVisibility(View.GONE);
                                iv_no_transfer.setVisibility(View.VISIBLE);
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } else {
            Helper.showCheckInternet(context);


        }
    }


    List<Roomy> roomyListVp;

    void checkTransferNsetPayTgList() {

        if (CheckInternetReceiver.isOnline(this)) {

            if (isTransfer || isTransferRemote) {
                // TODO: 2/25/2018 show from afterTransfer
                showPayTgListfromAfterTransfer();

                but_transfer_money.setVisibility(View.GONE);

            } else {
                but_transfer_money.setVisibility(View.VISIBLE);
                // TODO: 2/25/2018 show regulr list
                final DialogEffect dialogEffect = new DialogEffect(this);

                dialogEffect.showDialog();
                db_ref.child(Helper.ROOMY).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        roomyListVp = new ArrayList<>();

                        dialogEffect.cancelDialog();
                        // roomyList.clear();

                        for (DataSnapshot dataSnapshot1 :
                                dataSnapshot.getChildren()) {

                            // TODO: 1/27/2018  add one by one roomy in list
                            Roomy roomy = dataSnapshot1.getValue(Roomy.class);

                            if (mobileLogged.equals(roomy.getMobileLogged())) {
                                roomyListVp.add(roomy);
                            }

                        }


                        //  Log.i("====pp", paymentList.toString());

                        Map<String, List<Payment>> paymentMap = new HashMap<>();
                        Map<Roomy, Double> roomyTotalMap = new HashMap<>();
                        List<Payment> onesPAymentList = null;
                        System.out.println(roomyListVp.size() + "");


                        for (int i = 0; i < roomyListVp.size(); i++) {
                            onesPAymentList = new ArrayList<>();
                            double totalPaidByOne = 0;


                            if (paymentList == null) {
                                paymentList = new ArrayList<>();
                            }
                            for (int j = 0; j < paymentList.size(); j++) {

                                if (roomyListVp.get(i).getMobile().equals(paymentList.get(j).getRoomy().getMobile())) {
                                    onesPAymentList.add(paymentList.get(j));

                                    totalPaidByOne = totalPaidByOne + paymentList.get(j).getAmount();
                                }

                                // TODO: 1/31/2018 each total

                            }

                            paymentMap.put(roomyListVp.get(i).getName(), onesPAymentList);

                            roomyTotalMap.put(roomyListVp.get(i), totalPaidByOne);

                        }

                        System.out.println(roomyTotalMap);
                        System.out.println(paymentMap.size());

                        // TODO: 2/3/2018 save paymentTg List (Each Payment total Map)


                        double totalAmount = 0;
                        try {
                            totalAmount = setEachAndTotalTexts();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        payTgList = new ArrayList<>();

                        for (Map.Entry<Roomy, Double> s : roomyTotalMap.entrySet()) {

                            System.out.println("esss" + s.getKey() + s.getValue());

                            // TODO: 2/17/2018 save payTgList
                            PayTg payTg = new PayTg();
                            payTg.setRoomyName(s.getKey().getName());


                            double amTgRf = helper.getRoundedOffValue(s.getValue());
                            payTg.setAmountTg(amTgRf);


                            payTg.setMobile(s.getKey().getMobile());
                            payTg.setMobileLogged(mobileLogged);
                            payTg.setPayTgId(Helper.randomString(10));

                            double amVarRf = helper.getRoundedOffValue(s.getValue() - totalAmount);
                            payTg.setAmountVariation(amVarRf);

                            payTgList.add(payTg);

                        }


                        try {


                            if (paymentList.size() > 0) {
                                lv_take_give.setVisibility(View.VISIBLE);
                                // rel_divide_payment.setVisibility(View.VISIBLE);
                                iv_no_transfer.setVisibility(View.GONE);
                                // TODO: 2/16/2018 sort list

                                payTgList = Helper.getSortedPaymentTakeGiveList(payTgList);


                                // TODO: 2/3/2018  save PaymentTg(Each Total PAyment List) to db
                                PaymentTakeGiveListAdapter paymentTakeGiveListAdapter = new PaymentTakeGiveListAdapter(context, payTgList);
                                lv_take_give.setAdapter(paymentTakeGiveListAdapter);


                                dialogEffect.cancelDialog();

                            } else {


                                lv_take_give.setVisibility(View.GONE);
                                iv_no_transfer.setVisibility(View.VISIBLE);
                                lv_take_give.setVisibility(View.GONE);
                                //rel_divide_payment.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            iv_no_transfer.setVisibility(View.VISIBLE);
                            lv_take_give.setVisibility(View.GONE);

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }


        } else {
            Helper.showCheckInternet(context);

        }
    }


    double setEachAndTotalTexts() {
        //  but_divide.setVisibility(View.GONE);

//        ll_tot_each_roomy.setVisibility(View.VISIBLE);
//        tv_each_payment.setVisibility(View.VISIBLE);


        try {


            total = 0;

            for (int i = 0; i < paymentList.size(); i++) {


                if (!paymentList.get(i).isTransferPayment()) {
                    total = total + paymentList.get(i).getAmount();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double eachAmount;
        try {
            eachAmount = total / totalRoommates;

        } catch (Exception e) {
            e.printStackTrace();
            eachAmount = 1;
        }


        tv_total_amount.setText(total + " ₹");
        tv_total_roomies.setText(totalRoommates + "");
        tv_each_payment.setText(helper.getRoundedOffValue(eachAmount) + " ₹");

//        if (paymentList.size() != 0) {
//            but_transfer_money.setVisibility(View.VISIBLE);
//        }

        return eachAmount;
    }


    public void cancelPayment(View view) {
        animationManager.animateButton(view,context);

        onBackPressed();
    }

    public void deletePayment(View view) {
        animationManager.animateButton(view,context);
        if (CheckInternetReceiver.isOnline(this)) {
            saveNdelete();
            dialogDeleteAlert.dismiss();
        } else {
            Helper.showCheckInternet(context);

        }
    }

    public void cancelDelete(View view) {
        animationManager.animateButton(view,context);
        dialogDeleteAlert.dismiss();
    }


/*
    void deletePaymentNpayTgAtIfTransfered() {


        // TODO: 2/5/2018 delete current Payment

        dialogEffect.showDialog();
        db_ref.child(Helper.PAYMENT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {


                    for (DataSnapshot dataSnapshot1 :
                            dataSnapshot.getChildren()) {

                        Payment payment = dataSnapshot1.getValue(Payment.class);

                        if (payment.getMobileLogged().equals(mobileLogged)) {

                            db_ref.child(Helper.PAYMENT)
                                    .child(payment.getPid())
                                    .removeValue();

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


        db_ref.child(Helper.AFTER_TRANSFER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dialogEffect.cancelDialog();
                try {


                    for (DataSnapshot dataSnapshot1 :
                            dataSnapshot.getChildren()) {

                        PayTg payTg = dataSnapshot1.getValue(PayTg.class);

                        if (payTg.getMobileLogged().equals(mobileLogged)) {
                            db_ref.child(Helper.AFTER_TRANSFER)
                                    .child(payTg.getPayTgId())
                                    .removeValue();

                        }


                    }

                } catch (Exception e) {
                    spe = sp.edit();
                    spe.putBoolean(SessionManager.IS_TRANSFER, false);
                    spe.apply();

                    Helper.setRemoteIstransfer(mobileLogged, false);

                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        db_ref.child(Helper.IS_TRANSFER).child(mobileLogged).setValue(false);

        Toast.makeText(context, "Data Cleared Successfully", Toast.LENGTH_SHORT).show();


    }
*/


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);

        isAllTransactionsDeleted = false;
        finish();

    }


    void getIsTransferRemote() {

       final DialogEffect dialogEffect = new DialogEffect(this);
        dialogEffect.showDialog();
        db_ref.child(SessionManager.IS_TRANSFER)
                .child(mobileLogged)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dialogEffect.cancelDialog();
                        isTransferRemote = dataSnapshot.getValue(Boolean.class);

                        checkTransferNsetPayTgList();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    void setDeleteButtonVisibility(List<Payment> list) {

        if (list.size() > 0) {
            but_delete_payment.setVisibility(View.VISIBLE);
            // but_transfer_money.setVisibility(View.VISIBLE);

        } else {
            but_delete_payment.setVisibility(View.GONE);
            but_transfer_money.setVisibility(View.GONE);

        }

    }


    private void setTotalRoomieesCountInSession() {

        final DialogEffect dialogEffect = new DialogEffect(this);
        dialogEffect.showDialog();

        if (CheckInternetReceiver.isOnline(this)) {
            db_ref.child(Helper.ROOMY).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    List roomysList = new ArrayList<>();
                    dialogEffect.cancelDialog();


                    for (DataSnapshot dataSnapshot1 :
                            dataSnapshot.getChildren()) {

                        // TODO: 1/27/2018  add one by one roomy in list
                        Roomy roomy = dataSnapshot1.getValue(Roomy.class);


                        if (mobileLogged.equals(roomy.getMobileLogged())) {
                            if (roomy != null) {
                                roomysList.add(roomy.getName());
                            }
                        }

                    }

                    totalRoommates = roomysList.size();

                    // TODO: 2/24/2018 save roomies  count

                    spe = sp.edit();
                    spe.putInt(SessionManager.TOTAL_ROOMMATES, roomysList.size());
                    spe.apply();


                    setEachAndTotalTexts();
                    //setPaymentList();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Helper.showCheckInternet(context);
        }

    }


}
