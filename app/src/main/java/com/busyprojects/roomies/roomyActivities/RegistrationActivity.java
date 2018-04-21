package com.busyprojects.roomies.roomyActivities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.AnimationManager;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.RuntimePermissionsCs;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.helper.ToastManager;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.busyprojects.roomies.pojos.master.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends Activity {
    Context context = RegistrationActivity.this;

    EditText  et_reg_name,et_reg_mob;
    TextView tv_log_reg_title;


    DatabaseReference db_ref;

    DialogEffect dialogEffect;

    SharedPreferences sp;
    SharedPreferences.Editor spe;
private AnimationManager animationManager;
    String roomNoInSession;

    RuntimePermissionsCs runtimePermissionsCs;
   // boolean CheckInternetReceiver.isOnline(this);
   // RelativeLayout rel_login_reg, rel_login, rel_register;

    boolean isTransfer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        roomNoInSession = Helper.getRoomNoFromSession(context);

        sp = getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);
//        CheckInternetReceiver.isOnline(this) = sp.getBoolean(SessionManager.IS_INTERNET, false);
        //CheckInternetReceiver.isOnline(this) = CheckInternetReceiver.isOnline(this);
        db_ref = Helper.getFirebaseDatabseRef();
        animationManager = AnimationManager.getInstance();

        isTransfer = sp.getBoolean(SessionManager.IS_TRANSFER, false);
        mobileLogin = sp.getString(SessionManager.MOBILE, "");
        dialogEffect = new DialogEffect(context);

        et_reg_name = findViewById(R.id.et_reg_name);
        et_reg_mob = findViewById(R.id.et_reg_mobile);

        tv_log_reg_title = findViewById(R.id.tv_log_reg_title);

takeRemoteIsTransfer();
      /*  rel_login_reg = findViewById(R.id.rel_log_reg);
        rel_login = findViewById(R.id.rel_login);
        rel_register = findViewById(R.id.rel_register);



        setInitialVisibility();
*/
        if (Build.VERSION.SDK_INT>=23)
        {
        runtimePermissionsCs = new RuntimePermissionsCs(this);
        runtimePermissionsCs.getPermissions();
    }
    }





    String mobileLogin,rooomyMobile;




/*
    public void loginRoomyold(View view) {

        if (CheckInternetReceiver.isOnline(this)) {
            mobileLogin = et_reg_room_no.getText().toString();
            rooomyMobile = et_reg_mob.getText().toString();

            if (mobileLogin.equals("")||rooomyMobile.equals("")) {

                Toast.makeText(context, "Please Check Empty Fields", Toast.LENGTH_SHORT).show();


            } else {

                dialogEffect.showDialog();
                final List<String> userListMobile = new ArrayList<>();
                // TODO: 1/28/2018 validate login mobile
                db_ref.child(Helper.USER)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                dialogEffect.cancelDialog();
                                userListMobile.clear();

                                for (DataSnapshot dataSnapshot1 :
                                        dataSnapshot.getChildren()) {
                                    User user = dataSnapshot1.getValue(User.class);
                                    userListMobile.add(user.getMobile());
                                }

                                if (userListMobile.contains(mobileLogin)) {

                                    // TODO: 1/28/2018 save main roomate (user) in session
                                    spe = sp.edit();
                                    spe.putString(SessionManager.MOBILE, mobileLogin);
                                    spe.apply();

                                    SharedPreferences spUc = getSharedPreferences(SessionManager.FILE_UC, MODE_PRIVATE);

                                    spe = spUc.edit();
                                    spe.putString(SessionManager.LOGGED_MOBILE_UC, mobileLogin);
                                    spe.apply();


                                    saveRoomyMacAddressForNotification();


                                    startActivity(new Intent(context, HomeActivity.class));


                                } else {

                                    // TODO: 3/24/2018  open admin
                                    if (mobileLogin.equals("0000"))
                                    {



                                    }
                                    else {
                                        Toast.makeText(context, "Invalid Login", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }

        }else
        {
            Helper.showCheckInternet(context);

        }
    }
*/


