package com.busyprojects.roomies.roomyActivities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.busyprojects.roomies.Adapters.RoomySpinnerAdapterr;
import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.AnimationManager;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.helper.TinyDb;
import com.busyprojects.roomies.helper.ToastManager;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.master.PayingItems;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class
PayNowActivity extends Activity {

    String mobileLogged;

    Context context = PayNowActivity.this;
    DialogEffect dialogEffect;
    EditText et_amount;

    EditText et_paying_item;
    int totalRoommates;

    ProgressBar progressBarSuggImage;

    ListView lv_items_suggestions;

    ImageView iv_main_paying_item;
    RelativeLayout rel_main_paying_item_mage;
    boolean canTransfer = false;
    double totalAfterTransfer;
    String amount;

    double totalAmountPaid;
    double amountVariation;

    boolean haveTotalAmountPaid;


    int totalRoomates;


    Spinner spinner_roomy;
    FirebaseDatabase fb_db;
    DatabaseReference db_ref, cc_ref, key_ref_cc, cc_inner_ref;        // TODO : one database ref

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    AnimationManager animationManager;
    LinearLayout ll_paying_amount, ll_paying_item;

    Helper helper;

    List<PayTg> payTgList;
    List<PayTg> payTgListAt;

    Map<String, String> mapItems;
    String payingItemImageUrl="", defaultImageUrl;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_now);
        db_ref = Helper.getFirebaseDatabseRef();

        helper = new Helper();
        lv_items_suggestions = findViewById(R.id.lv_items_suggestions);
        progressBarSuggImage = findViewById(R.id.progressBarSuggImage);


        dialogEffect = new DialogEffect(this);



       try {

           setSuggestionsInList();
       }catch (Exception e)
       {
           e.printStackTrace();
       }
        lv_items_suggestions.setVisibility(View.GONE);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
        mobileLogged = sp.getString(SessionManager.MOBILE, "");
        defaultImageUrl = getResources().getString(R.string.default_image);

        spinner_roomy = findViewById(R.id.spinner_roomy);
        rel_main_paying_item_mage = findViewById(R.id.rel_main_paying_item_mage);
        rel_main_paying_item_mage.setVisibility(View.GONE);
        iv_main_paying_item = findViewById(R.id.iv_main_paying_item);
        RelativeLayout rel_iv_roomy_home = findViewById(R.id.rel_iv_roomy_home);
        ImageView iv_amount_to_pay = findViewById(R.id.iv_amount_to_pay);
        ImageView iv_paying_item = findViewById(R.id.iv_paying_item);
        Button but_cancel_pay = findViewById(R.id.iv_cancel_pay);
        Button but_pay = findViewById(R.id.but_pay);
        ll_paying_amount = findViewById(R.id.ll_paying_amount);
        ll_paying_item = findViewById(R.id.ll_paying_item);
        et_paying_item = findViewById(R.id.et_paying_item);

        payTgListAt = new ArrayList<>();
        et_amount = findViewById(R.id.et_amount);

        totalRoomates = sp.getInt(SessionManager.TOTAL_ROOMMATES, 0);

        animationManager = AnimationManager.getInstance();



        setSpinneerAdapter();
        //setAutocompetionTextViewItems();


        totalAfterTransfer = 0;

        getSelectedRoomy();


        String appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);
        rel_iv_roomy_home.setBackgroundColor(Color.parseColor(appColor));
        but_pay.setBackgroundColor(Color.parseColor(appColor));
        but_cancel_pay.setBackgroundColor(Color.parseColor(appColor));
