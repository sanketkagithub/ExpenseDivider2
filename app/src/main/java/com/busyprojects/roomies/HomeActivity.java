package com.busyprojects.roomies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.busyprojects.roomies.Adapters.PaymentListAdapter;
import com.busyprojects.roomies.Adapters.RoomySpinnerAdapterr;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.helper.ToastManager;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    Spinner spinner_roomy;
    FirebaseDatabase fb_db;
    DatabaseReference db_ref, cc_ref, key_ref_cc, cc_inner_ref;        // TODO : one database ref

    SharedPreferences sp;

    EditText et_name, et_mobile, et_amount;

    DialogEffect dialogEffect;
    Context context = HomeActivity.this;
    RelativeLayout rel_add_roomy_layout, rel_iv_roomy_home, rel_name, rel_mobile, rel_save_roomy, rel_add_roomy, rel_pay_roomy,
            rel_paymen_lay, rel_back, rel_view_payment, rel_all_paymen_lay;

    ListView lv_take_give, lv_payments;

    Button but_divide;
    TextView tv_total_amount, tv_each_payment, tv_total_roomies;
    LinearLayout ll_tot_each_roomy;

    String mobileLogged;
    View view_add_roomy_layout;


    long total = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dialogEffect = new DialogEffect(context);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");

        rel_add_roomy_layout = findViewById(R.id.rel_add_roomy_layout);
        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);

        but_divide = findViewById(R.id.but_divide);
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
        rel_all_paymen_lay = findViewById(R.id.rel_all_paymen_lay);


        spinner_roomy = findViewById(R.id.spinner_roomy);
        lv_payments = findViewById(R.id.lv_payments);


        fb_db = FirebaseDatabase.getInstance();
        db_ref = fb_db.getReference();


        setInitialVisibilities();
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

        rel_paymen_lay.setVisibility(View.GONE);


    }


    public void saveRoomy(View view) {

        dialogEffect.showDialog();
        String rid = Helper.randomString(10);
        String nameS = et_name.getText().toString();
        String mobileS = et_mobile.getText().toString();
        String registrationDateTime = Helper.getCurrentDateTime();

        // TODO: 1/27/2018 save unique roomy here
        Roomy roomy = new Roomy();
        roomy.setMobile(mobileS);
        roomy.setName(nameS);
        roomy.setRid(rid);
        roomy.setMobileLogged(mobileLogged);
        roomy.setRegistrationDateTime(registrationDateTime);

        db_ref.child(Helper.ROOMY).child(rid)
                .setValue(roomy);

        if (nameS.equals("") || mobileS.equals("")) {

            ToastManager.showToast(context, Helper.EMPTY_FIELD);
        } else {
            dialogEffect.cancelDialog();
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


        rel_paymen_lay.setVisibility(View.VISIBLE);
        setSpinneerAdapter();

    }


    List<String> roomyListNames = new ArrayList<>();
    List<Roomy> roomyList = new ArrayList<>();

    void setSpinneerAdapter() {

        db_ref.child(Helper.ROOMY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                roomyListNames.clear();
                roomyList.clear();

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

    Roomy roomy = null;

    void getSelectedRoomy() {
        spinner_roomy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                roomy = roomyList.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    public void payNowhere(View view) {
        String amount = et_amount.getText().toString();

        if (amount.equals("")) {
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
            payment.setRoomy(roomy);

            db_ref.child(Helper.PAYMENT)
                    .child(pid)
                    .setValue(payment);

            Toast.makeText(context, "Payment done successfully", Toast.LENGTH_SHORT).show();

            setInitialVisibilities();
        }

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
    void getAllRoomieesVp(final List<Payment> paymentList)
    {



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







                Log.i("====pp",paymentList.toString());

                Map<String,List<Payment>> paymentMap = new HashMap<>();

                List<Payment> onesPAymentList;


                System.out.println(roomyListVp.size() + "");


                for (int i = 0; i <roomyListVp.size()  ; i++) {

                    onesPAymentList = new ArrayList<>();

                    for (int j = 0; j < paymentList.size() ; j++) {


                        if (roomyListVp.get(i).getMobile().equals(paymentList.get(j).getRoomy().getMobile()))
                        {
                            onesPAymentList.add(paymentList.get(j));
                        }

                    }

                    paymentMap.put(roomyListVp.get(i).getMobile(),onesPAymentList);

                }


                System.out.println(paymentMap);

                System.out.println(paymentMap.size() + "   ");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    void setPaymentsTakeGiveList(final List<Payment> paymentList) {

      getAllRoomieesVp(paymentList);
       // PaymentTakeGiveListAdapter paymentListAdapterTg = new PaymentTakeGiveListAdapter(context, paymentListTg);
       // lv_take_give.setAdapter(paymentListAdapterTg);


    }







    public void backfromAllPayment(View view) {
        rel_all_paymen_lay.setVisibility(View.GONE);

        setInitialVisibilities();


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
        getAllRoomiees();


    }

    void setEachPayMent(List<Roomy> roomyList) {
        but_divide.setVisibility(View.GONE);

        ll_tot_each_roomy.setVisibility(View.VISIBLE);
        tv_each_payment.setVisibility(View.VISIBLE);

        for (int i = 0; i < paymentList.size(); i++) {


            total = total + paymentList.get(i).getAmount();

        }

        long eachAmount = total / roomyList.size();

        tv_total_amount.setText(total + "₹");
        tv_total_roomies.setText(roomyList.size() + "");
        tv_each_payment.setText(eachAmount + "₹");

    }


    public void backToHome(View view) {
        setInitialVisibilities();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setInitialVisibilities();
    }
}




