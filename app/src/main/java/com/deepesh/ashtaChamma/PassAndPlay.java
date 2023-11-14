package com.deepesh.ashtaChamma;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class PassAndPlay extends AppCompatActivity {
    public static final String MSG ="com.deepesh.ashtaChamma.PlayersName";
    ImageView p2btn,p3btn,p4btn,playbtn;
    EditText p1name,p2name,p3name,p4name;
    int noOfPlayer=2;
    ArrayList<String> playername= new ArrayList<>();
    int temp;
    public static int[] p3cond;   //when three players are play



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_and_play);
        //remove battery
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        p2btn = findViewById(R.id.p2btn);
        p3btn = findViewById(R.id.p3btn);
        p4btn = findViewById(R.id.p4btn);
        playbtn= findViewById(R.id.playbtn);
        p3cond= new int[]{0,1,2};



        replaceFragment(new Player2());
        p2btn.setClickable(false);


        p2btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noOfPlayer=2;

                replaceFragment(new Player2());

            }
        });

        p3btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noOfPlayer=3;
                replaceFragment(new Player3());
            }
        });

        p4btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noOfPlayer=4;
                replaceFragment(new Player4());

            }
        });

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playername.clear();
                switch (noOfPlayer){
                    case 2:
                        p1name = findViewById(R.id.p2player1name);
                        p2name = findViewById(R.id.p2player2name);

                        playername.add(p1name.getText().toString());
                        playername.add(p2name.getText().toString());

                        break;

                    case 3:
                        p1name = findViewById(R.id.p3player1name);
                        p2name = findViewById(R.id.p3player2name);
                        p3name = findViewById(R.id.p3player3name);

                        playername.add(p1name.getText().toString());
                        playername.add(p2name.getText().toString());
                        playername.add(p3name.getText().toString());


                        break;

                    case 4:
                        p1name = findViewById(R.id.p4player1name);
                        p2name = findViewById(R.id.p4player2name);
                        p3name = findViewById(R.id.p4player3name);
                        p4name = findViewById(R.id.p4player4name);

                        playername.add(p1name.getText().toString());
                        playername.add(p2name.getText().toString());
                        playername.add(p3name.getText().toString());
                        playername.add(p4name.getText().toString());

                        break;
                }


                for(int i=0;i<playername.size();i++){
                    if(Objects.equals(playername.get(i), "")){
                        playername.set(i,"player"+(i+1));
                    }
                }


                Intent intent = new Intent(PassAndPlay.this,MainActivity.class);
               intent.putStringArrayListExtra(MSG,playername);
                intent.putExtra("noOfPlayer",playername.size());
                startActivity(intent);

            }
        });


    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

    }

    public void p3OnTap(View v) {
        ImageView chGotiView = (ImageView) v;



        int tag = Integer.parseInt(chGotiView.getTag().toString());
        int row = tag / 10 - 1;
        int column = tag % 10 - 1;

        if(p3cond[row]==column)
            return;


        temp =p3cond[row];
        p3cond[row] = column;


        chGotiView.setBackgroundResource(R.drawable.background4);

        String name = "choosegoti"+((row+1)*10+temp+1);
        int Id = getResources().getIdentifier(name,"id",getPackageName());
        ImageView tempchGotiView= findViewById(Id);
        tempchGotiView.setBackgroundResource(0);



        int i = 0;
        while (i < 3) {
            if (i == row) {
                name = "selected" + (i + 1);
                Id = getResources().getIdentifier(name, "id", getPackageName());
                tempchGotiView = findViewById(Id);
                name = "player" + (p3cond[i] + 1) + "goti";
                Id = getResources().getIdentifier(name, "drawable", getPackageName());
                tempchGotiView.setImageResource(Id);


            } else {
                if (p3cond[i] == column) {
                    p3cond[i] = temp;
                    name = "choosegoti" + ((i + 1) * 10 + column + 1);
                    Id = getResources().getIdentifier(name, "id", getPackageName());
                    tempchGotiView = findViewById(Id);
                    tempchGotiView.setBackgroundResource(0);
                    name = "choosegoti" + ((i + 1) * 10 + p3cond[i] + 1);
                    Id = getResources().getIdentifier(name, "id", getPackageName());
                    tempchGotiView = findViewById(Id);
                    tempchGotiView.setBackgroundResource(R.drawable.background4);


                    name = "selected" + (i + 1);
                    Id = getResources().getIdentifier(name, "id", getPackageName());
                    tempchGotiView = findViewById(Id);
                    name = "player" + (p3cond[i] + 1) + "goti";
                    Id = getResources().getIdentifier(name, "drawable", getPackageName());
                    tempchGotiView.setImageResource(Id);
                }
            }
            i++;
        }
    }
    public void show(int a,boolean b){


        String name = "choosegoti"+a;
        int Id = getResources().getIdentifier(name,"id",getPackageName());
        ImageView tempchGotiView= findViewById(Id);
        tempchGotiView.setBackgroundResource(0);




    }

}
