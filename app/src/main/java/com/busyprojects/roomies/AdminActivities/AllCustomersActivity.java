    package com.busyprojects.roomies.AdminActivities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import com.busyprojects.roomies.Adapters.AllCustomersAdapter;
import com.busyprojects.roomies.R;
import com.busyprojects.roomies.pojos.CustomerDetails;

    public class AllCustomersActivity extends AppCompatActivity {
        ListView lv_allcustomers;

        Context con = AllCustomersActivity.this;

        ArrayList<CustomerDetails> al = new ArrayList<>();

        FirebaseDatabase fb_db;

        DatabaseReference db_ref, cus_det_ref, uid_ref;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_all_customers);


            lv_allcustomers = (ListView) findViewById(R.id.lv_allcustomers);

            fb_db = FirebaseDatabase.getInstance();

            db_ref = fb_db.getReference();


            //  cus_det_ref =   db_ref.child(Config_Firebase.CUSTOMER_DETAILS);


            db_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Map<String, Map> map = (Map<String, Map>) dataSnapshot.getValue();
                    //  mMessages.clear();
                    if (map != null) {
                        for (String key : map.keySet()) {                               //keys will be uid
//

                            cus_det_ref = db_ref.child(key);







                        //    uid_ref = cus_det_ref.child(key);


                            cus_det_ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    Map<String, Map> map = (Map<String, Map>) dataSnapshot.getValue();
                                    //  mMessages.clear();
                                    if (map != null) {
                                        for (String key : map.keySet()) {                               //keys will be uid
//


                                            Toast.makeText(AllCustomersActivity.this, key +  "  ***", Toast.LENGTH_SHORT).show();



                                        uid_ref =    cus_det_ref.child(key);




                                            uid_ref.addValueEventListener(new ValueEventListener()
                                            {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {


                                                    Iterable<DataSnapshot> it = dataSnapshot.getChildren();

                                                    for (DataSnapshot dat : it) {

                                                        CustomerDetails cus_det = (CustomerDetails) dat.getValue(CustomerDetails.class);

                                                        Toast.makeText(con,
                                                                cus_det.getName()
                                                                        + "", Toast.LENGTH_SHORT).show();


                                                      //  CustomerDetails cus_det1 = new CustomerDetails();

                                                        al.add(cus_det);


                                                    }

                                                    AllCustomersAdapter acus_adap = new AllCustomersAdapter(con, R.layout.row_all_cutomers, al);

                                                    lv_allcustomers.setAdapter(acus_adap);



                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });






                                          //  uid_ref = db_ref.child(key);


                                        }







//                                    MyAdap ma = new MyAdap();
//
//                                    lv_all.setAdapter(ma);
                                    }




                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                        }


                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });

        }


    }