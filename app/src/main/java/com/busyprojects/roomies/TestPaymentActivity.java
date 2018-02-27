package com.busyprojects.roomies;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
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
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.History;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPaymentActivity extends Activity {

    List<Payment> paymentList;
    List<PayTg> payTgList;
    String mobileLogged;
    TextView tv_total_amount, tv_each_payment, tv_total_roomies;
    boolean isTransfer;
    ImageView iv_no_pay, iv_no_transfer;
    SharedPreferences.Editor spe;
    ListView lv_payments, lv_take_give;

    Button but_delete_payment, but_transfer_money;

    Context context = TestPaymentActivity.this;
    SharedPreferences sp;
    DialogEffect dialogEffect;
    DatabaseReference db_ref;

    long total;
    int totalRoommates;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_payment);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");

        totalRoommates = sp.getInt(SessionManager.TOTAL_ROOMMATES, 0);


        but_delete_payment = findViewById(R.id.but_delete_payment);
        but_transfer_money = findViewById(R.id.but_transfer_money);
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
        lv_payments = findViewById(R.id.lv_payments);
        lv_take_give = findViewById(R.id.lv_take_give);

        iv_no_pay.setVisibility(View.GONE);
        lv_payments.setVisibility(View.VISIBLE);
        lv_take_give.setVisibility(View.VISIBLE);

        setPaymentList();
        String appColor =  sp.getString(SessionManager.APP_COLOR,SessionManager.DEFAULT_APP_COLOR);
  int deletePayment =  sp.getInt(SessionManager.IV_DELETE,R.drawable.delete_payment);
 int transferPayment =  sp.getInt(SessionManager.IV_TRANSFER,R.drawable.transfer_payment);


        // TODO: 2/27/2018  appColor
        tv_transaction.setTextColor(Color.parseColor(appColor));
        tv_each_paid.setTextColor(Color.parseColor(appColor));
        ll_tot_each_roomy.setBackgroundColor(Color.parseColor(appColor));
        but_delete_payment.setBackground(getDrawable(deletePayment));
        but_transfer_money.setBackground(getDrawable(transferPayment));

    }


    String hid;
    History history;

    public void saveDeleteList(View view) {

        // boolean isTransfer = sp.getBoolean(SessionManager.IS_TRANSFER, false);


        // TODO: 2/4/2018 save n delete list


        // TODO: 2/5/2018 save Payments to History

        spe = sp.edit();
        spe.putBoolean(SessionManager.IS_TRANSFER, false);
        spe.apply();

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

                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        db_ref.child(Helper.IS_TRANSFER).child(mobileLogged).setValue(false);

        Toast.makeText(context, "Reset", Toast.LENGTH_SHORT).show();

        payTgList.clear();

    }



    void setPaymentList() {

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


//                        if (paymentList.size() > 0) {
//                            but_delete_payment.setVisibility(View.VISIBLE);
//                            iv_no_pay.setVisibility(View.GONE);
//                        } else {
//                            but_delete_payment.setVisibility(View.GONE);
//                            iv_no_pay.setVisibility(View.VISIBLE);
//
//                        }


                        paymentList = new Helper().getSortedTransactionList(paymentList);


                        if (paymentList.size() != 0) {
                            lv_payments.setVisibility(View.VISIBLE);
                            iv_no_pay.setVisibility(View.GONE);

                            PaymentListAdapter paymentListAdapter = new PaymentListAdapter(context, paymentList);
                            lv_payments.setAdapter(paymentListAdapter);

                            setEachAndTotalTexts();

                            // TODO: 2/17/2018 set payTgList
                            setPayTgList();

                        } else {
                            lv_payments.setVisibility(View.GONE);
                            iv_no_pay.setVisibility(View.VISIBLE);
                            iv_no_transfer.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    void setPayTgList() {

        checkTransferNsetPayTgList();
    }


    public void saveAndShowAfterTransferPayTgList(View view) {

        savePayTgListToAfterTransfer();

        showPayTgListfromAfterTransfer();
    }

    private void showPayTgListfromAfterTransfer() {

        payTgList = new ArrayList<>();
        db_ref.child(Helper.AFTER_TRANSFER)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        payTgList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 :
                                dataSnapshot.getChildren()) {

                            PayTg payTg = dataSnapshot1.getValue(PayTg.class);
                            if (payTg.getMobileLogged().equals(mobileLogged)) {
                                payTgList.add(payTg);
                            }


                        }

                        if (payTgList.size() != 0) {
                           payTgList = Helper.getSortedPaymentTakeGiveList(payTgList);

                            PaymentTakeGiveListAtAdapter paymentTakeGiveListAdapter = new PaymentTakeGiveListAtAdapter(context, payTgList);
                            lv_take_give.setAdapter(paymentTakeGiveListAdapter);
                        } else {
                            lv_take_give.setVisibility(View.GONE);
                            iv_no_transfer.setVisibility(View.VISIBLE);
                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    List<Roomy> roomyListVp;

    void checkTransferNsetPayTgList() {


        db_ref.child(Helper.IS_TRANSFER).child(mobileLogged).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            
                try {


                    isTransfer = dataSnapshot.getValue(Boolean.class);
                }catch (Exception e)
                {
                    e.printStackTrace();
                    
                    isTransfer = false;
                }
            
             //   isTransfer = sp.getBoolean(SessionManager.IS_TRANSFER, false);

                if (isTransfer) {
                    // TODO: 2/25/2018 show from afterTransfer 
                    showPayTgListfromAfterTransfer();

                    but_transfer_money.setVisibility(View.GONE);

                } else {
                    but_transfer_money.setVisibility(View.VISIBLE);
                    // TODO: 2/25/2018 show regulr list 
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


                            Log.i("====pp", paymentList.toString());

                            Map<String, List<Payment>> paymentMap = new HashMap<>();

                            Map<Roomy, Long> roomyTotalMap = new HashMap<>();

                            List<Payment> onesPAymentList = null;


                            System.out.println(roomyListVp.size() + "");


                            for (int i = 0; i < roomyListVp.size(); i++) {

                                onesPAymentList = new ArrayList<>();
                                long totalPaidByOne = 0;
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


                            long totalAmount=0;
                            try {
                                totalAmount = setEachAndTotalTexts();

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }


                            payTgList = new ArrayList<>();

                            for (Map.Entry<Roomy, Long> s : roomyTotalMap.entrySet()) {

                                System.out.println("esss" + s.getKey() + s.getValue());

                                // TODO: 2/17/2018 save payTgList
                                PayTg payTg = new PayTg();
                                payTg.setRoomyName(s.getKey().getName());
                                payTg.setAmountTg(s.getValue());
                                payTg.setMobile(s.getKey().getMobile());
                                payTg.setMobileLogged(mobileLogged);
                                payTg.setPayTgId(Helper.randomString(10));
                                payTg.setAmountVariation(s.getValue() - totalAmount);

                                payTgList.add(payTg);

                            }


                            if (paymentList.size() > 0) {
                                lv_take_give.setVisibility(View.VISIBLE);
                                // rel_divide_payment.setVisibility(View.VISIBLE);
                                iv_no_transfer.setVisibility(View.GONE);
                                // TODO: 2/16/2018 sort list

                                payTgList = Helper.getSortedPaymentTakeGiveList(payTgList);


                                // TODO: 2/3/2018  save PaymentTg(Each Total PAyment List) to db
                                PaymentTakeGiveListAdapter paymentTakeGiveListAdapter = new PaymentTakeGiveListAdapter(context, payTgList);
                                lv_take_give.setAdapter(paymentTakeGiveListAdapter);


                            } else {
                                iv_no_transfer.setVisibility(View.VISIBLE);
                                lv_take_give.setVisibility(View.GONE);
                                //rel_divide_payment.setVisibility(View.GONE);
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
        
    }


    long setEachAndTotalTexts() {
        //  but_divide.setVisibility(View.GONE);

//        ll_tot_each_roomy.setVisibility(View.VISIBLE);
//        tv_each_payment.setVisibility(View.VISIBLE);


        total = 0;
        for (int i = 0; i < paymentList.size(); i++) {


            if (!paymentList.get(i).isTransferPayment()) {
                total = total + paymentList.get(i).getAmount();
            }
        }

        long eachAmount = total / totalRoommates;

        tv_total_amount.setText(total + "₹");
        tv_total_roomies.setText(totalRoommates + "");
        tv_each_payment.setText(eachAmount + "₹");

//        if (paymentList.size() != 0) {
//            but_transfer_money.setVisibility(View.VISIBLE);
//        }

        return eachAmount;
    }


    void savePayTgListToAfterTransfer() {

        System.out.println(payTgList);
        // TODO: 2/17/2018 save to AfterTransfer
        for (int i = 0; i < payTgList.size(); i++) {


            db_ref.child(Helper.AFTER_TRANSFER)
                    .child(payTgList.get(i).getPayTgId())
                    .setValue(payTgList.get(i));


        }
        
        db_ref.child(Helper.IS_TRANSFER).child(mobileLogged).setValue(true);
        
        // setPaymentListNcallPaymentTakeGive(paymentList);


    }

    public void cancelPayment(View view) {

        onBackPressed();
    }

}
