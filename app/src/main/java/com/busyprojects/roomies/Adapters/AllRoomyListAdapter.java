package com.busyprojects.roomies.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.helper.CheckInternetReceiver;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.helper.SessionManager;
import com.busyprojects.roomies.helper.TinyDb;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.busyprojects.roomies.pojos.master.Roomy;
import com.busyprojects.roomies.pojos.transaction.Payment;
import com.busyprojects.roomies.roomyActivities.LoginActivity;
import com.busyprojects.roomies.roomyActivities.RegisterLoginActivity;
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

public class AllRoomyListAdapter extends ArrayAdapter {
    Context context;
    List<Roomy> roomyList;
    SharedPreferences sp;

    DialogEffect dialogEffect;
    String mobileLogged;
    DatabaseReference db_ref;

    Helper helper;

    String appColor,loggedRoomyMobile;


    TinyDb tinyDb;
    ArrayList<String> payingroomyMobList;

    boolean isTransfer;
    public AllRoomyListAdapter(Context context, List<Roomy> roomyList) {
        super(context, R.layout.row_all_roomy, roomyList);
        this.context = context;
        this.roomyList = roomyList;

        helper = new Helper();

        tinyDb = new TinyDb(context);
        dialogEffect = new DialogEffect(context);

        sp = context.getSharedPreferences(SessionManager.FILE_WTC, MODE_PRIVATE);

       payingroomyMobList = tinyDb.getListString(SessionManager.PAYING_ROOMY_LIST);

        mobileLogged = sp.getString(SessionManager.MOBILE, "");
       loggedRoomyMobile = Helper.getRoomyMobileFromSession(context);

        isTransfer = sp.getBoolean(SessionManager.IS_TRANSFER, false);
        appColor = sp.getString(SessionManager.APP_COLOR, SessionManager.DEFAULT_APP_COLOR);

        db_ref = Helper.getFirebaseDatabseRef();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolderTg viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolderTg();

            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.row_all_roomy, parent, false);
            viewHolder.tv_roomy_mobile = convertView.findViewById(R.id.tv_roomy_mobile);
            viewHolder.tv_roomy_name_all_roomy = convertView.findViewById(R.id.tv_roomy_name_all_roomy);
            viewHolder.but_call_roomy = convertView.findViewById(R.id.but_call_roomy);
            viewHolder.but_del_sel_roomy = convertView.findViewById(R.id.but_del_sel_roomy);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderTg) convertView.getTag();
        }
        viewHolder.tv_roomy_name_all_roomy.setText(roomyList.get(position).getName());
        viewHolder.tv_roomy_mobile.setText(roomyList.get(position).getMobile());


        viewHolder.but_call_roomy.setBackgroundColor(Color.parseColor(appColor));
        viewHolder.but_del_sel_roomy.setBackgroundColor(Color.parseColor(appColor));



        viewHolder.but_call_roomy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + roomyList.get(position).getMobile()));
                context.startActivity(intent);


            }
        });

        viewHolder.but_del_sel_roomy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    showSelectedRoomyDeleteRoomyAlert(roomyList.get(position));
            }
        });


        return convertView;
    }




    class ViewHolderTg {
        TextView tv_roomy_name_all_roomy, tv_roomy_mobile;
      Button but_call_roomy,but_del_sel_roomy;
    }

    Dialog dialogDeleteSelectedRoomyAlert;

    public void showSelectedRoomyDeleteRoomyAlert(final Roomy roomy) {

        if (CheckInternetReceiver.isOnline(context)) {
            //animationManager.animateViewForEmptyField();
            dialogDeleteSelectedRoomyAlert = new Dialog(context);
            dialogDeleteSelectedRoomyAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View v = LayoutInflater.from(context).inflate(R.layout.delete_selected_roomy_alert, null);
            TextView tv_del_sel_roomy_alert = v.findViewById(R.id.tv_del_sel_roomy_alert);
            Button but_yes_del_sel_roomy = v.findViewById(R.id.but_yes_del_sel_roomy);
             Button but_no_del_sel_roomy = v.findViewById(R.id.but_no_del_sel_roomy);

            tv_del_sel_roomy_alert.setText("Are You sure to delete "+ roomy.getName() + " ?");

            but_yes_del_sel_roomy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if (payingroomyMobList.contains(roomy.getMobile()) ) {
                        showAllTransactionsDeleteAlert(roomy);
                     }else if (isTransfer)
                    {
                        showAfterTransfersDeleteAlert(roomy);
                    }
                     else
                    {
                       // deletePaymentNpayTgAtIfTransfered();
                        // TODO: 3/24/2018 delete selected roomy
                        // TODO: 3/25/2018 del sel roomy
                        db_ref.child(Helper.ROOMY)
                                .child(roomy.getRid())
                                .removeValue();

                        dialogDeleteSelectedRoomyAlert.dismiss();

                        Toast.makeText(context, "Roomy " + roomy.getName() + " deleted ", Toast.LENGTH_SHORT).show();


                        // TODO: 3/25/2018 reset isTransfer

                        resetIsTransfer();
                        if (isDeletingMySelf(roomy))
                        {
                            exitRoomy();
                        }
                    }






                }
            });




            dialogDeleteSelectedRoomyAlert.setContentView(v);
            dialogDeleteSelectedRoomyAlert.setCancelable(false);
            dialogDeleteSelectedRoomyAlert.show();


            but_no_del_sel_roomy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogDeleteSelectedRoomyAlert.dismiss();
                }
            }); } else {
            Helper.showCheckInternet(context);

        }
    }

    void deletePaymentNpayTgAtIfTransfered()
    {


        // TODO: 2/5/2018 delete current Payment

        dialogEffect = new DialogEffect(context);
        dialogEffect.showDialog();
        db_ref.child(Helper.PAYMENT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dialogEffect.cancelDialog();
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
                  /*  spe = sp.edit();
                    spe.putBoolean(SessionManager.IS_TRANSFER, false);
                    spe.apply();*/

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







boolean isDeletingMySelf(Roomy roomy)
{
    return  roomy.getMobile().equals(loggedRoomyMobile);
}

    public void showAllTransactionsDeleteAlert(final Roomy roomy) {

        if (CheckInternetReceiver.isOnline(context)) {
            //animationManager.animateViewForEmptyField();
            dialodDeleteTranserAlert = new Dialog(context);
            dialodDeleteTranserAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View v = LayoutInflater.from(context).inflate(R.layout.delete_transfer_alert, null);
            Button but_yes_del_transfer_t = v.findViewById(R.id.but_yes_del_transfer_t);
            final Button but_no_del_transfer_t = v.findViewById(R.id.but_no_del_transfer_t);


            but_yes_del_transfer_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO: 3/24/2018 delete selected roomy
                    db_ref.child(Helper.ROOMY)
                            .child(roomy.getRid())
                            .removeValue();

                    if (roomyList.size()==1) {
                        SharedPreferences.Editor spe = sp.edit();
                        spe.putBoolean(SessionManager.IS_SEL_LAST_ROOMY_DEL, true);
                        spe.apply();
                    }


                    deletePaymentNpayTgAtIfTransfered();

                     Toast.makeText(context, "Roomy " + roomy.getName() + " deleted ", Toast.LENGTH_SHORT).show();



                    Toast.makeText(context, "Transactions Deleted", Toast.LENGTH_SHORT).show();


                    // TODO: 3/25/2018 reset isTransfer
                    SharedPreferences.Editor spe = sp.edit();
                    spe.putBoolean(SessionManager.IS_TRANSFER, false);
                    spe.apply();
                    Helper.setRemoteIstransfer(mobileLogged,false);

                    dialodDeleteTranserAlert.dismiss();
                    dialogDeleteSelectedRoomyAlert.dismiss();

                    if (isDeletingMySelf(roomy))
                    {
                        exitRoomy();
                    }

                }
            });


            but_no_del_transfer_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialodDeleteTranserAlert.dismiss();
                    dialogDeleteSelectedRoomyAlert.dismiss();
                }
            });

            dialodDeleteTranserAlert.setContentView(v);
            dialodDeleteTranserAlert.setCancelable(false);
            dialodDeleteTranserAlert.show();

        } else {
            Helper.showCheckInternet(context);

        }
    }



    void resetIsTransfer()
    {

        // TODO: 3/25/2018 reset isTransfer
        SharedPreferences.Editor spe = sp.edit();
        spe.putBoolean(SessionManager.IS_TRANSFER, false);
        spe.apply();
        Helper.setRemoteIstransfer(mobileLogged,false);

    }

    void deleteAfterExistingTransfer() {

        dialogEffect = new DialogEffect(context);
        if (CheckInternetReceiver.isOnline(context)) {

            dialogEffect.showDialog();

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
                     SharedPreferences.Editor spe = sp.edit();
                        spe.putBoolean(SessionManager.IS_TRANSFER, false);
                        spe.apply();
                        Helper.setRemoteIstransfer(mobileLogged,false);

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


    Dialog dialodDeleteTranserAlert;

    public void showAfterTransfersDeleteAlert(final Roomy roomy) {

        if (CheckInternetReceiver.isOnline(context)) {
            //animationManager.animateViewForEmptyField();
            dialodDeleteTranserAlert = new Dialog(context);
            dialodDeleteTranserAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View v = LayoutInflater.from(context).inflate(R.layout.delete_transfer_del_alert, null);
            Button but_yes_del_transfer_t = v.findViewById(R.id.but_yes_del_transfer_t);
            final Button but_no_del_transfer_t = v.findViewById(R.id.but_no_del_transfer_t);


            but_yes_del_transfer_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    resetIsTransfer();
                    deleteAfterExistingTransfer();
                    // deletePaymentNpayTgAtIfTransfered();
                    // TODO: 3/24/2018 delete selected roomy

                    // TODO: 3/25/2018 del sel roomy
                    db_ref.child(Helper.ROOMY)
                            .child(roomy.getRid())
                            .removeValue();

                    dialodDeleteTranserAlert.dismiss();
                    dialogDeleteSelectedRoomyAlert.dismiss();

                    Toast.makeText(context, "Roomy " + roomy.getName() + " deleted ", Toast.LENGTH_SHORT).show();

                    if (isDeletingMySelf(roomy))
                    {
                        exitRoomy();
                    }


                }
            });


            but_no_del_transfer_t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dialodDeleteTranserAlert.dismiss();
                    dialogDeleteSelectedRoomyAlert.dismiss();
                }
            });

            dialodDeleteTranserAlert.setContentView(v);
            dialodDeleteTranserAlert.setCancelable(false);
            dialodDeleteTranserAlert.show();

        } else {
            Helper.showCheckInternet(context);

        }
    }

void  exitRoomy()
{Intent  intentExit = new Intent(context, RegisterLoginActivity.class);
intentExit.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    context.startActivity(intentExit);
}

}




