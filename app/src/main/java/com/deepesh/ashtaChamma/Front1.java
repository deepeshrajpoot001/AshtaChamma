package com.deepesh.ashtaChamma;



import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class Front1 extends AppCompatActivity {
    public static final String MSG ="com.example.cheetha.front1";
    Intent intent;
    private boolean isTouch = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1) {
            startActivity(new Intent(this, Front2.class));
            finish();
        }
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front1);
        //remove battery
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        intent = new Intent(this,Front2.class);



        new CountDownTimer(2000,1000){
            @Override
            public void onTick(long millisUntilFinished){

            }
            @Override
            public void onFinish(){

                startActivity(intent);

            }
        }.start();




    }
}