/*
    void saveRoomyMacAddressForNotification() {
        String macAddress = Helper.getMacAddr();

        if (macAddress.equals("")) {
            macAddress = "emulatorMacAdd";
        }
        spe = sp.edit();
        spe.putString(SessionManager.MAC_ADDRESS, macAddress);
        spe.apply();


       SharedPreferences spUc = getSharedPreferences(SessionManager.FILE_UC,MODE_PRIVATE);
        int payNotfSize = spUc.getInt(SessionManager.PNID_LIST, 0);

        System.out.println(payNotfSize + " payNotfSize ");

        String pnId = Helper.randomString(10);
        PaymentNotification paymentNotification = new PaymentNotification();
        paymentNotification.setMacAddress(macAddress);
        paymentNotification.setMobileLogged(mobileLogin);
        paymentNotification.setPnid(pnId);

        HashMap<String, Payment> paymentMap = new HashMap<>();

        // TODO: 3/1/2018  dummy Payment
        Payment payment = new Payment();
        payment.setPid("xdvxcvcxcv");
        payment.setMobileLogged(mobileLogin);

        for (int i = 0; i < payNotfSize; i++) {
            paymentMap.put(i + "defaultKeys", payment);
        }


        paymentNotification.setPaymentList(paymentMap);

        db_ref.child(Helper.PAYMENT_NOTIFICATION)
                .child(macAddress)
                .setValue(paymentNotification);


        //getRoomsAllMacAddressListInSession();


    }
*/

    public void registerRoomy(View view) {

if (CheckInternetReceiver.isOnline(this)) {
    final String roomyReg = et_reg_mob.getText().toString().trim();
    final String roomyName = et_reg_name.getText().toString().trim();

    if (roomyReg.equals("")||roomyName.equals("")) {
        Toast.makeText(context, "Please Check Empty Fields", Toast.LENGTH_SHORT).show();
    } else {

        if (CheckInternetReceiver.isOnline(context)) {


            dialogEffect.showDialog();

            db_ref.child(Helper.USER)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dialogEffect.cancelDialog();

                            List<String> userList = new ArrayList<>();

                            for (DataSnapshot dataSnapshot1 :
                                    dataSnapshot.getChildren()) {

                                User userDb = dataSnapshot1.getValue(User.class);

                                userList.add(userDb.getMobile());

                            }


                            if (userList.contains(roomNoInSession)) {
                                //      Toast.makeText(context, "User Already Exists", Toast.LENGTH_SHORT).show();



                            } else {

                                // TODO: 2/25/2018 save room
                                String uid = Helper.randomString(10);
                                User user = new User();
                                user.setUid(uid);
                                user.setMobile(roomNoInSession);


                                // saveRoomNo(User)
                                db_ref.child(Helper.USER).child(uid)
                                        .setValue(user);
                                Toast.makeText(context, "New Room Registered Successfully", Toast.LENGTH_SHORT).show();



                            }

                            saveRoommate();

                            Toast.makeText(context, "Roommate Registered Successfully", Toast.LENGTH_SHORT).show();



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

}else
{
    Helper.showCheckInternet(context);

}
    }


    void saveRoommate()
    {

        if (CheckInternetReceiver.isOnline(this)) {


            dialogEffect.showDialog();

            String rid = Helper.randomString(10);
            String nameS = et_reg_name.getText().toString();
            String mobileS = et_reg_mob.getText().toString();
            String registrationDateTime = Helper.getCurrentDateTime();


            if (nameS.equals("") || mobileS.equals("")) {
                ToastManager.showToast(context, Helper.EMPTY_FIELD);
                if (nameS.equals(""))
                {
                    et_reg_name.setText("");
                    animationManager.animateViewForEmptyField(et_reg_name,context);
                }


                if (mobileS.equals(""))
                {
                    et_reg_mob.setText("");
                    animationManager.animateViewForEmptyField(et_reg_mob,context);
                }





            } else {
                dialogEffect.showDialog();

                // TODO: 1/27/2018 save unique roomy here
                Roomy roomy = new Roomy();
                roomy.setMobile(mobileS);
                roomy.setName(nameS);
                roomy.setRid(rid);
                roomy.setMacAddress(Helper.getMacAddr());
                roomy.setMobileLogged(roomNoInSession);
                roomy.setRegistrationDateTime(registrationDateTime);




                // TODO: 3/25/2018 delete pp if isTransfered
                if (isTransfer || isRemoteTransfer )
                {

                    showAfterTransfersDeleteAlert(roomy);

                }else
                {
                    db_ref.child(Helper.ROOMY).child(rid)
                            .setValue(roomy);
                    dialogEffect.cancelDialog();

                    ToastManager.showToast(context, Helper.REGISTERD);


                    et_reg_mob.setText("");
                    et_reg_name.setText("");
                    onBackPressed();

                    Helper.setRoomyNameIsSession(context,nameS);
                    Helper.setRoomyMobileIsSession(context,mobileS);
                    startActivity(new Intent(context,HomeActivity.class));

                }



                dialogEffect.cancelDialog();
            }

            // deletePaymentNpayTgAtIfTransfered();
        }else
        {
            Helper.showCheckInternet(this);
        }

    }


    Dialog dialodDeleteTranserAlert;

    public void showAfterTransfersDeleteAlert(final Roomy roomy) {

        if (CheckInternetReceiver.isOnline(context)) {
            //animationManager.animateViewForEmptyField();
            dialodDeleteTranserAlert = new Dialog(context);
            dialodDeleteTranserAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View v = LayoutInflater.from(context).inflate(R.layout.delete_transfer_add_alert, null);
            Button but_yes_del_transfer_t = v.findViewById(R.id.but_yes_del_transfer_t);
            final Button but_no_del_transfer_t = v.findViewById(R.id.but_no_del_transfer_t);


            but_yes_del_transfer_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    deleteAfterExistingTransfer(roomy);
                    // TODO: 2/25/2018 reset transfer session
                    spe = sp.edit();
                    spe.putBoolean(SessionManager.IS_TRANSFER, false);
                    spe.apply();
                    Helper.setRemoteIstransfer(roomy.getMobileLogged(),false);


                    // TODO: 3/31/2018 then as usual save roomy
                    db_ref.child(Helper.ROOMY).child(roomy.getRid())
                            .setValue(roomy);

                    ToastManager.showToast(context, Helper.REGISTERD);

                    Toast.makeText(context, "Transfered payments are Deleted  and new roomy is saved", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(context,HomeActivity.class));

                   // et_mobile.setText("");
                   // et_name.setText("");
                    //onBackPressed();

                }
            });


            but_no_del_transfer_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialodDeleteTranserAlert.dismiss();
                    onBackPressed();
                }
            });

            dialodDeleteTranserAlert.setContentView(v);
            dialodDeleteTranserAlert.setCancelable(false);
            dialodDeleteTranserAlert.show();

        } else {
            Helper.showCheckInternet(context);

        }
    }



    void deleteAfterExistingTransfer(final Roomy roomy) {

        if (CheckInternetReceiver.isOnline(this)) {

            dialogEffect.showDialog();

            db_ref.child(Helper.AFTER_TRANSFER).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dialogEffect.cancelDialog();
                    try {


                        for (DataSnapshot dataSnapshot1 :
                                dataSnapshot.getChildren()) {

                            PayTg payTg = dataSnapshot1.getValue(PayTg.class);

                            if (payTg.getMobileLogged().equals(roomy.getMobileLogged())) {
                                db_ref.child(Helper.AFTER_TRANSFER)
                                        .child(payTg.getPayTgId())
                                        .removeValue();

                            }


                        }

                    } catch (Exception e) {
                        spe = sp.edit();
                        spe.putBoolean(SessionManager.IS_TRANSFER, false);
                        spe.apply();
                        Helper.setRemoteIstransfer(roomy.getMobileLogged(),false);

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








    boolean isRemoteTransfer;
    void takeRemoteIsTransfer()
    {
        dialogEffect.showDialog();
        db_ref.child(Helper.IS_TRANSFER)
                .child(roomNoInSession)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dialogEffect.cancelDialog();

                       try {
                           isRemoteTransfer = dataSnapshot.getValue(Boolean.class);
                       }catch (Exception e)
                       {
                           e.printStackTrace();
                           isRemoteTransfer = false;
                       }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
