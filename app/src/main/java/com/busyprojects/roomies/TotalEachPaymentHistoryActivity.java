package com.busyprojects.roomies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.busyprojects.roomies.Adapters.PaymentListAdapter;
import com.busyprojects.roomies.Adapters.PaymentTakeGiveListAdapter;
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

public class TotalEachPaymentHistoryActivity extends AppCompatActivity {

    ListView lv_payments, lv_take_give;
    SharedPreferences sp;
    String hid, mobileLogged;

    Context context;
    DialogEffect dialogEffect;
    DatabaseReference dbRef;

    TextView tv_total_amount,tv_total_roomies,tv_each_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_each_payment);

        dialogEffect = new DialogEffect(this);

        context = TotalEachPaymentHistoryActivity.this;
        dbRef = Helper.getFirebaseDatabseRef();

        lv_payments = findViewById(R.id.lv_payments);
        lv_take_give = findViewById(R.id.lv_take_give);

        tv_total_amount = findViewById(R.id.tv_total_amount);
        tv_total_roomies = findViewById(R.id.tv_total_roomies);
        tv_each_payment = findViewById(R.id.tv_each_payment);

        sp = getSharedPreferences(SessionManager.FILE_WTC, Context.MODE_PRIVATE);

        hid = sp.getString(SessionManager.HID, "");
        mobileLogged = sp.getString(SessionManager.MOBILE, "");

        setPaymentsList();
        setEachPaymentsList();

    }





    List<Payment> paymentList;

    void setPaymentsList() {

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


    }


    List<PayTg> eachPaymentList;

    void setEachPaymentsList() {

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


    }


    long setEachPayMent() {

     long   total = 0;


        for (int i = 0; i < paymentList.size(); i++) {

            if (!paymentList.get(i).isTransferPayment()) {
                total = total + paymentList.get(i).getAmount();
            }
        }

        long eachAmount = total / eachPaymentList.size();

        tv_total_amount.setText(total + "₹");
        tv_total_roomies.setText(eachPaymentList.size() + "");
        tv_each_payment.setText(eachAmount + "₹");

        return eachAmount;
    }

}
