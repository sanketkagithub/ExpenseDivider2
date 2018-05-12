package com.busyprojects.roomies.roomyActivities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.helper.TinyDb;
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

    EditText et_reg_name, et_reg_mob;
    TextView tv_log_reg_title;
    TinyDb tinyDb;


    DatabaseReference db_ref;

    DialogEffect dialogEffect;

    // String roomNoInSession;

    SharedPreferences sp;
    SharedPreferences.Editor spe;
    private AnimationManager animationManager;
    String roomNoInSession;


    // boolean CheckInternetReceiver.isOnline(this);
    // RelativeLayout rel_login_reg, rel_login, rel_register;


    ArrayList<String> roomyMobList;
    boolean isTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tinyDb = new TinyDb(context);
        roomNoInSession = Helper.getRoomNoFromSession(context);
        roomyMobList = tinyDb.getListString(SessionManager.ROOMY_MOBILE_LIST);

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
    }


    String mobileLogin;


    public void registerRoomy(View view) {
        //animationManager.animateButton(view,context);
        if (CheckInternetReceiver.isOnline(this)) {
            final String roomyMobile = et_reg_mob.getText().toString().trim();
            final String roomyName = et_reg_name.getText().toString().trim();

            if (roomyMobile.equals("") || roomyName.equals("")) {
                Toast.makeText(context, "Please Check Empty Fields", Toast.LENGTH_SHORT).show();
            } else {


             //   if (CheckInternetReceiver.isOnline(context)) {


                    registerUniqueRoomy(roomyName,roomyMobile);

               // } else {
                //    Helper.showCheckInternet(context);
                //}

            }

        } else {
            Helper.showCheckInternet(context);

        }
    }


    void saveRoommate() {

        if (CheckInternetReceiver.isOnline(this)) {


            String rid = Helper.randomString(10);
            String nameS = et_reg_name.getText().toString();
            String mobileS = et_reg_mob.getText().toString();
            String registrationDateTime = Helper.getCurrentDateTime();


            if (nameS.equals("") || mobileS.equals("")) {
                ToastManager.showToast(context, Helper.EMPTY_FIELD);
                if (nameS.equals("")) {
                    et_reg_name.setText("");
                    animationManager.animateViewForEmptyField(et_reg_name, context);
                }


                if (mobileS.equals("")) {
                    et_reg_mob.setText("");
                    animationManager.animateViewForEmptyField(et_reg_mob, context);
                }


            } else {

                if (!isRoomyMobileExist(mobileS)) {

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
                    if (isTransfer || isRemoteTransfer) {

                        showAfterTransfersDeleteAlert(roomy);

                    } else {
                        db_ref.child(Helper.ROOMY).child(rid)
                                .setValue(roomy);

                        ToastManager.showToast(context, Helper.REGISTERD);


                        dialogEffect.cancelDialog();

                        et_reg_mob.setText("");
                        et_reg_name.setText("");
                        // onBackPressed();

                        Helper.setRoomyNameIsSession(context, nameS);
                        Helper.setRoomyMobileIsSession(context, mobileS);
                        startActivity(new Intent(context, HomeActivity.class));

                    }


                } else {
                    Toast.makeText(context, "Mobile number already exist.", Toast.LENGTH_SHORT).show();
                }

                // deletePaymentNpayTgAtIfTransfered();

                Helper.showCheckInternet(this);
            }
        }
    }


    boolean isRoomyMobileExist(String roomyMobile) {

        return roomyMobList.contains(roomyMobile);

    }

    Dialog dialodDeleteTranserAlert;

    public void showAfterTransfersDeleteAlert(final Roomy roomy) {

        if (CheckInternetReceiver.isOnline(context)) {
            //animationManager.animateViewForEmptyField();
            dialodDeleteTranserAlert = new Dialog(context);

            try {
                dialodDeleteTranserAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            } catch (Exception e) {
                e.printStackTrace();
            }

            View v = LayoutInflater.from(context).inflate(R.layout.delete_transfer_add_alert, null);
            Button but_yes_del_transfer_t = v.findViewById(R.id.but_yes_del_transfer_t);
            final Button but_no_del_transfer_t = v.findViewById(R.id.but_no_del_transfer_t);


            but_yes_del_transfer_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialodDeleteTranserAlert.dismiss();
                    deleteAfterExistingTransfer(roomy);

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

        dialogEffect = new DialogEffect(context);
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

                            assert payTg != null;
                            if (payTg.getMobileLogged().equals(roomy.getMobileLogged())) {
                                db_ref.child(Helper.AFTER_TRANSFER)
                                        .child(payTg.getPayTgId())
                                        .removeValue();

                            }


                        }










                        // TODO: 2/25/2018 reset transfer session
                        spe = sp.edit();
                        spe.putBoolean(SessionManager.IS_TRANSFER, false);
                        spe.apply();
                        Helper.setRemoteIstransfer(roomy.getMobileLogged(), false);


                        // TODO: 3/31/2018 then as usual save roomy
                        db_ref.child(Helper.ROOMY).child(roomy.getRid())
                                .setValue(roomy);

                        ToastManager.showToast(context, Helper.REGISTERD);

                        Toast.makeText(context, "Transfered payments are Deleted  and new roomy is saved", Toast.LENGTH_SHORT).show();
                        Helper.setRoomyNameIsSession(context, roomy.getName());
                        Helper.setRoomyMobileIsSession(context, roomy.getMobile());

                        startActivity(new Intent(context, HomeActivity.class));





                    } catch (Exception e) {
                        spe = sp.edit();
                        spe.putBoolean(SessionManager.IS_TRANSFER, false);
                        spe.apply();
                        Helper.setRemoteIstransfer(roomy.getMobileLogged(), false);

                        e.printStackTrace();
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


    boolean isRemoteTransfer;

    void takeRemoteIsTransfer() {
        dialogEffect.showDialog();
        db_ref.child(Helper.IS_TRANSFER)
                .child(roomNoInSession)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dialogEffect.cancelDialog();

                        try {
                            isRemoteTransfer = dataSnapshot.getValue(Boolean.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            isRemoteTransfer = false;
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    void registerUniqueRoomy(String roomyName, String roomyMobile)
    {
        if (roomyMobList.contains(roomyMobile)) {
            Toast.makeText(context, "Roommate with mobile no "+roomyMobile +
                    " already exist in Room no " + roomNoInSession, Toast.LENGTH_SHORT).show();
        } else {
    //        if (CheckInternetReceiver.isOnline(this)) {
                String rid = Helper.randomString(10);
//                String nameS = et_reg_name.getText().toString();
//                String mobileS = et_reg_mob.getText().toString();
                String registrationDateTime = Helper.getCurrentDateTime();


               /* if (nameS.equals("") || mobileS.equals("")) {
                    ToastManager.showToast(context, Helper.EMPTY_FIELD);
                    if (nameS.equals("")) {
                        et_reg_name.setText("");
                        animationManager.animateViewForEmptyField(et_reg_name, context);
                    }


                    if (mobileS.equals("")) {
                        et_reg_mob.setText("");
                        animationManager.animateViewForEmptyField(et_reg_mob, context);
                    }
*/

                // } else {

                //   if (!isRoomyMobileExist(mobileS)) {

                dialogEffect.showDialog();

                // TODO: 1/27/2018 save unique roomy here
                Roomy roomy = new Roomy();
                roomy.setMobile(roomyMobile);
                roomy.setName(roomyName);
                roomy.setRid(rid);
                roomy.setMacAddress(Helper.getMacAddr());
                roomy.setMobileLogged(roomNoInSession);
                roomy.setRegistrationDateTime(registrationDateTime);


                // TODO: 3/25/2018 delete pp if isTransfered
                if (isTransfer || isRemoteTransfer) {

                    showAfterTransfersDeleteAlert(roomy);

                } else {
                    db_ref.child(Helper.ROOMY).child(rid)
                            .setValue(roomy);

                    ToastManager.showToast(context, Helper.REGISTERD);


                    dialogEffect.cancelDialog();

                    et_reg_mob.setText("");
                    et_reg_name.setText("");
                    // onBackPressed();

                    Helper.setRoomyNameIsSession(context, roomyName);
                    Helper.setRoomyMobileIsSession(context, roomyMobile);

                    Intent intentHome = new Intent(context,HomeActivity.class);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intentHome);

                    //startActivity(new Intent(context, HomeActivity.class));

                }


//            } else {
               // Toast.makeText(context, "Mobile number already exist.", Toast.LENGTH_SHORT).show();
  //          }

            // deletePaymentNpayTgAtIfTransfered();

           // Helper.showCheckInternet(this);
        }
    }

//        }


}




