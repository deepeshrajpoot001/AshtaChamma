package com.deepesh.ashtaChamma;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class CreateRoom extends AppCompatActivity {


    public static String roomCode;

    ImageView myImageView;
    TextView myNameView;
    TextView codeView;
    Button nowPlay;
    public static int myId;

    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    DatabaseReference adminRef,roomRef,otherPlayerRef;
    Uri myPhoto;



    ImageView friendImageView;
    String friendId;
    TextView friendNameView;
    ImageView share;
    ImageView whatsapp;
    int friendViewId;
   ArrayList<String> onlinePlayersName = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        myImageView = findViewById(R.id.oplayer1Image);
        myNameView = findViewById(R.id.oplayer1Name);
        codeView = findViewById(R.id.generatedCode);
        nowPlay = findViewById(R.id.nowPlay);
        whatsapp = findViewById(R.id.whatsapp);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("whatsapp://send?text=I want to play Ashta Chamma with you:\n Room Code: \"" + CreateRoom.roomCode + "\" \n Start Game > Play with Friends > Join > Enter Room Code.\n Please install: this \n Belive me this is awesome game:")));
            }
        });
        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.TEXT", "This is my text to send.");
                sendIntent.setType("text/plain");
                CreateRoom.this.startActivity(Intent.createChooser(sendIntent, (CharSequence) null));
            }
        });






        Intent intent = getIntent();
        boolean permission = intent.getBooleanExtra("permission", false);

        if(permission) {

            nowPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CreateRoom.this, MainActivity.class);


                    OnlinePlayer flag = new OnlinePlayer(-1, "room is full", "startGame");
                    FirebaseDatabase.getInstance().getReference("Room").child(roomCode).child("player-1").setValue(flag);

                    Player2.p2cond = 1;
                    PassAndPlay.p3cond = new int[]{0, 1, 2};
                    startActivity(intent);
                    finish();

                }
            });

            nowPlay.setClickable(false);

            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                myPhoto = acct.getPhotoUrl();

            }


           //FirebaseDatabase.getInstance().getReference("Admin").child("RoomCode").setValue("1000");


            database = FirebaseDatabase.getInstance();
            adminRef = database.getReference("Admin");

            adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot s : snapshot.getChildren()) {
                            roomCode = (String) s.getValue();
                            codeView.setText(roomCode);

                            roomRef = database.getReference("Room").child(roomCode);

                            myId = 1;
                            if (myPhoto != null) {
                                OnlinePlayer player1 = new OnlinePlayer(myId, Front2.myName, myPhoto.toString());
                                roomRef.child("player1").setValue(player1);
                                roomRef.child("player1").setValue(new OnlinePlayer(CreateRoom.myId, Front2.myName, CreateRoom.this.myPhoto.toString()));
                            } else {
                                OnlinePlayer player1 = new OnlinePlayer(myId, Front2.myName, "null");
                                roomRef.child("player1").setValue(player1);

                            }


                            Long intRoomCode = Long.parseLong(roomCode);
                            intRoomCode++;
                            String updateRoomCode = String.valueOf(intRoomCode);
                            adminRef.child("RoomCode").setValue(updateRoomCode);
                        }

                        updateOtherPlayer();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(CreateRoom.this, "currently unavailable", Toast.LENGTH_SHORT).show();
                    finish();

                }

            });

        }

if(!permission) {
    codeView.setText(roomCode);
    updateOtherPlayer();

}
    }

    public void updateOtherPlayer(){

        FirebaseDatabase.getInstance().getReference("Room").child(roomCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int noOfBranchInRoom = (int) snapshot.getChildrenCount();

                    int loop = 2;
                    for (DataSnapshot s : snapshot.getChildren()) {
                        OnlinePlayer friend = s.getValue(OnlinePlayer.class);

                        if (friend.getId() == -1 && friend.getPhotoURL().equals("startGame")) {

                            Intent intent = new Intent(CreateRoom.this, MainActivity.class);
                            OnlinePlayer flag = new OnlinePlayer(-1, "room is full", "Game began");
                            FirebaseDatabase.getInstance().getReference("Room").child(roomCode).child("player-1").setValue(flag);
                            Player2.p2cond = 1;
                            PassAndPlay.p3cond = new int[]{0, 1, 2};

                            intent.putExtra("noOfPlayer", noOfBranchInRoom - 1);
                            startActivity(intent);
                            finish();
                        }

                        if (friend.getId() != -1) {

                            onlinePlayersName.add(friend.getName());
                            if (myId == friend.getId()) {
                                myNameView.setText(Front2.myName);
                                if (!Objects.equals(friend.photoURL, "null")) {
                                    Glide.with(CreateRoom.this).load(String.valueOf(Uri.parse(friend.photoURL))).into(myImageView);
                                }
                            } else {
                                friendId = "oplayer" + loop + "Name";
                                friendViewId = getResources().getIdentifier(friendId, "id", getPackageName());
                                friendNameView = findViewById(friendViewId);
                                friend.getName().replace(" ", "\n");
                                friendNameView.setText(friend.getName());

                                friendId = "oplayer" + loop + "Image";
                                friendViewId = getResources().getIdentifier(friendId, "id", getPackageName());
                                friendImageView = findViewById(friendViewId);
                                if (!Objects.equals(friend.photoURL, "null")) {
                                    Glide.with(CreateRoom.this).load(String.valueOf(Uri.parse(friend.photoURL))).into(friendImageView);
                                }
                                loop++;
                            }
                        } else {
                            return;
                        }
                    }

                    if(loop>2) {
                        nowPlay.setClickable(true);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }



}