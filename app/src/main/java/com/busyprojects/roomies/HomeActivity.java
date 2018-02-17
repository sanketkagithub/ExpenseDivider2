package com.busyprojects.roomies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.busyprojects.roomies.Adapters.PaymentListAdapter;
import com.busyprojects.roomies.Adapters.PaymentTakeGiveListAdapter;
import com.busyprojects.roomies.Adapters.PaymentTakeGiveListAtAdapter;
import com.busyprojects.roomies.Adapters.RoomySpinnerAdapterr;
import com.busyprojects.roomies.helper.AnimationManager;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.helper.ToastManager;
import com.busyprojects.roomies.pojos.master.History;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends Activity {

    Spinner spinner_roomy;
    FirebaseDatabase fb_db;
    DatabaseReference db_ref, cc_ref, key_ref_cc, cc_inner_ref;        // TODO : one database ref

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    Button but_transfer_money;
    EditText et_name, et_mobile, et_amount;
    AnimationManager animationManager = null;

    AutoCompleteTextView actv_paying_item;
    Button but_delete_payment;

    DialogEffect dialogEffect;
    Context context = HomeActivity.this;
    View inc_all_payment;
    RelativeLayout rel_add_roomy_layout, rel_iv_roomy_home, rel_name, rel_mobile, rel_save_roomy, rel_add_roomy, rel_pay_roomy,
            rel_paymen_lay, rel_back, rel_view_payment, rel_divide_payment, rel_view_history, rel_all_paymen_lay;

    ListView lv_payments, lv_take_give;// lv_take_give_at;

    Button but_divide;
    TextView tv_total_amount, tv_each_payment, tv_total_roomies;
    LinearLayout ll_tot_each_roomy;
    LinearLayout ll_paying_amount, ll_paying_item;
    ImageView iv_no_pay, iv_no_transfer;

    String mobileLogged;
    View view_add_roomy_layout;


    long total = 0;

    List<PayTg> payTgList;
    List<PayTg> payTgListAt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        animationManager = AnimationManager.getInstance();

        dialogEffect = new DialogEffect(context);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");

        rel_add_roomy_layout = findViewById(R.id.rel_add_roomy_layout);
        rel_divide_payment = findViewById(R.id.rel_divide_payment);
        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);
        but_transfer_money = findViewById(R.id.but_transfer_money);
        but_transfer_money.setVisibility(View.GONE);
        actv_paying_item = findViewById(R.id.actv_paying_item);

        iv_no_pay = findViewById(R.id.iv_no_payment_record_found);
        iv_no_transfer = findViewById(R.id.iv_no_transfer_record_found);

        ll_paying_amount = findViewById(R.id.ll_paying_amount);
        ll_paying_item = findViewById(R.id.ll_paying_item);


        but_divide = findViewById(R.id.but_divide);
        but_delete_payment = findViewById(R.id.but_delete_payment);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        tv_each_payment = findViewById(R.id.tv_each_payment);
        tv_total_roomies = findViewById(R.id.tv_total_roomies);
        ll_tot_each_roomy = findViewById(R.id.ll_tot_each_roomy);


        et_amount = findViewById(R.id.et_amount);

        rel_iv_roomy_home = findViewById(R.id.rel_iv_roomy_home);
        rel_name = findViewById(R.id.rel_name);
        rel_back = findViewById(R.id.rel_back);
        rel_mobile = findViewById(R.id.rel_mobile);
        rel_save_roomy = findViewById(R.id.rel_save_roomy);
        rel_add_roomy = findViewById(R.id.rel_add_roomy);
        rel_pay_roomy = findViewById(R.id.rel_pay_roomy);
        rel_paymen_lay = findViewById(R.id.rel_paymen_lay);

        rel_view_payment = findViewById(R.id.rel_view_payment);
        rel_view_history = findViewById(R.id.rel_view_history);
        rel_all_paymen_lay = findViewById(R.id.rel_all_paymen_lay);


        spinner_roomy = findViewById(R.id.spinner_roomy);
        lv_payments = findViewById(R.id.lv_payments);
        lv_take_give = findViewById(R.id.lv_take_give);
        //  lv_take_give_at = findViewById(R.id.lv_take_give_at);


        fb_db = FirebaseDatabase.getInstance();
        db_ref = fb_db.getReference();


        setInitialVisibilities();


//        lv_payments.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });

