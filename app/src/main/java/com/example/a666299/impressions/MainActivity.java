package com.example.a666299.impressions;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loginpack.SignupActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.login_email)
    EditText t1;
    @BindView(R.id.login_password)
    EditText t2;
    @BindView(R.id.textview)
    TextView t3;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthlistener;
    @OnClick({R.id.login,R.id.textview})
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.login:
            {
                //Login operation
                String l_email = t1.getText().toString().trim();
                String l_password = t2.getText().toString().trim();

                if(!TextUtils.isEmpty(l_email) && !TextUtils.isEmpty(l_password))
                {
                    mAuth.signInWithEmailAndPassword(l_email,l_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                startActivity(new Intent(MainActivity.this,Welcome.class));
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"User not Found",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
                break;
            }
            case R.id.googlesignin:
            {

                break;
            }
            case R.id.textview:
            {
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
                break;
            }

            default:
                break;


        }
        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
