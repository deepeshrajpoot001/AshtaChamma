package com.deepesh.ashtaChamma;

import static com.deepesh.ashtaChamma.Front2.myName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class userlogin extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    ImageView googleBtn;
    EditText editText;
    Button playAsGuest;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        editText = findViewById(R.id.userName);
        playAsGuest = findViewById(R.id.playAsGuest);
        googleBtn= findViewById(R.id.googleBtn);



       playAsGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                 Front2.myName = editText.getText().toString();
                                SharedPreferences shrd= getSharedPreferences("AshtaChamma",MODE_PRIVATE);
                                SharedPreferences.Editor editor= shrd.edit();
                                editor.putString("myName",Front2.myName);
                                editor.apply();
                                signOut();

            }
        });





        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

     if(account!=null){
          googleBtn.setImageResource(R.drawable.googlebtnsignout);
      }

       googleBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(account==null)
                   signIn();
               else
                   signOut();


           }
       });


    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount result = completedTask.getResult(ApiException.class);
            Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                Front2.myName = acct.getDisplayName();
                SharedPreferences shrd= getSharedPreferences("AshtaChamma",MODE_PRIVATE);
                SharedPreferences.Editor editor= shrd.edit();
                editor.putString("myName", Front2.myName);
                editor.apply();

            }

            googleBtn.setImageResource(R.drawable.googlebtnsignout);
            finish();


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show();
        }
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(userlogin.this,"signout success",Toast.LENGTH_SHORT).show();
                        googleBtn.setImageResource(R.drawable.googlebtnsignin);
                        finish();
                    }
                });
    }

    public void onBackPressed() {
        if (this.account == null) {
            Toast.makeText(this, "Please, SIGN IN", 0).show();
        } else {
            super.onBackPressed();
        }
    }

}