//        lv_take_give.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//        lv_take_give_at.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });


    }

    void setAutocompetionTextViewItems(AutoCompleteTextView actv_paying_item) {

        String sr[] = getResources().getStringArray(R.array.payingItems);

        List<String> payingItemsList = Arrays.asList(sr);


        ArrayAdapter aa = new ArrayAdapter(context, R.layout.row_actv_paying_item,
                payingItemsList);
        actv_paying_item.setAdapter(aa);


    }

    void setInitialVisibilities() {

        rel_add_roomy_layout.setVisibility(View.GONE);

//        rel_add_roomy.setVisibility(View.VISIBLE);
//        rel_pay_roomy.setVisibility(View.VISIBLE);
//
//        rel_name.setVisibility(View.GONE);
//        rel_mobile.setVisibility(View.GONE);
//        rel_save_roomy.setVisibility(View.GONE);
//        rel_back.setVisibility(View.GONE);
//
//        rel_paymen_lay.setVisibility(View.GONE);
//        rel_all_paymen_lay.setVisibility(View.GONE);
//        rel_view_payment.setVisibility(View.VISIBLE);
//
        ll_tot_each_roomy.setVisibility(View.GONE);

        rel_iv_roomy_home.setVisibility(View.VISIBLE);

        rel_add_roomy_layout.setVisibility(View.GONE);
        rel_all_paymen_lay.setVisibility(View.GONE);

        rel_add_roomy.setVisibility(View.VISIBLE);
        rel_pay_roomy.setVisibility(View.VISIBLE);
        rel_view_payment.setVisibility(View.VISIBLE);
        rel_view_history.setVisibility(View.VISIBLE);

        rel_paymen_lay.setVisibility(View.GONE);


    }


    public void saveRoomy(View view) {

        dialogEffect.showDialog();
        String rid = Helper.randomString(10);
        String nameS = et_name.getText().toString();
        String mobileS = et_mobile.getText().toString();
        String registrationDateTime = Helper.getCurrentDateTime();


        if (nameS.equals("") || mobileS.equals("")) {

            ToastManager.showToast(context, Helper.EMPTY_FIELD);
        } else {
            dialogEffect.cancelDialog();

            // TODO: 1/27/2018 save unique roomy here
            Roomy roomy = new Roomy();
            roomy.setMobile(mobileS);
            roomy.setName(nameS);
            roomy.setRid(rid);
            roomy.setMobileLogged(mobileLogged);
            roomy.setRegistrationDateTime(registrationDateTime);

            db_ref.child(Helper.ROOMY).child(rid)
                    .setValue(roomy);

            ToastManager.showToast(context, Helper.REGISTERD);

            et_mobile.setText("");
            et_name.setText("");


            setInitialVisibilities();

        }

    }


    public void addRoomy(View view) {
//        rel_name.setVisibility(View.VISIBLE);
//        rel_mobile.setVisibility(View.VISIBLE);
//        rel_save_roomy.setVisibility(View.VISIBLE);
//        rel_back.setVisibility(View.VISIBLE);
//
        rel_iv_roomy_home.setVisibility(View.GONE);

        rel_add_roomy_layout.setVisibility(View.VISIBLE);

        rel_add_roomy.setVisibility(View.GONE);
        rel_pay_roomy.setVisibility(View.GONE);
        rel_view_payment.setVisibility(View.GONE);

        rel_view_history.setVisibility(View.GONE);
    }


    public void payNow(View view) {

//        rel_pay_roomy.setVisibility(View.GONE);
//        rel_iv_roomy_home.setVisibility(View.GONE);
//
//        rel_name.setVisibility(View.GONE);
//        rel_mobile.setVisibility(View.GONE);
//        rel_save_roomy.setVisibility(View.GONE);
//        rel_add_roomy.setVisibility(View.GONE);
//
//
//        rel_paymen_lay.setVisibility(View.VISIBLE);
//        rel_view_payment.setVisibility(View.GONE);

        rel_iv_roomy_home.setVisibility(View.GONE);

        rel_add_roomy_layout.setVisibility(View.GONE);

        rel_add_roomy.setVisibility(View.GONE);
        rel_pay_roomy.setVisibility(View.GONE);
        rel_view_payment.setVisibility(View.GONE);
        rel_view_history.setVisibility(View.GONE);


        rel_paymen_lay.setVisibility(View.VISIBLE);
        setSpinneerAdapter();
        setAutocompetionTextViewItems(actv_paying_item);

    }


    List<String> roomyListNames = new ArrayList<>();
    List<Roomy> roomyList = new ArrayList<>();

    void setSpinneerAdapter() {

        db_ref.child(Helper.ROOMY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                roomyListNames.clear();
                roomyList.clear();

                // TODO: 2/10/2018 add default roomy
                roomyListNames.add(Helper.SELECT_ROOMY);

                Roomy roomyDefault = new Roomy();
                roomyDefault.setName(Helper.SELECT_ROOMY);
                roomyDefault.setRid("");
                roomyDefault.setRegistrationDateTime("");
                roomyDefault.setMobile("");
                roomyDefault.setMobileLogged("");

                roomyList.add(roomyDefault);


                for (DataSnapshot dataSnapshot1 :
                        dataSnapshot.getChildren()) {

                    // TODO: 1/27/2018  add one by one roomy in list
                    Roomy roomy = dataSnapshot1.getValue(Roomy.class);

                    String mobile = roomy.getMobileLogged();

                    System.out.println(mobileLogged + " == " + mobile);

                    if (mobileLogged.equals(roomy.getMobileLogged())) {
                        roomyListNames.add(roomy.getName());
                        roomyList.add(roomy);

                    }

                }

                // TODO: 1/27/2018 set roomy list in spinner
                RoomySpinnerAdapterr roomySpinnerAdapterr = new RoomySpinnerAdapterr(context, roomyListNames);
                spinner_roomy.setAdapter(roomySpinnerAdapterr);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getSelectedRoomy();
    }

    Roomy roomySelected = null;

    void getSelectedRoomy() {
        spinner_roomy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                roomySelected = roomyList.get(i);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    boolean canTransfer = false;

    public void payNowhere(View view) {
        final String amount = et_amount.getText().toString();
        String payingItem = actv_paying_item.getText().toString();


        if (amount.equals("") || roomySelected.getName().equals(Helper.SELECT_ROOMY) ||
                payingItem.equals("")) {
            if (roomySelected.getName().equals(Helper.SELECT_ROOMY)) {

                animationManager.animateViewForEmptyField(spinner_roomy, context);
            }

            if (amount.equals("")) {
                animationManager.animateViewForEmptyField(ll_paying_amount, context);

            }

            if (payingItem.equals("")) {
                animationManager.animateViewForEmptyField(ll_paying_item, context);

            }

            ToastManager.showToast(context, Helper.EMPTY_FIELD);


        } else {

            String pid = Helper.randomString(10);
            // TODO: 1/27/2018 save payment;

            String currentDateTime = Helper.getCurrentDateTime();

            Payment payment = new Payment();
            payment.setAmount(Long.parseLong(amount));
            payment.setPaymentDateTime(currentDateTime);
            payment.setPid(pid);
            payment.setMobileLogged(mobileLogged);
            payment.setRoomy(roomySelected);

            payment.setPayingItem(payingItem);


            db_ref.child(Helper.PAYMENT)
                    .child(pid)
                    .setValue(payment);

            Toast.makeText(context, "Payment done successfully", Toast.LENGTH_SHORT).show();

            getTotalAmountPaid();


            dialogEffect.showDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  //  dialogEffect.cancelDialog();
                    // TODO: 2/11/2018 save after some delay================
                    // totalAmountPaid = totalAmountPaid + Long.parseLong(amount);



                    getPayTgListAt:       getPayTgListAt();  // will give payTgListAt list also


                    dialogEffect.showDialog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialogEffect.cancelDialog();

                            long totalAfterTransfer = 0;


                            for (int i = 0; i < payTgListAt.size(); i++) {

                                try {

                                    if (payTgListAt.get(i).getMobile().equals(roomySelected.getMobile())) {
                                        // TODO: 2/12/2018 add total amount paid  to atList
                                        totalAmountPaid = totalAmountPaid + Long.parseLong(amount);


                                        db_ref.child(Helper.AFTER_TRANSFER)
                                                .child(payTgListAt.get(i).getMobile())
                                                .child(Helper.TOTAL_PAID).setValue(totalAmountPaid);

                                        payTgListAt.get(i).setAmountTg(totalAmountPaid);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }


                            for (int i = 0; i < payTgListAt.size(); i++) {

                                // TODO: 2/12/2018 find total.......total/roomysize......each
                                // TODO: 2/12/2018    totPaid -  each =   amountVar


                                totalAfterTransfer = totalAfterTransfer + payTgListAt.get(i).getAmountTg();


                            }

                            long div = roomyList.size() - 1;


                            long eachHasToPayAfterTransfer = totalAfterTransfer / div;

                            for (int i = 0; i < payTgListAt.size(); i++) {

                                db_ref.child(Helper.AFTER_TRANSFER)
                                        .child(payTgListAt.get(i).getMobile())
                                        .child(Helper.AMOUNT_VARIATION)
                                        .setValue(payTgListAt.get(i).getAmountTg() - eachHasToPayAfterTransfer);
                            }


                            canTransfer = true;

                            dialogEffect.cancelDialog();

                            //showAtTgList();



                            // amountVariation =
                        }
                    }, 4000);


                }
            }, 4000);


            setInitialVisibilities();
        }

    }

    long totalAmountPaid, amountVariation;

    boolean haveTotalAmountPaid;

    void getTotalAmountPaid() {
        db_ref.child(Helper.AFTER_TRANSFER)
                .child(roomySelected.getMobile())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try {

                            PayTg payTg = dataSnapshot.getValue(PayTg.class);
                            totalAmountPaid = payTg.getAmountTg();
                            haveTotalAmountPaid=true;

                            amountVariation = payTg.getAmountVariation();

                        } catch (Exception e) {
                            e.printStackTrace();

                            totalAmountPaid = 0;
                            amountVariation = 0;
                        }


                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    public void cancelPayment(View view) {
        rel_paymen_lay.setVisibility(View.GONE);

        setInitialVisibilities();

        rel_add_roomy.setVisibility(View.VISIBLE);
        rel_pay_roomy.setVisibility(View.VISIBLE);
    }


    public void viewPayment(View view) {
        // TODO: 1/27/2018 get All Sessions list  firstly


        rel_iv_roomy_home.setVisibility(View.GONE);

        rel_add_roomy_layout.setVisibility(View.GONE);

        rel_add_roomy.setVisibility(View.GONE);
        rel_pay_roomy.setVisibility(View.GONE);
        rel_view_payment.setVisibility(View.GONE);
        rel_view_history.setVisibility(View.GONE);
        rel_all_paymen_lay.setVisibility(View.VISIBLE);


        setPaymentsList();

        but_divide.setVisibility(View.VISIBLE);
        tv_each_payment.setVisibility(View.GONE);

    }

    List<Payment> paymentList = null;

    void setPaymentsList() {
        paymentList = new ArrayList<>();

        dialogEffect.showDialog();
        db_ref.child(Helper.PAYMENT)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dialogEffect.cancelDialog();
                        if (paymentList != null) {
                            paymentList.clear();
                        }

                        for (DataSnapshot dataSnapshot1 :
                                dataSnapshot.getChildren()) {

                            Payment payment = dataSnapshot1.getValue(Payment.class);

                            if (mobileLogged.equals(payment.getMobileLogged())) {
                                paymentList.add(payment);
                            }
                        }


                        if (paymentList.size() > 0) {
                            but_delete_payment.setVisibility(View.VISIBLE);
                            iv_no_pay.setVisibility(View.GONE);
                        } else {
                            but_delete_payment.setVisibility(View.GONE);
                            iv_no_pay.setVisibility(View.VISIBLE);

                        }
                        paymentList = new Helper().getSortedTransactionList(paymentList);

                        PaymentListAdapter paymentListAdapter = new PaymentListAdapter(context, paymentList);
                        lv_payments.setAdapter(paymentListAdapter);

                        setPaymentsTakeGiveList(paymentList);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    List<Roomy> roomyListVp;


    void setPaymentListNcallPaymentTakeGive(final List<Payment> paymentList) {

        boolean isTransfer = sp.getBoolean(SessionManager.IS_TRANSFER, false);


        if (isTransfer) {
            setPaymentTgAfterTransfer(lv_take_give);

        } else {
            dialogEffect.showDialog();

            if (paymentList.size() > 0) {
            } else {

            }

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


                    long totalAmount = setEachPayMentTg(roomyListVp);
                    payTgList = new ArrayList<>();

                    for (Map.Entry<Roomy, Long> s : roomyTotalMap.entrySet()) {

                        System.out.println("esss" + s.getKey() + s.getValue());

                        PayTg payTg = new PayTg();
                        payTg.setRoomyName(s.getKey().getName());
                        payTg.setAmountTg(s.getValue());
                        payTg.setMobile(s.getKey().getMobile());
                        payTg.setMobileLogged(mobileLogged);

                        payTg.setAmountVariation(s.getValue() - totalAmount);

                        payTgList.add(payTg);

                    }


                    if (paymentList.size() > 0) {
                        lv_take_give.setVisibility(View.VISIBLE);
                        rel_divide_payment.setVisibility(View.VISIBLE);
                        iv_no_transfer.setVisibility(View.GONE);
                        // TODO: 2/16/2018 sort list

                        payTgList = Helper.getSortedPaymentTakeGiveList(payTgList);


                        // TODO: 2/3/2018  save PaymentTg(Each Total PAyment List) to db
                        PaymentTakeGiveListAdapter paymentTakeGiveListAdapter = new PaymentTakeGiveListAdapter(context, payTgList);
                        lv_take_give.setAdapter(paymentTakeGiveListAdapter);


                    } else {
                        iv_no_transfer.setVisibility(View.VISIBLE);
                        lv_take_give.setVisibility(View.GONE);
                        rel_divide_payment.setVisibility(View.GONE);
                    }


//                for (int i = 0; i < payTgList.size(); i++) {
//
//                    db_ref.child(Helper.EACH_TOTAL_PAMENT)
//                            .child(mobileLogged)
//                            .child(Helper.randomString(10))
//                            .setValue(payTgList.get(i));
//
//                }


                    // setAfterTransferTgList(lv_take_give_at);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    void setPaymentTgAfterTransfer(final ListView lv_take_give_at) {


        db_ref.child(Helper.AFTER_TRANSFER)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        List<PayTg> payTgList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 :
                                dataSnapshot.getChildren()) {

                            PayTg payTg = dataSnapshot1.getValue(PayTg.class);

                            if (payTg.getMobileLogged().equals(mobileLogged))
                            payTgList.add(payTg);


                        }


                        if (paymentList.size() > 0) {
                            iv_no_transfer.setVisibility(View.GONE);

                            lv_take_give.setVisibility(View.VISIBLE);
                            // TODO: 2/16/2018 sort list

                            payTgList = Helper.getSortedPaymentTakeGiveList(payTgList);


                            rel_divide_payment.setVisibility(View.VISIBLE);
                            // TODO: 2/3/2018  save PaymentTg(Each Total PAyment List) to db
                            PaymentTakeGiveListAtAdapter paymentTakeGiveListAdapter = new PaymentTakeGiveListAtAdapter(context, payTgList);
                            lv_take_give.setAdapter(paymentTakeGiveListAdapter);
                        } else {
                            iv_no_transfer.setVisibility(View.VISIBLE);
                            lv_take_give.setVisibility(View.GONE);

                            rel_divide_payment.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    void setPaymentsTakeGiveList(final List<Payment> paymentList) {

        setPaymentListNcallPaymentTakeGive(paymentList);
        // PaymentTakeGiveListAdapter paymentListAdapterTg = new PaymentTakeGiveListAdapter(context, paymentListTg);
        // lv_take_give.setAdapter(paymentListAdapterTg);


    }


    void getAllRoomiees() {

        dialogEffect.showDialog();
        db_ref.child(Helper.ROOMY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dialogEffect.cancelDialog();
                roomyList.clear();

                for (DataSnapshot dataSnapshot1 :
                        dataSnapshot.getChildren()) {

                    // TODO: 1/27/2018  add one by one roomy in list
                    Roomy roomy = dataSnapshot1.getValue(Roomy.class);

                    if (mobileLogged.equals(roomy.getMobileLogged())) {
                        roomyList.add(roomy);
                    }

                }


                setEachPayMent(roomyList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void divideAmount(View view) {

        but_divide.setVisibility(View.GONE);
        getAllRoomiees();


    }

    long setEachPayMentTg(List<Roomy> roomyList) {
        //  but_divide.setVisibility(View.GONE);

//        ll_tot_each_roomy.setVisibility(View.VISIBLE);
//        tv_each_payment.setVisibility(View.VISIBLE);


        total = 0;
        for (int i = 0; i < paymentList.size(); i++) {


            total = total + paymentList.get(i).getAmount();

        }

        long eachAmount = total / roomyList.size();

        tv_total_amount.setText(total + "₹");
        tv_total_roomies.setText(roomyList.size() + "");
        tv_each_payment.setText(eachAmount + "₹");

      if (paymentList.size() != 0)
      {
          but_transfer_money.setVisibility(View.VISIBLE);
      }
        return eachAmount;
    }

    long setEachPayMent(List<Roomy> roomyList) {

        total = 0;


        ll_tot_each_roomy.setVisibility(View.VISIBLE);
        tv_each_payment.setVisibility(View.VISIBLE);

        for (int i = 0; i < paymentList.size(); i++) {
            if (!paymentList.get(i).isTransferPayment()) {
                total = total + paymentList.get(i).getAmount();
            }
        }

        long eachAmount = total / roomyList.size();

        tv_total_amount.setText(total + "₹");
        tv_total_roomies.setText(roomyList.size() + "");
        tv_each_payment.setText(eachAmount + "₹");

        return eachAmount;
    }



    void getPayTgListAt() {

        dialogEffect.showDialog();
        db_ref.child(Helper.AFTER_TRANSFER)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                     //   dialogEffect.cancelDialog();

                        payTgListAt = new ArrayList<>();


                        try {


                            for (DataSnapshot dataSnapshot1 :
                                    dataSnapshot.getChildren()) {

                                PayTg payTg = dataSnapshot1.getValue(PayTg.class);

                                payTgListAt.add(payTg);
                            }

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    public void backToHome(View view) {
        setInitialVisibilities();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        setInitialVisibilities();
    }


//    void setAfterTransferTgList(final ListView lv_take_give_at) {
//
//        db_ref.child(Helper.EACH_TOTAL_PAMENT)
//                .child(mobileLogged)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        List<PayTg> payTgList = new ArrayList<>();
//
//                        try {
//
//                            for (DataSnapshot dataSnapshot1 :
//                                    dataSnapshot.getChildren()) {
//
//                                PayTg payTg = dataSnapshot1.getValue(PayTg.class);
//
//                                payTgList.add(payTg);
//                            }
//
//                            PaymentTakeGiveListAdapter paymentTakeGiveListAdapter = new PaymentTakeGiveListAdapter(context, payTgList);
//                            lv_take_give_at.setAdapter(paymentTakeGiveListAdapter);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//    }

    History history;
    String hid;

    public void saveDeleteList(View view) {
        // TODO: 2/4/2018 save n delete list


        // TODO: 2/5/2018 save Payments to History

        dialogEffect.showDialog();

        spe = sp.edit();
        spe.putBoolean(SessionManager.IS_TRANSFER, false);
        spe.apply();

        hid = Helper.randomString(10);

        history = new History();

        getPayTgListAt();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogEffect.cancelDialog();

                history.setHid(hid);
                history.setMobileLogged(mobileLogged);
                history.setDateTime(Helper.getCurrentDateTime());


                if (payTgListAt.size()==0)
                {
                    history.setEachPaymentList(payTgList);

                }else
                {
                    history.setEachPaymentList(payTgListAt);

                }


                history.setPaymentList(paymentList);

                System.out.println(payTgList + "\n" + payTgListAt);

                db_ref.child(Helper.HISTORY)
                        .child(hid)
                        .setValue(history);


                // TODO: 2/5/2018 delete current Payment

                db_ref.child(Helper.PAYMENT)
                        .removeValue();
                db_ref.child(Helper.AFTER_TRANSFER)
                        .removeValue();

                Toast.makeText(context, "Reset", Toast.LENGTH_SHORT).show();


            }
        }, 4000);

    }


    public void viewHistory(View view) {

        startActivity(new Intent(context, HistoryDateActivity.class));
    }


    public void viewTransfer(View view) {
        startActivity(new Intent(context, TransferActivity.class));
    }


    View tranferButtonView;
    public void showAfterTransferPaymentTakeGiveList(View view) {

        tranferButtonView = view;
        showAtTgList();
    }

    void showAtTgList()
    {
        if (canTransfer) {
            tranferButtonView.setVisibility(View.GONE);

            spe = sp.edit();
            spe.putBoolean(SessionManager.IS_TRANSFER, true);
            spe.apply();

            try {

                // TODO: 2/16/2018 save paymenttg list
                for (int i = 0; i < payTgList.size(); i++) {
                    db_ref.child(Helper.AFTER_TRANSFER).child(payTgList.get(i).getMobile())
                            .setValue(payTgList.get(i));

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            setPaymentTgAfterTransfer(lv_take_give);

            canTransfer = false;
        }else
        {
            Toast.makeText(context, "Please wait", Toast.LENGTH_SHORT).show();
            dialogEffect.showDialog();


        }

    }


}




