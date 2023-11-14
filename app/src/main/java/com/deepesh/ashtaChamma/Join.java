package com.deepesh.ashtaChamma;


import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class Join extends Fragment {

    View view;
    EditText enterCodeView;
    FirebaseDatabase database;
    DatabaseReference ref,roomRef;
    ImageView joinRoom;
    String enteredRoomCode;
    String availableRoomCode;

    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    Uri myPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_join, container, false);
        enterCodeView = view.findViewById(R.id.enteredCode);
        joinRoom = view.findViewById(R.id.joinRoom);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireActivity());
        if (acct != null) {
            myPhoto = acct.getPhotoUrl();

        }


        FirebaseApp.initializeApp(requireActivity());
        database= FirebaseDatabase.getInstance();

joinRoom.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        enteredRoomCode=enterCodeView.getText().toString();
        ref = database.getReference("Room");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for(DataSnapshot s:snapshot.getChildren()){
                       availableRoomCode = s.getKey();
                       if(enteredRoomCode.equals(availableRoomCode)) {
                           int noOfFriendJoin = (int) s.getChildrenCount();
                           Log.d("qwert", "no of Friend JOin" + noOfFriendJoin);
                           Log.d("qwert", "key" + availableRoomCode);
                           noOfFriendJoin++;
                           if (noOfFriendJoin > 4) {
                               Toast.makeText(getActivity(), "Room is full", Toast.LENGTH_SHORT).show();
                               break;
                           }
                                CreateRoom.myId= noOfFriendJoin;
                                CreateRoom.roomCode= enteredRoomCode;
                           if(myPhoto!=null) {
                               OnlinePlayer player = new OnlinePlayer(noOfFriendJoin, Front2.myName, myPhoto.toString());
                               ref.child(enteredRoomCode).child("player" + noOfFriendJoin).setValue(player);
                           }
                           else{  OnlinePlayer player = new OnlinePlayer(noOfFriendJoin, Front2.myName, "null");
                               ref.child(enteredRoomCode).child("player" + noOfFriendJoin).setValue(player);


                           }
                               Intent intent = new Intent(getActivity(),CreateRoom.class);
                               intent.putExtra("permission",false);
                               startActivity(intent);
                           }
                       }

                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
});
        return view;
    }
}