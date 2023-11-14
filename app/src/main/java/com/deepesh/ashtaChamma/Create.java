package com.deepesh.ashtaChamma;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class Create extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_create, container, false);


        ImageView createRoom= view.findViewById(R.id.createRoom);
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CreateRoom.class);
                intent.putExtra("permission",true);
                startActivity(intent);
            }
        });






        return view;
    }
}