//        iv_amount_to_pay.setColorFilter(Color.parseColor(appColor));
//        iv_paying_item.setColorFilter(Color.parseColor(appColor));
//        iv_cancel_pay.setColorFilter(Color.parseColor(appColor));


        int rupee = sp.getInt(SessionManager.IV_RUPEE, R.drawable.rupee);
        int payingItem = sp.getInt(SessionManager.IV_PAYING_ITEM, R.drawable.paying_item);

        iv_amount_to_pay.setImageResource(rupee);
        iv_paying_item.setImageResource(payingItem);


        if (Build.VERSION.SDK_INT >= 21) {
            et_amount.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(appColor)));
            et_paying_item.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(appColor)));

        }

        et_amount.setTextColor(ColorStateList.valueOf(Color.parseColor(appColor)));


        et_paying_item.setTextColor(ColorStateList.valueOf(Color.parseColor(appColor)));


        et_paying_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                lv_items_suggestions.setVisibility(View.VISIBLE);
                filter_listview(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {


                try {


                    setPayinItemImage(s.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        lv_items_suggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                et_paying_item.setText(payingItemsLinkedList.get(position));

                lv_items_suggestions.setVisibility(View.GONE);


            }
        });


    }




    void setPayinItemImage(String imageName) {


        if (mapItems.containsKey(imageName)) {
            payingItemImageUrl = mapItems.get(imageName);
            rel_main_paying_item_mage.setVisibility(View.VISIBLE);

            progressBarSuggImage.setVisibility(View.VISIBLE);

            Picasso.with(this).load(payingItemImageUrl).into(iv_main_paying_item, new Callback() {
                @Override
                public void onSuccess() {

               progressBarSuggImage.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onError() {

                }
            });


        } else {
            rel_main_paying_item_mage.setVisibility(View.GONE);
        }



    }

    String payingItem;
    public void payNowhere(View view) {

        if (CheckInternetReceiver.isOnline(this)) {
            //getSelectedRoomy();
            amount = et_amount.getText().toString();
           payingItem = et_paying_item.getText().toString();


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

                // TODO: 3/31/2018 payAsync
                        new PayAsyncTask().execute();

               /* String pid = Helper.randomString(10);
                // TODO: 1/27/2018 save payment;

                String currentDateTime = Helper.getCurrentDateTime();

                Payment payment = new Payment();
                double amtRf =  helper.getRoundedOffValue(Double.parseDouble(amount)) ;
                payment.setAmount(amtRf);
                payment.setPaymentDateTime(currentDateTime);
                payment.setPid(pid);
                payment.setMobileLogged(mobileLogged);
                payment.setRoomy(roomySelected);

                if (payingItemImageUrl == null)
                {

                    payingItemImageUrl  = defaultImageUrl;
                }
                payment.setPayinItemUrl(payingItemImageUrl);

                payment.setPayingItem(payingItem);


                db_ref.child(Helper.PAYMENT)
                        .child(pid)
                        .setValue(payment);

                Toast.makeText(context, "Payment done successfully", Toast.LENGTH_SHORT).show();

                payingItemImageUrl="";

                TinyDb tinyDb = new TinyDb(context);
                ArrayList<String> macAddList = tinyDb.getListString(SessionManager.MAC_ADD_LIST);
                ArrayList<String> pnIdList = tinyDb.getListString(SessionManager.PNID_LIST);

                for (int i = 0; i < macAddList.size(); i++) {


                    // TODO: 1/27/2018 save payment notification;
                    db_ref.child(Helper.PAYMENT_NOTIFICATION)
                            .child(macAddList.get(i))
                            .child(Helper.PAYMENT_LIST)
                            .child(pid)
                            .setValue(payment);


                }


                // TODO: 2/18/2018   change------
                try {


                    updateAfterTransfer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
// TODO: 2/18/2018 do alllllllllllll below

                onBackPressed();
*/
            }
        } else {
            Helper.showCheckInternet(this);
        }
    }

    Roomy roomySelected = null;

    void getSelectedRoomy() {
        spinner_roomy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                roomySelected = roomyList.get(i);

                //  Toast.makeText(context, i + "   poss", Toast.LENGTH_SHORT).show();

                getLatestAfterTransferToGetTotalAmountOfPayee();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private void getLatestAfterTransferToGetTotalAmountOfPayee() {

        if (CheckInternetReceiver.isOnline(this)) {
            dialogEffect.showDialog();
            db_ref.child(Helper.AFTER_TRANSFER)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dialogEffect.cancelDialog();
                            try {

                                payTgListAt = new ArrayList<>();

                                for (DataSnapshot dataSnapshot1 :
                                        dataSnapshot.getChildren()) {


                                    PayTg payTg = dataSnapshot1.getValue(PayTg.class);

                                    if (payTg.getMobileLogged().equals(mobileLogged)) {

                                        if (payTg.getMobile().equals(roomySelected.getMobile())) {
                                            totalAmountPaid = payTg.getAmountTg();
                                            amountVariation = payTg.getAmountVariation();
                                            haveTotalAmountPaid = true;
                                        }

                                        payTgListAt.add(payTg);
                                    }


                                }
                                payTgListAt = Helper.getSortedPaymentTakeGiveList(payTgListAt);


                                // getPayTgListAt();  // will give payTgListAt list also


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
        } else {
            Helper.showCheckInternet(context);

        }

    }

    List<String> roomyListNames = new ArrayList<>();
    List<Roomy> roomyList = new ArrayList<>();

    void setSpinneerAdapter() {

        if (CheckInternetReceiver.isOnline(this)) {
            dialogEffect.showDialog();
            db_ref.child(Helper.ROOMY).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dialogEffect.cancelDialog();
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

                    //          getSelectedRoomy();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Helper.showCheckInternet(context);

        }
    }

    void updateAfterTransfer() {
        // TODO: 2/18/2018 get  totalAmountPaid n amountVariation of paying roomy  n   also add payTgAtList

        try {
            // TODO: 2/18/2018 *********

            for (int i = 0; i < payTgListAt.size(); i++) {

                try {

                    if (payTgListAt.get(i).getMobile().equals(roomySelected.getMobile())) {
                        // TODO: 2/12/2018 add total amount paid  to atList
                        totalAmountPaid = helper.getRoundedOffValue(totalAmountPaid) + helper.getRoundedOffValue(Double.parseDouble(amount));

                       totalAmountPaid = helper.getRoundedOffValue(totalAmountPaid);


                        db_ref.child(Helper.AFTER_TRANSFER)
                                .child(payTgListAt.get(i).getPayTgId())
                                .child(Helper.TOTAL_PAID).setValue(totalAmountPaid);


                        // TODO: 2/21/2018 update new total paid of payee

                        PayTg payTg = payTgListAt.get(i);

                        payTg.setAmountTg(totalAmountPaid);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            totalAmountPaid = 0;


            // TODO: 2/19/2018 total paid by all    in AfterTransfer
            for (int i = 0; i < payTgListAt.size(); i++) {

                // TODO: 2/12/2018 find total.......total/roomysize......each
                // TODO: 2/12/2018    totPaid -  each =   amountVar
                totalAfterTransfer = helper.getRoundedOffValue(totalAfterTransfer) +
                        helper.getRoundedOffValue(payTgListAt.get(i).getAmountTg());

            }


            double eachHasToPayAfterTransfer = totalAfterTransfer / totalRoomates;

           eachHasToPayAfterTransfer = helper.getRoundedOffValue(eachHasToPayAfterTransfer);


            for (int i = 0; i < payTgListAt.size(); i++) {


                double totalAmountVar = payTgListAt.get(i).getAmountTg() - eachHasToPayAfterTransfer;

               totalAmountVar = helper.getRoundedOffValue(totalAmountVar);

                db_ref.child(Helper.AFTER_TRANSFER)
                        .child(payTgListAt.get(i).getPayTgId())
                        .child(Helper.AMOUNT_VARIATION)
                        .setValue(totalAmountVar);
            }
            totalAfterTransfer = 0;

            //   Toast.makeText(context, "At updated", Toast.LENGTH_SHORT).show();

            canTransfer = true;

            // TODO: 2/18/2018 *********


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void setAutocompetionTextViewItems() {

        String sr[] = getResources().getStringArray(R.array.payingItems);

        List<String> payingItemsList = Arrays.asList(sr);


//        ArrayAdapter aa = new ArrayAdapter(context, R.layout.row_et_paying_item,
//                payingItemsList);
//        et_paying_item.setAdapter(aa);


    }
    List<String> payingItemsLinkedList,payingItemsLinkedListAccu;
    ArrayAdapter aa;
    private void setSuggestionsInList()
   {

       dialogEffect.showDialog();
     // payingItemsLinkedList = new LinkedList<>();

     //  System.out.println(mapItems.size() + " " + mapItems);

      db_ref.child(Helper.PAYING_ITEMS)
     //  db_ref.child("PayingItemsTest")
              .addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {

                      dialogEffect.cancelDialog();
                      payingItemsLinkedList = new LinkedList<>();
                      payingItemsLinkedListAccu = new LinkedList<>();

                      // TODO: 3/24/2018 get all map items

                    /*  for (DataSnapshot dataSnapshot1:
                           dataSnapshot.getChildren()) {
*/

                         PayingItems payingItems =  dataSnapshot.getValue(PayingItems.class);
                          mapItems = payingItems.getItemsMap();
  //                    }

                      try {


                          for (Map.Entry<String, String> s :
                                  mapItems.entrySet()) {

                              payingItemsLinkedList.add(s.getKey());

                          }

                      }catch (Exception e)
                      {
                          e.printStackTrace();
                      }


                      aa = new ArrayAdapter(context,R.layout.paying_item_lv_layout,
                              R.id.tv_paying_item_lv,
                              payingItemsLinkedList);
                      lv_items_suggestions.setAdapter(aa);
                      payingItemsLinkedListAccu.addAll(payingItemsLinkedList);




                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });




   }

    public void cancelPayment(View view) {
        onBackPressed();
    }

    // Filter Class
    public void filter_listview(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        payingItemsLinkedList.clear();
        if (charText.length() == 0) {
            payingItemsLinkedList.addAll(payingItemsLinkedListAccu);
        } else {
            for (String pl : payingItemsLinkedListAccu)
            {
                if (pl.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    payingItemsLinkedList.add(pl);
                }
            }
        }
        aa.notifyDataSetChanged();
    }


    class PayAsyncTask extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarSuggImage.setVisibility(View.VISIBLE);

          /*  dialogEffect = new DialogEffect(context);
            dialogEffect.showDialog();
*/
        }

        @Override
        protected Void doInBackground(Void... voids) {






            String pid = Helper.randomString(10);
            // TODO: 1/27/2018 save payment;

            String currentDateTime = Helper.getCurrentDateTime();

            Payment payment = new Payment();
            double amtRf =  helper.getRoundedOffValue(Double.parseDouble(amount)) ;
            payment.setAmount(amtRf);
            payment.setPaymentDateTime(currentDateTime);
            payment.setPid(pid);
            payment.setMobileLogged(mobileLogged);
            payment.setRoomy(roomySelected);

            if (payingItemImageUrl == null)
            {

                payingItemImageUrl  = defaultImageUrl;
            }
            payment.setPayinItemUrl(payingItemImageUrl);

            payment.setPayingItem(payingItem);


            db_ref.child(Helper.PAYMENT)
                    .child(pid)
                    .setValue(payment);


            payingItemImageUrl="";

            TinyDb tinyDb = new TinyDb(context);
            ArrayList<String> roomyMobileList = tinyDb.getListString(SessionManager.ROOMY_MOBILE_LIST);
            ArrayList<String> pnIdList = tinyDb.getListString(SessionManager.PNID_LIST);

            for (int i = 0; i < roomyMobileList.size(); i++) {


                // TODO: 1/27/2018 save payment notification;
                db_ref.child(Helper.PAYMENT_NOTIFICATION)
                        .child(roomyMobileList.get(i))
                        .child(Helper.PAYMENT_LIST)
                        .child(pid)
                        .setValue(payment);


            }


            // TODO: 2/18/2018   change------
            try {


                updateAfterTransfer();
            } catch (Exception e) {
                e.printStackTrace();
            }
// TODO: 2/18/2
            return null;


        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

           // dialogEffect.cancelDialog();
            Toast.makeText(context, "Payment done successfully", Toast.LENGTH_SHORT).show();

            progressBarSuggImage.setVisibility(View.GONE);
            onBackPressed();
        }


    }

}
