package com.loginpack;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a666299.impressions.MainActivity;
import com.example.a666299.impressions.R;
import com.example.a666299.impressions.Welcome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.sup_fname) EditText s_fname;
    @BindView(R.id.sup_lname) EditText s_lname;
    @BindView(R.id.sup_email) EditText s_email;
    @BindView(R.id.sup_password) EditText s_pass;
    @BindView(R.id.sup_cpassword) EditText s_cpass;
    @BindView(R.id.sup_mobilenumber) EditText s_mobilenumber;
    @BindView(R.id.signup) Button signup;


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthlistener;
    DatabaseReference mDatabase;

    @OnClick(R.id.signup)
    public void onClick()
    {
        final String email,pass,fname,lname,mob,name;

        email = s_email.getText().toString().trim();
        pass = s_pass.getText().toString().trim();
        fname = s_fname.getText().toString().trim();
        lname = s_lname.getText().toString().trim();
        mob = s_mobilenumber.getText().toString().trim();


        if((!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) && (!TextUtils.isEmpty(pass) && pass.length() >= 6))
        {
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        Toast.makeText(SignupActivity.this,"Registration Successful",Toast.LENGTH_LONG).show();
                        mAuth.signInWithEmailAndPassword(email,pass);
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("D's Users");
                        DatabaseReference currentuserdb = mDatabase.child(mAuth.getCurrentUser().getUid());
                        currentuserdb.child("Fname").setValue(fname);
                        currentuserdb.child("Lname").setValue(lname);
                        currentuserdb.child("Email").setValue(email);
                        currentuserdb.child("Mob").setValue(mob);

                        startActivity(new Intent(SignupActivity.this,Welcome.class));
                    }
                    else
                    {
                        Toast.makeText(SignupActivity.this,"Registration Failed",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
        else
        {
            Toast.makeText(this,"Invalid Email-id or Password",Toast.LENGTH_LONG).show();
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {

                }
                else
                {

                }

            }
        };




    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthlistener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthlistener);
    }
}
