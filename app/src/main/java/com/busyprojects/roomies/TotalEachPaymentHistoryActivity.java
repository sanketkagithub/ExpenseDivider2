package com.busyprojects.roomies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.busyprojects.roomies.Adapters.PaymentListAdapter;
import com.busyprojects.roomies.Adapters.PaymentTakeGiveListAdapter;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TotalEachPaymentHistoryActivity extends Activity {

    ListView lv_payments, lv_take_give;
    SharedPreferences sp;
    String hid, mobileLogged;

    Context context;
    DialogEffect dialogEffect;
    DatabaseReference dbRef;

    int totalRoomates;


    TextView tv_total_amount, tv_total_roomies, tv_each_payment;

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_each_payment);
        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);

        dialogEffect = new DialogEffect(this);

        context = TotalEachPaymentHistoryActivity.this;
        dbRef = Helper.getFirebaseDatabseRef();

        totalRoomates = sp.getInt(SessionManager.TOTAL_ROOMMATES, 0);
        lv_payments = findViewById(R.id.lv_payments);
        lv_take_give = findViewById(R.id.lv_take_give);

        tv_total_amount = findViewById(R.id.tv_total_amount);
        TextView tv_transaction = findViewById(R.id.tv_transaction);
        TextView tv_each_paid = findViewById(R.id.tv_each_paid);
        LinearLayout ll_tot_each_roomy = findViewById(R.id.ll_tot_each_roomy);
        tv_total_roomies = findViewById(R.id.tv_total_roomies);
        tv_each_payment = findViewById(R.id.tv_each_payment);

        sp = getSharedPreferences(SessionManager.FILE_WTC, Context.MODE_PRIVATE);

        hid = sp.getString(SessionManager.HID, "");
        mobileLogged = sp.getString(SessionManager.MOBILE, "");

        setPaymentsList();
        setEachPaymentsList();

        String appColor =  sp.getString(SessionManager.APP_COLOR,SessionManager.DEFAULT_APP_COLOR);
        int deletePayment =  sp.getInt(SessionManager.IV_DELETE,R.drawable.delete_payment);
        int transferPayment =  sp.getInt(SessionManager.IV_TRANSFER,R.drawable.transfer_payment);
        // TODO: 2/27/2018  appColor
        tv_transaction.setTextColor(Color.parseColor(appColor));
        tv_each_paid.setTextColor(Color.parseColor(appColor));
        ll_tot_each_roomy.setBackgroundColor(Color.parseColor(appColor));

        //  lv_payments.se(R.style.list_style);
       // lv_payments.setsc

        if (Build.VERSION.SDK_INT>=21) {
            lv_payments.setScrollIndicators(R.style.list_style);
        }
    }


    List<Payment> paymentList;

    void setPaymentsList() {

        if (CheckInternetReceiver.isOnline(this)) {
            dialogEffect.showDialog();
            dbRef.child(Helper.HISTORY)
                    .child(hid)
                    .child("paymentList")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dialogEffect.cancelDialog();

                            paymentList = new ArrayList<>();

                            for (DataSnapshot dataSnapshot1 :
                                    dataSnapshot.getChildren()) {

                                Payment payment = dataSnapshot1.getValue(Payment.class);

                                if (mobileLogged.equals(payment.getMobileLogged())) {
                                    paymentList.add(payment);
                                }
                            }

                            PaymentListAdapter paymentListAdapter = new PaymentListAdapter(context, paymentList);
                            lv_payments.setAdapter(paymentListAdapter);

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


    List<PayTg> eachPaymentList;

    void setEachPaymentsList() {

        if (CheckInternetReceiver.isOnline(this)) {
            dialogEffect.showDialog();
            dbRef.child(Helper.HISTORY)
                    .child(hid)
                    .child("eachPaymentList")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dialogEffect.cancelDialog();

                            eachPaymentList = new ArrayList<>();

                            for (DataSnapshot dataSnapshot1 :
                                    dataSnapshot.getChildren()) {

                                PayTg payTg = dataSnapshot1.getValue(PayTg.class);
                                eachPaymentList.add(payTg);

                            }

                            PaymentTakeGiveListAdapter paymentListAdapter = new PaymentTakeGiveListAdapter(context, eachPaymentList);
                            lv_take_give.setAdapter(paymentListAdapter);

                            setEachPayMent();
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


    long setEachPayMent() {

        long total = 0;


        for (int i = 0; i < paymentList.size(); i++) {

            if (!paymentList.get(i).isTransferPayment()) {
                total = total + paymentList.get(i).getAmount();
            }
        }

        long eachAmount = total / totalRoomates;

        tv_total_amount.setText(total + "₹");
        tv_total_roomies.setText(eachPaymentList.size() + "");
        tv_each_payment.setText(eachAmount + "₹");

        return eachAmount;
    }

}
