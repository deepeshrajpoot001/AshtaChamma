package com.deepesh.ashtaChamma;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class Player2 extends Fragment {

    View view;

    ImageView cond1,cond2,player1logo,player2logo;
    EditText player1name;
    EditText player2name;
    public static int p2cond;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=  inflater.inflate(R.layout.fragment_player2, container, false);

        cond1 = view.findViewById(R.id.cond1);
        cond2 = view.findViewById(R.id.cond2);
        player1logo = view.findViewById(R.id.player1logo);
        player2logo = view.findViewById(R.id.player2logo);
        player1name= view.findViewById(R.id.p2player1name);
        player2name= view.findViewById(R.id.p2player2name);

        p2cond=1;
        cond1.setBackgroundResource(R.drawable.background4);
        cond1.setClickable(false);
        cond1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player1logo.setImageResource(R.drawable.player1goti);
                player2logo.setImageResource(R.drawable.player3goti);
                p2cond=1;
                cond1.setBackgroundResource(R.drawable.background4);
                cond2.setBackgroundResource(0);
                cond1.setClickable(false);
                cond2.setClickable(true);

            }
        });

        cond2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player1logo.setImageResource(R.drawable.player2goti);
                player2logo.setImageResource(R.drawable.player4goti);
                cond1.setBackgroundResource(0);
                cond2.setBackgroundResource(R.drawable.background4);
                p2cond=2;
                cond2.setClickable(false);
                cond1.setClickable(true);


            }
        });

        return view;




    }
}