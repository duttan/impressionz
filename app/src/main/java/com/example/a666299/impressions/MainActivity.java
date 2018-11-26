package com.example.a666299.impressions;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.loginpack.SignupActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    @BindView(R.id.login_email)
    EditText t1;
    @BindView(R.id.login_password)
    EditText t2;
    @BindView(R.id.textview)
    TextView t3;

    //GoogleApiClient googleApiClient;
    GoogleSignInClient mGoogleSignInClient;
    private static final int REQ_CODE = 9001;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthlistener;

    @OnClick({R.id.login,R.id.textview,R.id.googlesignin})
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

                //GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                //googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
                signin();
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
                Intent in = new Intent(MainActivity.this,Welcome.class);
                startActivity(in);

            }
            else
            {

            }

        }
    };
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(this,"Connection failed",Toast.LENGTH_LONG).show();

    }

    private void signin()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_CODE);

//        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
//        startActivityForResult(intent,REQ_CODE);

    }

    private void updateUI(FirebaseUser user)
    {
        if(user != null )
        {
            Intent in = new Intent(this,Welcome.class);
            //user.getProviderData();
        }
    }

    public void handleresult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();

            Intent in = new Intent(this,Welcome.class);
            in.putExtra("user",name);
            startActivity(in);
        }
        else
        {
            Toast.makeText(this,"Google Sign-In failed",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google sign in failed", e);
                // ...
            }
        }


//        if(requestCode == REQ_CODE)
//        {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleresult(result);
//        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.d("@@", "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("@@", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("@@", "signInWithCredential:failure");
                            Toast.makeText(MainActivity.this,"Authentication failed",Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });



    }
}
