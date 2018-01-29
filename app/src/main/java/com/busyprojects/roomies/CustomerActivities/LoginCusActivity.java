package com.busyprojects.roomies.CustomerActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.busyprojects.roomies.Config.Config_Firebase;
import com.busyprojects.roomies.R;
import com.busyprojects.roomies.pojos.CustomerDetails;

public class LoginCusActivity extends AppCompatActivity
{
    Context con = LoginCusActivity.this;

    EditText et_email, et_pass;

    String email_s, pass_s;

    SharedPreferences sp;
    SharedPreferences.Editor spe;


    FirebaseAuth fb_auth;

    ProgressBar progressBar_login;

    FirebaseDatabase fb_db;
    DatabaseReference db_ref,   cus_det_ref,    uid_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cus);


        et_email = (EditText)findViewById(R.id.et_email_log);
        et_pass = (EditText)findViewById(R.id.et_pass_log);




       fb_auth = FirebaseAuth.getInstance();

        progressBar_login = (ProgressBar)findViewById(R.id.progressBar_login);

        progressBar_login.setVisibility(View.INVISIBLE);


      fb_db =   FirebaseDatabase.getInstance();

     db_ref =   fb_db.getReference();


        Firebase.setAndroidContext(con);

    }

    public void login_this_user(View view)
    {

        progressBar_login.setVisibility(View.VISIBLE);


        email_s =   et_email.getText().toString();

        pass_s = et_pass.getText().toString();


        Task task_login = fb_auth.signInWithEmailAndPassword(email_s,pass_s);

        task_login.addOnCompleteListener(new OnCompleteListener()
        {



            @Override
            public void onComplete(@NonNull Task task) {

                progressBar_login.setVisibility(View.INVISIBLE);


                if (task.isSuccessful())
                {


                 String uid =   fb_auth.getCurrentUser().getUid();



                 cus_det_ref =   db_ref.child(Config_Firebase.CUSTOMER_DETAILS);


                   uid_ref = cus_det_ref.child(uid);


               uid_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            Iterable<DataSnapshot> iter =   dataSnapshot.getChildren();


                            for (DataSnapshot dat: iter)
                            {

//                                String det =  dat.getValue(String.class).toString();


                                CustomerDetails cus_det = dat.getValue(CustomerDetails.class);


                                //                      Toast.makeText(LoginActivity.this, det, Toast.LENGTH_SHORT).show();

                                Toast.makeText(con, cus_det.getName()
                                         + "\n" + cus_det.getMobile()+ "\n" + cus_det.getAddress() +
                                          "\n" + cus_det.getEmail(), Toast.LENGTH_SHORT).show();

                            }




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            Toast.makeText(con, "CANCELLED", Toast.LENGTH_SHORT).show();
                        }
                    });



                }else
                {
                    Toast.makeText(con, "log failed", Toast.LENGTH_SHORT).show();
                }


            }
        });





    //  startActivity(new Intent(con,WelcomeActivity.class));



    }





}
