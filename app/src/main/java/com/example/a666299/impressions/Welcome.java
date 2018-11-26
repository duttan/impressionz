package com.example.a666299.impressions;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.loginpack.SignupActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Welcome extends AppCompatActivity {
    @BindView(R.id.firstn) TextView disp_firstn;

    String name1;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //Intent intent = getIntent();
        //String str = intent.getStringExtra("user");
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = mAuth.getCurrentUser();
                if( user == null)
                {
                   // mAuth.signOut();
                    startActivity(new Intent(Welcome.this,MainActivity.class));
                   // finish();

                }

            }
        };








    }



    public void showusername()
    {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null)
        {
            for(UserInfo profile : user.getProviderData())
            {
                String providerId = profile.getProviderId();
                // UID specific to the provider
                String uid = profile.getUid();

                name1 = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
                disp_firstn.setText("Hi "+name1);
                Toast.makeText(this,name1,Toast.LENGTH_LONG).show();
            }


        }

    }

    public void func_help()
    {

        Toast.makeText(this,name1,Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.welcome_activity_actions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_help:
                func_help();
                return true;
            case R.id.menu_logout:
                {

                    mAuth.signOut();
                    finish();
                   // startActivity(new Intent(Welcome.this,MainActivity.class));
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthlistener);
        showusername();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthlistener);
    }
}
