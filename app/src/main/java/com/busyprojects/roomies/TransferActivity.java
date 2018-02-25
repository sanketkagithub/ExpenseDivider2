package com.busyprojects.roomies;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.busyprojects.roomies.Adapters.PaymentTakeGiveListAtAdapter;
import com.busyprojects.roomies.helper.DialogEffect;
import com.busyprojects.roomies.helper.Helper;
import com.busyprojects.roomies.pojos.master.PayTg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransferActivity extends AppCompatActivity {

    ListView lv_take_give_at;
    DialogEffect dialogEffect;
    DatabaseReference db_ref;
    Context context = TransferActivity.this;

    TextView tv_from,tv_to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        lv_take_give_at = findViewById(R.id.lv_take_give_at);


        db_ref = Helper.getFirebaseDatabseRef();
        dialogEffect = new DialogEffect(context);

        setPaymentTgAfterTransfer(lv_take_give_at);
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

                            payTgList.add(payTg);
                        }

                        // TODO: 2/3/2018  save PaymentTg(Each Total PAyment List) to db
                        PaymentTakeGiveListAtAdapter paymentTakeGiveListAdapter = new PaymentTakeGiveListAtAdapter(context, payTgList);
                        lv_take_give_at.setAdapter(paymentTakeGiveListAdapter);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }


    public void saveRoomy(View view)
    {
    }

    public void backToHome(View view) {
    }
}
