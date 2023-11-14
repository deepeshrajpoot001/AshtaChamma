package com.deepesh.ashtaChamma;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


public class PlayWithFriends extends AppCompatActivity {

    ImageView join,create,createRoom;
    ImageButton userAccount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_friends);

        GoogleSignInClient client = GoogleSignIn.getClient( this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build());
        if (GoogleSignIn.getLastSignedInAccount(this) == null) {
            startActivity(new Intent(this, userlogin.class));
        }
        userAccount = findViewById(R.id.userAccount);
        userAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayWithFriends.this,userlogin.class));
            }
        });


        join = findViewById(R.id.join);
        create= findViewById(R.id.create);
        //   createRoom= findViewById(R.id.createRoom);

        replaceFragment(new Join());

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join.setBackgroundResource(R.drawable.background5);
                create.setBackgroundResource(0);
                replaceFragment(new Join());

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.setBackgroundResource(R.drawable.background5);
                join.setBackgroundResource(0);
                replaceFragment(new Create());

            }
        });



    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout2, fragment);
        fragmentTransaction.commit();

    }

    public void selectcolor(View view) {

        ImageView chgoti = (ImageView) view;
        int tag = Integer.parseInt(chgoti.getTag().toString());
        chgoti.setBackgroundResource(R.drawable.background4);

        for (int i = 1; i <= 4; i++) {
            if (i != tag) {
                String str = "choosegoti" + i;
                int id = getResources().getIdentifier(str, "id", getPackageName());
                chgoti = findViewById(id);
                chgoti.setBackgroundResource(0);
            }
        }
    }
}