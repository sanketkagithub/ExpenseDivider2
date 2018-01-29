package com.busyprojects.roomies.CustomerActivities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.busyprojects.roomies.Config.Config_Firebase;
import com.busyprojects.roomies.R;
import com.busyprojects.roomies.pojos.CustomerDetails;

public class RegisterCusActivity extends AppCompatActivity
{
    Context con = RegisterCusActivity.this;

    EditText et_email,et_pass,et_name, et_mob,et_address;

    String email_s, pass_s,name_s, mob_s,add_s;

    ProgressBar progressBar_reg;

    // TODO : firebase references

    FirebaseAuth fb_auth;   // TODO: auth to register
    String uid;


    FirebaseDatabase fb_db;
    DatabaseReference db_ref,        // TODO : one database ref

             customer_details_ref,     // TODO : can be multiple pojos - collections

                uid_ref, cus_det_ref_inner;             // TODO : uid ref



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cus);


        et_email = (EditText)findViewById(R.id.et_email_reg);
        et_pass = (EditText)findViewById(R.id.et_pass_reg);
        et_name = (EditText)findViewById(R.id.et_nam_reg);
        et_mob = (EditText)findViewById(R.id.et_mob_reg);
        et_address = (EditText)findViewById(R.id.et_add_reg);


        progressBar_reg = (ProgressBar)findViewById(R.id.progressBar_reg);
        progressBar_reg.setVisibility(View.INVISIBLE);

        // TODO : firebase Auth init

        fb_auth =   FirebaseAuth.getInstance();


        // TODO : firebase Database init

      fb_db =  FirebaseDatabase.getInstance();

       db_ref = fb_db.getReference();

        Firebase.setAndroidContext(con);

    }


    public void register_it_go_to_LoginCusActivity(View view)
    {
        email_s =  et_email.getText().toString();
        pass_s =  et_pass.getText().toString();
        name_s =  et_name.getText().toString();
        mob_s =  et_mob.getText().toString();
        add_s =  et_address.getText().toString();


        progressBar_reg.setVisibility(View.VISIBLE);


      Task task = fb_auth.createUserWithEmailAndPassword(email_s,pass_s);




        task.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {


                progressBar_reg.setVisibility(View.INVISIBLE);

                if (task.isSuccessful())
                {

                    Toast.makeText(RegisterCusActivity.this, "registered successfully", Toast.LENGTH_SHORT).show();

                 uid =    fb_auth.getCurrentUser().getUid();


                  customer_details_ref =  db_ref.child(Config_Firebase.CUSTOMER_DETAILS);

               uid_ref =     customer_details_ref.child(uid);


                 cus_det_ref_inner =   uid_ref.child(Config_Firebase.CUSTOMER_DETAILS);

                    // TODO : set pojo

                    CustomerDetails cus_detail = new CustomerDetails();

                    cus_detail.setEmail(email_s);

                    cus_detail.setName(name_s);


                    cus_detail.setMobile(mob_s);

                    cus_detail.setAddress(add_s);


                    cus_det_ref_inner.setValue(cus_detail);

                    Toast.makeText(RegisterCusActivity.this, uid, Toast.LENGTH_SHORT).show();


                    startActivity(new Intent(con,LoginCusActivity.class));


                }




            }
        });






    }










}
