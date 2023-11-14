package com.deepesh.ashtaChamma;


import static com.deepesh.ashtaChamma.Player2.p2cond;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsCallback;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;


public class Front2 extends AppCompatActivity {
    ImageView playOnline,playWithFriends,vsComputer,passAndPlay;
    public static String myName;
    public static String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front2);

        //remove battery
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        playOnline = findViewById(R.id.playOnline);
        playWithFriends = findViewById(R.id.playWithFriends);
        vsComputer= findViewById(R.id.vsComputer);
        passAndPlay = findViewById(R.id.passAndPlay);




        SharedPreferences getStarted= getSharedPreferences("AshtaChamma",MODE_PRIVATE);
        myName = getStarted.getString("myName","player");


        findViewById(R.id.userAccountFront).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Front2.this, userlogin.class));
            }
        });




        playOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Front2.this,"CURRENTLY UNAVAILABLE",Toast.LENGTH_SHORT).show();
            }
        });
        playWithFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode="online";
                startActivity(new Intent(Front2.this,PlayWithFriends.class));

            }
        });
        vsComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode="vsComputer";
                p2cond = 1;
                startActivity(new Intent(Front2.this,MainActivity.class));



            }
        });
        passAndPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  mode="offline";
                Intent intent = new Intent(Front2.this, PassAndPlay.class);
                startActivity(intent);



            }
        });


    }


}