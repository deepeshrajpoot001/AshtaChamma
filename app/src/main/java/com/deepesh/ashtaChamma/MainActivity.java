package com.deepesh.ashtaChamma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;





class Block{
    int centerX;  //this is not center because this is 39 UNIT away form center
    int centerY;
    ArrayList<Integer> gotiContain = new ArrayList<>();

    public Block(int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        gotiContain = new ArrayList<>();
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }


}
class Player{
    String playerName;
    int countOfWay;
    int id;
    int[] playerPath;
    int[] gotiScore=new int[4];
    //how many coordinate goti came from player home

    public Player(int[] playerPath, int[] goti) {
        this.playerPath = playerPath;
        this.gotiScore = goti;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int[] getPlayerPath() {
        return playerPath;
    }

    public int[] getGotiScore() {
        return gotiScore;
    }

    public void setGotiScore(int[] goti) {
        this.gotiScore = goti;
    }
}

public class MainActivity extends AppCompatActivity {

    boolean adminPermission = false;

    Block[] block = new Block[25];
    Player[] player=new Player[4];
    ImageView touchView,gotiView,gotiButton1,gotiButton2,highView;
    ProgressBar progressBar;

    MediaPlayer mediaPlayer,mediaPlayer1;
    ArrayList<String> playersName=new ArrayList<>();
    int[] wayOfMovingPlayer;
    int countOfWay=0,noOfPlayer,turn,currentPlayer,currentKodiPoint,deepesh,animIni,tempScore=0;

    boolean GotiOnClickFlag= false;
    boolean throwGotiFlag= true;
    int Id;  // for generate variable id
    String name,currentKodiPointStr = "Score-";

    // x and y are centre of board , s is the length of side of box;
    int x,y,s;

    CountDownTimer timer;

    boolean vsComputerStartFlag = true;
    boolean wonFlag = true;
    boolean gameStart = false;
    boolean flagForOnResume=false;



    public void tap(View view) {
        gotiView = (ImageView) view;
        tap2(Integer.parseInt(gotiView.getTag().toString()));
    }

    public void tap2(int tag) {
        int i;
        int i2;
        int i3 = tag;
        int clickOnGotiOfPlayer = (i3 / 10) - 1;
        int clickOnGotiNo = (i3 % 10) - 1;
        if ((Front2.mode.equals("vsComputer") && countOfWay == 1) || adminPermission || Front2.mode.equals("offline") || (Front2.mode.equals("online") && this.wayOfMovingPlayer[CreateRoom.myId - 1] == clickOnGotiOfPlayer && CreateRoom.myId == this.turn)) {
            if (this.throwGotiFlag && !this.GotiOnClickFlag && clickOnGotiOfPlayer == (i2 = this.currentPlayer) && (this.player[i2].gotiScore[0] == 0 || this.player[this.currentPlayer].gotiScore[1] == 0 || this.player[this.currentPlayer].gotiScore[2] == 0 || this.player[this.currentPlayer].gotiScore[3] == 0)) {
                throwKodi();
            } else if (this.GotiOnClickFlag && clickOnGotiOfPlayer == this.currentPlayer) {
                if (Front2.mode.equals("online") && CreateRoom.myId == this.turn) {
                    FirebaseDatabase.getInstance().getReference("Room").child(CreateRoom.roomCode).child("player5").setValue(new OnlinePlayer(this.countOfWay + 1, "tap2", String.valueOf(tag)));
                }
                startProgress(false);
                int[] iArr = this.player[this.currentPlayer].gotiScore;
                iArr[clickOnGotiNo] = iArr[clickOnGotiNo] + this.currentKodiPoint;
                if (this.player[this.currentPlayer].gotiScore[clickOnGotiNo] < 25) {
                    int[] iArr2 = this.player[this.currentPlayer].gotiScore;
                    iArr2[clickOnGotiNo] = iArr2[clickOnGotiNo] - this.currentKodiPoint;
                    this.animIni = this.player[this.currentPlayer].gotiScore[clickOnGotiNo];
                    int currentBlock = this.player[this.currentPlayer].playerPath[this.player[this.currentPlayer].gotiScore[clickOnGotiNo]];
                    int i4 = 0;
                    while (true) {
                        if (i4 >= this.block[currentBlock].gotiContain.size()) {
                            break;
                        } else if (i3 == this.block[currentBlock].gotiContain.get(i4).intValue()) {
                            this.block[currentBlock].gotiContain.remove(i4);
                            break;
                        } else {
                            i4++;
                        }
                    }
                    int[] iArr3 = this.player[this.currentPlayer].gotiScore;
                    iArr3[clickOnGotiNo] = iArr3[clickOnGotiNo] + this.currentKodiPoint;
                    blinkGoti(false);
                    int target = this.player[this.currentPlayer].playerPath[this.player[this.currentPlayer].gotiScore[clickOnGotiNo]];
                    if (!(target == 2 || target == 10 || target == 12 || target == 14 || target == 22 || this.block[target].gotiContain.size() != 1 || (this.block[target].gotiContain.get(0).intValue() / 10) - 1 == this.currentPlayer)) {
                        MediaPlayer create = MediaPlayer.create(this, R.raw.kill);
                        this.mediaPlayer = create;
                        create.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                mp.reset();
                                mp.release();
                                MainActivity.this.mediaPlayer = null;
                            }
                        });
                        this.mediaPlayer.start();
                        int temptag = this.block[target].gotiContain.get(0).intValue();
                        this.block[target].gotiContain.remove(0);
                        this.player[(temptag / 10) - 1].gotiScore[(temptag % 10) - 1] = 0;
                        gotiMoverOnFinish(temptag, this.player[(temptag / 10) - 1].playerPath[0]);
                        int i5 = this.countOfWay - 1;
                        this.countOfWay = i5;
                        if (i5 == -1) {
                            this.countOfWay = this.noOfPlayer - 1;
                        }
                        this.currentPlayer = this.wayOfMovingPlayer[this.countOfWay];
                    }
                    gotiMover(i3, target);
                    int i6 = 0;
                    while (true) {
                        if (i6 >= 4) {
                            i = 1;
                            break;
                        } else if (this.player[this.currentPlayer].gotiScore[i6] < 24) {
                            i = 0;
                            break;
                        } else {
                            i6++;
                        }
                    }
                    if (i != 0) {
                        Intent intent = new Intent(this, Won.class);
                        intent.putExtra("player", this.countOfWay);
                        startActivity(intent);
                        finish();
                    }
                    this.GotiOnClickFlag = false;
                    int i7 = this.countOfWay + 1;
                    this.countOfWay = i7;
                    if (i7 == this.noOfPlayer) {
                        this.countOfWay = 0;
                    }
                    this.currentPlayer = this.wayOfMovingPlayer[this.countOfWay];
                    if (Front2.mode.equals("online") && CreateRoom.myId == this.turn) {
                        OnlinePlayer admin = new OnlinePlayer(this.countOfWay + 1, "changePlayer", "null");
                        FirebaseDatabase.getInstance().getReference("Room").child(CreateRoom.roomCode).child("player0").setValue(admin);
                        FirebaseDatabase.getInstance().getReference("Room").child(CreateRoom.roomCode).child("player5").setValue(admin);
                    }
                    if (!Front2.mode.equals("vsComputer") || this.countOfWay != 0) {
                        blinkHome(true);
                        this.name = "player" + (this.currentPlayer + 1) + "home";
                        int identifier = getResources().getIdentifier(this.name, "id", getPackageName());
                        this.Id = identifier;
                        ImageView imageView = (ImageView) findViewById(identifier);
                        this.touchView = imageView;
                        imageView.setClickable(true);
                        startProgress(true);
                        gotiButtonMeth(true);
                        this.touchView.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                if (Front2.mode.equals("vsComputer") || Front2.mode.equals("offline") || (Front2.mode.equals("online") && CreateRoom.myId == MainActivity.this.turn)) {
                                    MainActivity.this.throwKodi();
                                }
                            }
                        });
                        return;
                    }
                    new CountDownTimer(((long) (this.currentKodiPoint + 2)) * 333, 333) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            MainActivity.this.blinkHome(true);
                            MainActivity.this.name = "player" + (MainActivity.this.currentPlayer + 1) + "home";
                            MainActivity mainActivity = MainActivity.this;
                            mainActivity.Id = mainActivity.getResources().getIdentifier(MainActivity.this.name, "id", MainActivity.this.getPackageName());
                            MainActivity mainActivity2 = MainActivity.this;
                            mainActivity2.touchView = (ImageView) mainActivity2.findViewById(mainActivity2.Id);
                            MainActivity.this.touchView.setClickable(true);
                            MainActivity.this.startProgress(true);
                            MainActivity.this.gotiButtonMeth(true);
                            MainActivity.this.throwKodi();
                        }
                    }.start();
                    return;
                }
                int[] iArr4 = this.player[this.currentPlayer].gotiScore;
                iArr4[clickOnGotiNo] = iArr4[clickOnGotiNo] - this.currentKodiPoint;
            }
        }
    }

    public void vsComputer() {
        throwKodi();
        new CountDownTimer(1000, 200) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                MainActivity.this.tap2(31);
            }
        }.start();
    }

    public void onResume() {
        super.onResume();
        blinkHome(true);
        startProgress(true);
        this.name = "player" + (this.currentPlayer + 1) + "home";
        int identifier = getResources().getIdentifier(this.name, "id", getPackageName());
        this.Id = identifier;
        ImageView imageView = (ImageView) findViewById(identifier);
        this.touchView = imageView;
        imageView.setClickable(true);
        gotiButtonMeth(true);
        if (!Front2.mode.equals("vsComputer") || this.countOfWay != 0) {
            this.touchView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if ((Front2.mode.equals("vsComputer") && MainActivity.this.countOfWay == 0) || Front2.mode.equals("offline") || (Front2.mode.equals("online") && CreateRoom.myId == MainActivity.this.turn)) {
                        MainActivity.this.throwKodi();
                    }
                }
            });
            if (Front2.mode.equals("online")) {
                FirebaseDatabase.getInstance().getReference("Room").child(CreateRoom.roomCode).child("player0").addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            OnlinePlayer admin = (OnlinePlayer) snapshot.getValue(OnlinePlayer.class);
                            MainActivity.this.turn = admin.getId();
                            if (MainActivity.this.turn != CreateRoom.myId && admin.getName().equals("throwKodi")) {
                                MainActivity.this.currentKodiPointStr = admin.getPhotoURL();
                                MainActivity.this.throwKodi();
                            }
                        }
                    }

                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
            if (Front2.mode.equals("online")) {
                FirebaseDatabase.getInstance().getReference("Room").child(CreateRoom.roomCode).child("player5").addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            OnlinePlayer admin = (OnlinePlayer) snapshot.getValue(OnlinePlayer.class);
                            MainActivity.this.turn = admin.getId();
                            if (MainActivity.this.turn != CreateRoom.myId && admin.getName().equals("tap2")) {
                                adminPermission = true;
                                MainActivity.this.takeCurrentKodiScore();
                                MainActivity.this.tap2(Integer.parseInt(admin.getPhotoURL()));
                                adminPermission = false;
                            }
                        }
                    }

                    public void onCancelled(DatabaseError error) {
                    }
                });
                return;
            }
            return;
        }
        throwKodi();
    }

    public void takeCurrentKodiScore() {
        if (Front2.mode.equals("online")) {
            FirebaseDatabase.getInstance().getReference("Room").child(CreateRoom.roomCode).child("player0").addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        OnlinePlayer admin = (OnlinePlayer) snapshot.getValue(OnlinePlayer.class);
                        MainActivity.this.turn = admin.getId();
                        if (MainActivity.this.turn != CreateRoom.myId && admin.getName().equals("throwKodi")) {
                            MainActivity.this.currentKodiPointStr = admin.getPhotoURL();
                            for (int i = 1; i <= 5; i++) {
                                if (Integer.parseInt(String.valueOf(MainActivity.this.currentKodiPointStr.charAt(i + 5))) == 1) {
                                    tempScore++;
                                }
                            }
                            switch (tempScore) {
                                case 0:
                                  currentKodiPoint = 5;
                                    return;
                                case 1:
                                  currentKodiPoint = 1;
                                    return;
                                case 2:
                                   currentKodiPoint = 2;
                                    return;
                                case 3:
                                 currentKodiPoint = 3;
                                    return;
                                case 4:
                                 currentKodiPoint = 8;
                                    return;
                                case 5:
                                  currentKodiPoint = 10;
                                    return;
                                default:
                                    return;
                            }
                        }
                    }
                }

                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        if (Front2.mode.equals("online")) {
            FirebaseDatabase.getInstance().getReference("Room").child(CreateRoom.roomCode).child("player0").setValue(new OnlinePlayer(1, "deepesh", "null"));
        }
        this.noOfPlayer = getIntent().getIntExtra("noOfPlayer", 2);
        getWindow().setFlags(1024, 1024);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightOfMobile = displayMetrics.heightPixels;
        int widthOfMobile = displayMetrics.widthPixels;
        this.x = widthOfMobile / 2;
        this.y = heightOfMobile / 2;
        this.s = (widthOfMobile - 20) / 5;
        ImageView imageView = (ImageView) findViewById(R.id.board);
        this.touchView = imageView;
        imageView.setY((float) ((this.y - this.x) + 10));
        this.touchView.setX(10.0f);
        ViewGroup.LayoutParams params = this.touchView.getLayoutParams();
        params.height = widthOfMobile - 20;
        params.width = widthOfMobile - 20;
        this.touchView.setLayoutParams(params);
        ImageView imageView2 = (ImageView) findViewById(R.id.player1home);
        this.touchView = imageView2;
        imageView2.setY((float) ((this.y - ((this.s * 5) / 2)) - 13));
        ViewGroup.LayoutParams params2 = this.touchView.getLayoutParams();
        params2.height = (this.s * 9) / 8;
        params2.width = (this.s * 9) / 8;
        this.touchView.setLayoutParams(params2);
        ImageView imageView3 = (ImageView) findViewById(R.id.player2home);
        this.touchView = imageView3;
        imageView3.setX((float) (this.x - ((this.s * 5) / 2)));
        ViewGroup.LayoutParams params3 = this.touchView.getLayoutParams();
        params3.height = (this.s * 9) / 8;
        params3.width = (this.s * 9) / 8;
        this.touchView.setLayoutParams(params3);
        ImageView imageView4 = (ImageView) findViewById(R.id.player3home);
        this.touchView = imageView4;
        imageView4.setY((float) ((-this.y) + ((this.s * 5) / 2) + 13));
        ViewGroup.LayoutParams params4 = this.touchView.getLayoutParams();
        params4.height = (this.s * 9) / 8;
        params4.width = (this.s * 9) / 8;
        this.touchView.setLayoutParams(params4);
        ImageView imageView5 = (ImageView) findViewById(R.id.player4home);
        this.touchView = imageView5;
        imageView5.setX((float) ((-this.x) + ((this.s * 5) / 2)));
        ViewGroup.LayoutParams params5 = this.touchView.getLayoutParams();
        params5.height = (this.s * 9) / 8;
        params5.width = (this.s * 9) / 8;
        this.touchView.setLayoutParams(params5);
        int i = 3;
        if (Front2.mode.equals("online")) {
            ImageView imageView6 = (ImageView) findViewById(R.id.Mplayer1Image);
            this.touchView = imageView6;
            imageView6.setY((float) (this.s / 2));
            ViewGroup.LayoutParams params6 = this.touchView.getLayoutParams();
            params6.height = (this.s * 3) / 4;
            params6.width = (this.s * 3) / 4;
            this.touchView.setLayoutParams(params6);
            ImageView imageView7 = (ImageView) findViewById(R.id.Mplayer2Image);
            this.touchView = imageView7;
            imageView7.setY((float) (this.s / 2));
            ViewGroup.LayoutParams params7 = this.touchView.getLayoutParams();
            params7.height = (this.s * 3) / 4;
            params7.width = (this.s * 3) / 4;
            this.touchView.setLayoutParams(params7);
            ImageView imageView8 = (ImageView) findViewById(R.id.Mplayer3Image);
            this.touchView = imageView8;
            imageView8.setY((float) ((-this.s) / 2));
            ViewGroup.LayoutParams params8 = this.touchView.getLayoutParams();
            params8.height = (this.s * 3) / 4;
            params8.width = (this.s * 3) / 4;
            this.touchView.setLayoutParams(params8);
            ImageView imageView9 = (ImageView) findViewById(R.id.Mplayer4Image);
            this.touchView = imageView9;
            imageView9.setY((float) ((-this.s) / 2));
            ViewGroup.LayoutParams params9 = this.touchView.getLayoutParams();
            params9.height = (this.s * 3) / 4;
            params9.width = (this.s * 3) / 4;
            this.touchView.setLayoutParams(params9);
            for (int i2 = 1; i2 <= 4; i2++) {
                this.name = "Mplayer" + i2 + "Name";
                int identifier = getResources().getIdentifier(this.name, "id", getPackageName());
                this.Id = identifier;
                ((TextView) findViewById(identifier)).setText("ram");
            }
        } else {
            for (int i3 = 1; i3 <= 4; i3++) {
                this.name = "Mplayer" + i3 + "Image";
                int identifier2 = getResources().getIdentifier(this.name, "id", getPackageName());
                this.Id = identifier2;
                ImageView imageView10 = (ImageView) findViewById(identifier2);
                this.touchView = imageView10;
                imageView10.setAlpha(0.0f);
                this.name = "Mplayer" + i3 + "Name";
                int identifier3 = getResources().getIdentifier(this.name, "id", getPackageName());
                this.Id = identifier3;
                ((TextView) findViewById(identifier3)).setAlpha(0.0f);
            }
        }
        int k = 0;
        int i4 = -2;
        while (i4 < i) {
            int j = -2;
            while (j < i) {
                Block[] blockArr = this.block;
                int i5 = this.x;
                int i6 = this.s;
                blockArr[k] = new Block(i5 + (j * i6), this.y + (i6 * i4));
                k++;
                j++;
                i = 3;
            }
            i4++;
            i = 3;
        }
        int i7 = this.noOfPlayer;
        if (i7 != 2) {
            if (i7 == 3) {
                this.wayOfMovingPlayer = PassAndPlay.p3cond;

                for (int i8 = 0; i8 < 3; i8++) {
                    switch (PassAndPlay.p3cond[i8]) {
                        case 0:
                            this.player[PassAndPlay.p3cond[i8]] = new Player(new int[]{2, 1, 0, 5, 10, 15, 20, 21, 22, 23, 24, 19, 14, 9, 4, 3, 8, 13, 18, 17, 16, 11, 6, 7, 12}, new int[]{0, 0, 0, 0});
                            gotiMover(11, 2);
                            gotiMover(12, 2);
                            gotiMover(13, 2);
                            gotiMover(14, 2);
                            break;
                        case 1:
                            this.player[PassAndPlay.p3cond[i8]] = new Player(new int[]{10, 15, 20, 21, 22, 23, 24, 19, 14, 9, 4, 3, 2, 1, 0, 5, 6, 7, 8, 13, 18, 17, 16, 11, 12}, new int[]{0, 0, 0, 0});
                            gotiMover(21, 10);
                            gotiMover(22, 10);
                            gotiMover(23, 10);
                            gotiMover(24, 10);
                            break;
                        case 2:
                            this.player[PassAndPlay.p3cond[i8]] = new Player(new int[]{22, 23, 24, 19, 14, 9, 4, 3, 2, 1, 0, 5, 10, 15, 20, 21, 16, 11, 6, 7, 8, 13, 18, 17, 12}, new int[]{0, 0, 0, 0});
                            gotiMover(31, 22);
                            gotiMover(32, 22);
                            gotiMover(33, 22);
                            gotiMover(34, 22);
                            break;
                        case 3:
                            this.player[PassAndPlay.p3cond[i8]] = new Player(new int[]{14, 9, 4, 3, 2, 1, 0, 5, 10, 15, 20, 21, 22, 23, 24, 19, 18, 17, 16, 11, 6, 7, 8, 13, 12}, new int[]{0, 0, 0, 0});
                            gotiMover(41, 14);
                            gotiMover(42, 14);
                            gotiMover(43, 14);
                            gotiMover(44, 14);
                            break;
                    }
                }
                for (int i10 = 0; i10 < 4; i10++) {
                    boolean gayabCond = true;

                    for(int j2=0;j2<3;j2++){
                        if (PassAndPlay.p3cond[j2] == i10) {
                            gayabCond = false;
                            break;
                        }
                    }
                    if (gayabCond) {
                        gotiGayab(((i10 + 1) * 10) + 1, true);
                        gotiGayab(((i10 + 1) * 10) + 2, true);
                        gotiGayab(((i10 + 1) * 10) + 3, true);
                        gotiGayab(((i10 + 1) * 10) + 4, true);
                        nameGayab(i10);
                        nameGayab(i10);
                    }
                }

            } else {
                this.player[0] = new Player(new int[]{2, 1, 0, 5, 10, 15, 20, 21, 22, 23, 24, 19, 14, 9, 4, 3, 8, 13, 18, 17, 16, 11, 6, 7, 12}, new int[]{0, 0, 0, 0});
                this.player[1] = new Player(new int[]{10, 15, 20, 21, 22, 23, 24, 19, 14, 9, 4, 3, 2, 1, 0, 5, 6, 7, 8, 13, 18, 17, 16, 11, 12}, new int[]{0, 0, 0, 0});
                this.player[2] = new Player(new int[]{22, 23, 24, 19, 14, 9, 4, 3, 2, 1, 0, 5, 10, 15, 20, 21, 16, 11, 6, 7, 8, 13, 18, 17, 12}, new int[]{0, 0, 0, 0});
                this.player[3] = new Player(new int[]{14, 9, 4, 3, 2, 1, 0, 5, 10, 15, 20, 21, 22, 23, 24, 19, 18, 17, 16, 11, 6, 7, 8, 13, 12}, new int[]{0, 0, 0, 0});
                gotiMover(11, 2);
                gotiMover(12, 2);
                gotiMover(13, 2);
                gotiMover(14, 2);
                gotiMover(21, 10);
                gotiMover(22, 10);
                gotiMover(23, 10);
                gotiMover(24, 10);
                gotiMover(41, 14);
                gotiMover(42, 14);
                gotiMover(43, 14);
                gotiMover(44, 14);
                gotiMover(31, 22);
                gotiMover(32, 22);
                gotiMover(33, 22);
                gotiMover(34, 22);
                this.wayOfMovingPlayer = new int[]{0, 1, 2, 3};
            }
        } else if (Player2.p2cond == 1) {
            this.player[0] = new Player(new int[]{2, 1, 0, 5, 10, 15, 20, 21, 22, 23, 24, 19, 14, 9, 4, 3, 8, 13, 18, 17, 16, 11, 6, 7, 12}, new int[]{0, 0, 0, 0});
            this.player[2] = new Player(new int[]{22, 23, 24, 19, 14, 9, 4, 3, 2, 1, 0, 5, 10, 15, 20, 21, 16, 11, 6, 7, 8, 13, 18, 17, 12}, new int[]{0, 0, 0, 0});
            gotiMover(11, 2);
            gotiMover(12, 2);
            gotiMover(13, 2);
            gotiMover(14, 2);
            gotiMover(31, 22);
            gotiMover(32, 22);
            gotiMover(33, 22);
            gotiMover(34, 22);
            gotiGayab(21, true);
            gotiGayab(22, true);
            gotiGayab(23, true);
            gotiGayab(24, true);
            gotiGayab(41, true);
            gotiGayab(42, true);
            gotiGayab(43, true);
            gotiGayab(44, true);
            nameGayab(1);
            nameGayab(3);
            this.wayOfMovingPlayer = new int[]{0, 2};
        } else {
            this.player[1] = new Player(new int[]{10, 15, 20, 21, 22, 23, 24, 19, 14, 9, 4, 3, 2, 1, 0, 5, 6, 7, 8, 13, 18, 17, 16, 11, 12}, new int[]{0, 0, 0, 0});
            this.player[3] = new Player(new int[]{14, 9, 4, 3, 2, 1, 0, 5, 10, 15, 20, 21, 22, 23, 24, 19, 18, 17, 16, 11, 6, 7, 8, 13, 12}, new int[]{0, 0, 0, 0});
            gotiMover(21, 10);
            gotiMover(22, 10);
            gotiMover(23, 10);
            gotiMover(24, 10);
            gotiMover(41, 14);
            gotiMover(42, 14);
            gotiMover(43, 14);
            gotiMover(44, 14);
            gotiGayab(11, true);
            gotiGayab(12, true);
            gotiGayab(13, true);
            gotiGayab(14, true);
            gotiGayab(31, true);
            gotiGayab(32, true);
            gotiGayab(33, true);
            gotiGayab(34, true);
            nameGayab(0);
            nameGayab(2);
            this.wayOfMovingPlayer = new int[]{1, 3};
        }
        this.currentPlayer = this.wayOfMovingPlayer[this.countOfWay];
        if (Front2.mode.equals("online")) {
            FirebaseDatabase.getInstance().getReference("Room").child(CreateRoom.roomCode).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        int loop = -3;
                        for (DataSnapshot s : snapshot.getChildren()) {
                            OnlinePlayer friend = (OnlinePlayer) s.getValue(OnlinePlayer.class);
                            loop++;
                            if (loop >= 0) {
                                TextView friendNameView = (TextView) MainActivity.this.findViewById(MainActivity.this.getResources().getIdentifier("Mplayer" + (MainActivity.this.wayOfMovingPlayer[loop] + 1) + "Name", "id", MainActivity.this.getPackageName()));
                                String text = friend.getName();
                                if (text.length() > 16) {
                                    text = text.substring(0, 15);
                                }
                                friendNameView.setText("  " + text + "  ");
                                ImageView friendImageView = (ImageView) MainActivity.this.findViewById(MainActivity.this.getResources().getIdentifier("Mplayer" + (MainActivity.this.wayOfMovingPlayer[loop] + 1) + "Image", "id", MainActivity.this.getPackageName()));
                                if (!Objects.equals(friend.photoURL, "null")) {
                                    Glide.with((FragmentActivity) MainActivity.this).load(String.valueOf(Uri.parse(friend.photoURL))).into(friendImageView);
                                }
                            }
                        }
                    }
                }

                public void onCancelled(DatabaseError error) {
                }
            });
        }
        for (int i11 = 1; i11 <= this.noOfPlayer; i11++) {
            for (int j3 = 1; j3 <= 4; j3++) {
                this.name = "goti" + i11 + j3;
                int identifier4 = getResources().getIdentifier(this.name, "id", getPackageName());
                this.Id = identifier4;
                ImageView imageView11 = (ImageView) findViewById(identifier4);
                this.gotiView = imageView11;
                ViewGroup.LayoutParams params10 = imageView11.getLayoutParams();
                params10.height = this.s / 2;
                params10.width = this.s / 2;
                this.gotiView.setLayoutParams(params10);
            }
        }
        for (int i12 = 1; i12 <= 4; i12++) {
            this.name = "highlighter" + i12;
            int identifier5 = getResources().getIdentifier(this.name, "id", getPackageName());
            this.Id = identifier5;
            ImageView imageView12 = (ImageView) findViewById(identifier5);
            this.highView = imageView12;
            ViewGroup.LayoutParams params11 = imageView12.getLayoutParams();
            params11.height = this.s / 2;
            params11.width = this.s / 2;
            this.highView.setLayoutParams(params11);
            this.highView.setAlpha(0.0f);
        }
        for (int i13 = 1; i13 <= 5; i13++) {
            this.name = "imageView" + i13;
            int identifier6 = getResources().getIdentifier(this.name, "id", getPackageName());
            this.Id = identifier6;
            ImageView imageView13 = (ImageView) findViewById(identifier6);
            this.touchView = imageView13;
            ViewGroup.LayoutParams params12 = imageView13.getLayoutParams();
            params12.height = (this.s * 3) / 4;
            params12.width = (this.s * 3) / 4;
            this.touchView.setLayoutParams(params12);
        }
        ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progress_bar);
        this.progressBar = progressBar2;
        progressBar2.setAlpha(0.0f);
        this.progressBar.setX((float) ((this.x * 2) - ((this.s * 5) / 3)));
        this.progressBar.setY((float) (this.s / 4));
        this.progressBar.setMax(100);
        this.progressBar.setMin(0);
        this.gameStart = true;
    }

    public void startProgress(boolean cond) {
        if (!cond) {
            this.timer.cancel();
            return;
        }
        this.progressBar.setProgress(0);
        this.deepesh = 0;
        this.progressBar.setAlpha(1.0f);
        switch (this.currentPlayer) {
            case 0:
                this.progressBar.setX((float) ((this.x * 2) - ((this.s * 5) / 3)));
                this.progressBar.setY((float) (this.s / 4));
                break;
            case 1:
                this.progressBar.setX((float) this.s);
                this.progressBar.setY((float) (this.s / 4));
                break;
            case 2:
                this.progressBar.setX((float) this.s);
                this.progressBar.setY((float) ((this.y * 2) - ((this.s * 5) / 4)));
                break;
            case 3:
                this.progressBar.setX((float) ((this.x * 2) - ((this.s * 5) / 3)));
                this.progressBar.setY((float) ((this.y * 2) - ((this.s * 5) / 4)));
                break;
        }
        this.timer = new CountDownTimer(23000, 200) {
            public void onTick(long millisUntilFinished) {
                MainActivity.this.progressBar.setProgress(MainActivity.this.deepesh);
                MainActivity.this.deepesh++;
            }

            public void onFinish() {
                MainActivity.this.throwGotiFlag = false;
                MainActivity.this.blinkHome(false);
                MainActivity.this.touchView.setClickable(false);
                MainActivity.this.blinkGoti(false);
                MainActivity.this.GotiOnClickFlag = false;
                MainActivity.this.progressBar.setAlpha(0.0f);
                MainActivity.this.countOfWay++;
                if (MainActivity.this.countOfWay == MainActivity.this.noOfPlayer) {
                    MainActivity.this.countOfWay = 0;
                }
                MainActivity mainActivity = MainActivity.this;
                mainActivity.currentPlayer = mainActivity.wayOfMovingPlayer[MainActivity.this.countOfWay];
                if (Front2.mode.equals("online") && CreateRoom.myId == MainActivity.this.turn) {
                    FirebaseDatabase.getInstance().getReference("Room").child(CreateRoom.roomCode).child("player0").setValue(new OnlinePlayer(MainActivity.this.countOfWay + 1, "changePlayer", "null"));
                }
                if (!Front2.mode.equals("vsComputer") || MainActivity.this.countOfWay != 0) {
                    MainActivity.this.throwGotiFlag = true;
                    MainActivity.this.blinkHome(true);
                    MainActivity.this.name = "player" + (MainActivity.this.currentPlayer + 1) + "home";
                    MainActivity mainActivity2 = MainActivity.this;
                    mainActivity2.Id = mainActivity2.getResources().getIdentifier(MainActivity.this.name, "id", MainActivity.this.getPackageName());
                    MainActivity mainActivity3 = MainActivity.this;
                    mainActivity3.touchView = (ImageView) mainActivity3.findViewById(mainActivity3.Id);
                    MainActivity.this.touchView.setClickable(true);
                    MainActivity.this.startProgress(true);
                    MainActivity.this.gotiButtonMeth(true);
                    MainActivity.this.touchView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (Front2.mode.equals("vsComputer") || Front2.mode.equals("offline") || (Front2.mode.equals("online") && CreateRoom.myId == MainActivity.this.turn)) {
                                MainActivity.this.throwKodi();
                            }
                        }
                    });
                    return;
                }
                new CountDownTimer(((long) (MainActivity.this.currentKodiPoint + 2)) * 333, 333) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        MainActivity.this.throwGotiFlag = true;
                        MainActivity.this.blinkHome(true);
                        MainActivity.this.name = "player" + (MainActivity.this.currentPlayer + 1) + "home";
                        MainActivity.this.Id = MainActivity.this.getResources().getIdentifier(MainActivity.this.name, "id", MainActivity.this.getPackageName());
                        MainActivity.this.touchView = (ImageView) MainActivity.this.findViewById(MainActivity.this.Id);
                        MainActivity.this.touchView.setClickable(true);
                        MainActivity.this.startProgress(true);
                        MainActivity.this.gotiButtonMeth(true);
                        MainActivity.this.throwKodi();
                    }
                }.start();
            }
        }.start();
    }

    public void nameGayab(int i) {
        int i2 = i + 1;
        this.name = "Mplayer" + i2 + "Image";
        int identifier = getResources().getIdentifier(this.name, "id", getPackageName());
        this.Id = identifier;
        ImageView imageView = (ImageView) findViewById(identifier);
        this.touchView = imageView;
        imageView.setAlpha(0.0f);
        this.name = "Mplayer" + i2 + "Name";
        int identifier2 = getResources().getIdentifier(this.name, "id", getPackageName());
        this.Id = identifier2;
        ((TextView) findViewById(identifier2)).setAlpha(0.0f);
    }

    public void gotiGayab(int gotino, boolean con) {
        ImageView imageView = (ImageView) findViewById(getResources().getIdentifier("goti" + gotino, "id", getPackageName()));
        this.gotiView = imageView;
        if (con) {
            imageView.setAlpha(0.0f);
        } else {
            imageView.setAlpha(1.0f);
        }
    }

    public void gotiMover(int gotino, int target) {
        if (!gameStart) {
            gotiMoverOnFinish(gotino, target);
            return;
        }
        final int i = gotino;
        final int i2 = target;
        new CountDownTimer(((long) (this.currentKodiPoint - 1)) * 333, 333) {
            public void onTick(long millisUntilFinished) {
                MainActivity.this.animIni++;
                MainActivity.this.gotiMoverOnTick(i, MainActivity.this.player[(i / 10) - 1].playerPath[MainActivity.this.animIni]);
            }

            public void onFinish() {
                MainActivity.this.gotiMoverOnFinish(i, i2);
            }
        }.start();
    }

    public void gotiMoverOnTick(int gotino, int target) {
        MediaPlayer create = MediaPlayer.create(this, R.raw.move);
        this.mediaPlayer = create;
        create.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                MainActivity.this.mediaPlayer = null;
            }
        });
        this.mediaPlayer.start();
        this.block[target].gotiContain.add(Integer.valueOf(gotino));
        int i = gotino / 10;
        int i2 = gotino % 10;
        ImageView imageView = (ImageView) findViewById(getResources().getIdentifier("goti" + gotino, "id", getPackageName()));
        this.gotiView = imageView;
        imageView.setX((float) (this.block[target].getCenterX() - (this.s / 4)));
        this.gotiView.setY((float) (this.block[target].getCenterY() - (this.s / 4)));
        this.block[target].gotiContain.remove(this.block[target].gotiContain.size() - 1);
    }

    public void gotiMoverOnFinish(int gotino, int target) {
        int i = gotino;
        int i2 = target;
        MediaPlayer create = MediaPlayer.create(this, R.raw.move);
        this.mediaPlayer = create;
        create.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                MainActivity.this.mediaPlayer = null;
            }
        });
        this.mediaPlayer.start();
        this.block[i2].gotiContain.add(Integer.valueOf(gotino));
        int gotiOfPlayer = i / 10;
        int i3 = i % 10;
        if (i2 == 12) {
            switch (gotiOfPlayer) {
                case 1:
                    ImageView imageView = (ImageView) findViewById(getResources().getIdentifier("goti" + i, "id", getPackageName()));
                    this.gotiView = imageView;
                    imageView.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    return;
                case 2:
                    ImageView imageView2 = (ImageView) findViewById(getResources().getIdentifier("goti" + i, "id", getPackageName()));
                    this.gotiView = imageView2;
                    imageView2.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 3:
                    ImageView imageView3 = (ImageView) findViewById(getResources().getIdentifier("goti" + i, "id", getPackageName()));
                    this.gotiView = imageView3;
                    imageView3.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    return;
                case 4:
                    ImageView imageView4 = (ImageView) findViewById(getResources().getIdentifier("goti" + i, "id", getPackageName()));
                    this.gotiView = imageView4;
                    imageView4.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                default:
                    return;
            }
        } else {
            switch (this.block[i2].gotiContain.size()) {
                case 1:
                    ImageView imageView5 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(0), "id", getPackageName()));
                    this.gotiView = imageView5;
                    imageView5.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 2:
                    ImageView imageView6 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(0), "id", getPackageName()));
                    this.gotiView = imageView6;
                    imageView6.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    ImageView imageView7 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(1), "id", getPackageName()));
                    this.gotiView = imageView7;
                    imageView7.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 3:
                    ImageView imageView8 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(0), "id", getPackageName()));
                    this.gotiView = imageView8;
                    imageView8.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView9 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(1), "id", getPackageName()));
                    this.gotiView = imageView9;
                    imageView9.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView10 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(2), "id", getPackageName()));
                    this.gotiView = imageView10;
                    imageView10.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    return;
                case 4:
                    ImageView imageView11 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(0), "id", getPackageName()));
                    this.gotiView = imageView11;
                    imageView11.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView12 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(1), "id", getPackageName()));
                    this.gotiView = imageView12;
                    imageView12.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView13 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(2), "id", getPackageName()));
                    this.gotiView = imageView13;
                    imageView13.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    ImageView imageView14 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(3), "id", getPackageName()));
                    this.gotiView = imageView14;
                    imageView14.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    return;
                case 5:
                    ImageView imageView15 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(0), "id", getPackageName()));
                    this.gotiView = imageView15;
                    imageView15.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView16 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(1), "id", getPackageName()));
                    this.gotiView = imageView16;
                    imageView16.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView17 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(2), "id", getPackageName()));
                    this.gotiView = imageView17;
                    imageView17.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    ImageView imageView18 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(3), "id", getPackageName()));
                    this.gotiView = imageView18;
                    imageView18.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    ImageView imageView19 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(4), "id", getPackageName()));
                    this.gotiView = imageView19;
                    imageView19.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 6:
                    ImageView imageView20 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(0), "id", getPackageName()));
                    this.gotiView = imageView20;
                    imageView20.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView21 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(1), "id", getPackageName()));
                    this.gotiView = imageView21;
                    imageView21.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView22 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(2), "id", getPackageName()));
                    this.gotiView = imageView22;
                    imageView22.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    ImageView imageView23 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(3), "id", getPackageName()));
                    this.gotiView = imageView23;
                    imageView23.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    ImageView imageView24 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(4), "id", getPackageName()));
                    this.gotiView = imageView24;
                    imageView24.bringToFront();
                    this.gotiView.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    ImageView imageView25 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(5), "id", getPackageName()));
                    this.gotiView = imageView25;
                    imageView25.bringToFront();
                    this.gotiView.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 7:
                    ImageView imageView26 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(0), "id", getPackageName()));
                    this.gotiView = imageView26;
                    imageView26.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView27 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(1), "id", getPackageName()));
                    this.gotiView = imageView27;
                    imageView27.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView28 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(2), "id", getPackageName()));
                    this.gotiView = imageView28;
                    imageView28.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    ImageView imageView29 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(3), "id", getPackageName()));
                    this.gotiView = imageView29;
                    imageView29.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    ImageView imageView30 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(4), "id", getPackageName()));
                    this.gotiView = imageView30;
                    imageView30.bringToFront();
                    this.gotiView.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    ImageView imageView31 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(5), "id", getPackageName()));
                    this.gotiView = imageView31;
                    imageView31.bringToFront();
                    this.gotiView.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    ImageView imageView32 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(6), "id", getPackageName()));
                    this.gotiView = imageView32;
                    imageView32.bringToFront();
                    this.gotiView.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 8:
                    ImageView imageView33 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(0), "id", getPackageName()));
                    this.gotiView = imageView33;
                    imageView33.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView34 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(1), "id", getPackageName()));
                    this.gotiView = imageView34;
                    imageView34.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView35 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(2), "id", getPackageName()));
                    this.gotiView = imageView35;
                    imageView35.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    ImageView imageView36 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(3), "id", getPackageName()));
                    this.gotiView = imageView36;
                    imageView36.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    ImageView imageView37 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(4), "id", getPackageName()));
                    this.gotiView = imageView37;
                    imageView37.bringToFront();
                    this.gotiView.setX((float) (this.block[i2].getCenterX() - (this.s / 2)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    ImageView imageView38 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(5), "id", getPackageName()));
                    this.gotiView = imageView38;
                    imageView38.bringToFront();
                    this.gotiView.setX((float) this.block[i2].getCenterX());
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    ImageView imageView39 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(6), "id", getPackageName()));
                    this.gotiView = imageView39;
                    imageView39.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 2)));
                    ImageView imageView40 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(7), "id", getPackageName()));
                    this.gotiView = imageView40;
                    imageView40.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) this.block[i2].getCenterY());
                    return;
                case 9:
                    ImageView imageView41 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(8), "id", getPackageName()));
                    this.gotiView = imageView41;
                    imageView41.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 10:
                    ImageView imageView42 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(9), "id", getPackageName()));
                    this.gotiView = imageView42;
                    imageView42.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 11:
                    ImageView imageView43 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(10), "id", getPackageName()));
                    this.gotiView = imageView43;
                    imageView43.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 12:
                    ImageView imageView44 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(11), "id", getPackageName()));
                    this.gotiView = imageView44;
                    imageView44.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 13:
                    ImageView imageView45 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(12), "id", getPackageName()));
                    this.gotiView = imageView45;
                    imageView45.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 14:
                    ImageView imageView46 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(13), "id", getPackageName()));
                    this.gotiView = imageView46;
                    imageView46.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 15:
                    ImageView imageView47 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(14), "id", getPackageName()));
                    this.gotiView = imageView47;
                    imageView47.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                case 16:
                    ImageView imageView48 = (ImageView) findViewById(getResources().getIdentifier("goti" + this.block[i2].gotiContain.get(15), "id", getPackageName()));
                    this.gotiView = imageView48;
                    imageView48.setX((float) (this.block[i2].getCenterX() - (this.s / 4)));
                    this.gotiView.setY((float) (this.block[i2].getCenterY() - (this.s / 4)));
                    return;
                default:
                    return;
            }
        }
    }

    public void gotiButtonMeth(boolean flag) {
        if (!flag) {
            this.gotiButton1.setClickable(false);
            this.gotiButton2.setClickable(false);
            return;
        }
        ImageView imageView = (ImageView) findViewById(R.id.gotiButton1);
        this.gotiButton1 = imageView;
        imageView.setClickable(true);
        this.gotiButton1.setImageResource(getResources().getIdentifier("player" + (this.currentPlayer + 1) + "home", "drawable", getPackageName()));
        this.gotiButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Front2.mode.equals("offline") || (Front2.mode.equals("online") && CreateRoom.myId == MainActivity.this.turn)) {
                    MainActivity.this.throwKodi();
                }
            }
        });
        ImageView imageView2 = (ImageView) findViewById(R.id.gotiButton2);
        this.gotiButton2 = imageView2;
        imageView2.setClickable(true);
        this.gotiButton2.setImageResource(getResources().getIdentifier("player" + (this.currentPlayer + 1) + "home", "drawable", getPackageName()));
        this.gotiButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Front2.mode.equals("vsComputer") || Front2.mode.equals("offline") || (Front2.mode.equals("online") && CreateRoom.myId == MainActivity.this.turn)) {
                    MainActivity.this.throwKodi();
                }
            }
        });
    }

    public void blinkHome(boolean flag) {
        ImageView homeView = (ImageView) findViewById(getResources().getIdentifier("player" + (this.currentPlayer + 1) + "home", "id", getPackageName()));
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(100);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(-1);
        animation.setRepeatMode(2);
        homeView.startAnimation(animation);
        if (!flag) {
            homeView.clearAnimation();
        }
    }

    public void blinkGoti(boolean flag) {
        int p = this.currentPlayer + 1;
        for (int i = 1; i < 5; i++) {
            ImageView imageView = (ImageView) findViewById(getResources().getIdentifier("goti" + p + i, "id", getPackageName()));
            this.gotiView = imageView;
            imageView.bringToFront();
            float highX = this.gotiView.getX();
            float highY = this.gotiView.getY();
            ImageView imageView2 = (ImageView) findViewById(getResources().getIdentifier("highlighter" + i, "id", getPackageName()));
            this.highView = imageView2;
            imageView2.setAlpha(1.0f);
            this.highView.bringToFront();
            this.highView.setX(highX);
            this.highView.setY(highY);
            int i2 = this.s;
            RotateAnimation rotate = new RotateAnimation(0.0f, 360.0f, ((float) (i2 / 4)) + highX, ((float) (i2 / 4)) + highY);
            rotate.setDuration(80);
            rotate.setRepeatCount(-1);
            rotate.setInterpolator(new LinearInterpolator());
            this.highView.startAnimation(rotate);
            if (!flag) {
                this.highView.clearAnimation();
                this.highView.setAlpha(0.0f);
            }
        }
    }

    public void throwKodi() {
        int timelimit;
        this.throwGotiFlag = false;
        blinkHome(false);
        gotiButtonMeth(false);
        this.touchView.setClickable(false);
        if (Front2.mode.equals("vsComputer") || Front2.mode.equals("offline") || (Front2.mode.equals("online") && CreateRoom.myId == this.turn)) {
            throwKodiOnTick();
        }
        if (CreateRoom.myId == this.turn) {
            timelimit = 1000;
        } else {
            timelimit = 500;
        }
        MediaPlayer create = MediaPlayer.create(this, R.raw.throwkodisound);
        this.mediaPlayer1 = create;
        create.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                MainActivity.this.mediaPlayer1 = null;
            }
        });
        this.mediaPlayer1.start();
        new CountDownTimer((long) timelimit, 50) {
            public void onTick(long millisUntilFinished) {
                MainActivity.this.throwKodiIllusionOnTick();
            }

            public void onFinish() {
                try {
                    MainActivity.this.mediaPlayer1.stop();
                } catch (Exception e) {
                    Log.d("exception", "exception");
                }
                MainActivity.this.throwKodiOnFinish();
                if (!MainActivity.this.wonFlag) {
                    MainActivity.this.GotiOnClickFlag = true;
                    MainActivity.this.blinkGoti(true);
                    if (Front2.mode.equals("vsComputer") && MainActivity.this.countOfWay == 0) {
                        adminPermission = true;
                        int rajpoot = MainActivity.this.computer();
                        Log.d("qwert", "rajpoot " + rajpoot);
                        MainActivity.this.tap2(rajpoot);
                        adminPermission = false;
                    }
                }
            }
        }.start();
    }

    public int computer() {
        ArrayList<Integer> tempTar = new ArrayList<>();
        if (this.vsComputerStartFlag) {
            int i = new Random().nextInt(4);
            this.vsComputerStartFlag = false;
            return i + 10 + 1;
        }
        for (int i2 = 0; i2 < 4; i2++) {
            if (this.player[0].gotiScore[i2] + this.currentKodiPoint < 25) {
                Log.d("qwert", "ai " + this.player[0].gotiScore[i2]);
                int tempTarget = this.player[0].playerPath[this.player[0].gotiScore[i2] + this.currentKodiPoint];
                tempTar.add(Integer.valueOf(tempTarget));
                if (this.block[tempTarget].gotiContain.size() == 1 && (this.block[tempTarget].gotiContain.get(0).equals(31) || this.block[tempTarget].gotiContain.get(0).equals(32) || this.block[tempTarget].gotiContain.get(0).equals(33) || this.block[tempTarget].gotiContain.get(0).equals(34))) {
                    return i2 + 10 + 1;
                }
            } else {
                tempTar.add(-1);
                Log.d("qwert", "ai -1");
            }
        }
        int i3 = 0;
        for (int k = 0; k < tempTar.size(); k++) {
            Log.d("qwert", "tempTar(" + k + ")=" + tempTar.get(k));
        }
        if (tempTar.size() != 0) {
            for (int j = 0; j < tempTar.size(); j++) {
                if (tempTar.get(j).intValue() == 12) {
                    return j + 10 + 1;
                }
            }
            for (int j2 = 0; j2 < tempTar.size(); j2++) {
                if (tempTar.get(j2).intValue() != -1 && this.block[tempTar.get(j2).intValue()].gotiContain.size() == 1 && (this.block[tempTar.get(j2).intValue()].gotiContain.get(0).equals(11) || this.block[tempTar.get(j2).intValue()].gotiContain.get(0).equals(12) || this.block[tempTar.get(j2).intValue()].gotiContain.get(0).equals(13) || this.block[tempTar.get(j2).intValue()].gotiContain.get(0).equals(14))) {
                    return j2 + 10 + 1;
                }
            }
            for (int j3 = 0; j3 < tempTar.size(); j3++) {
                if (tempTar.get(j3).intValue() == 2 || tempTar.get(j3).intValue() == 10 || tempTar.get(j3).intValue() == 14 || tempTar.get(j3).intValue() == 22) {
                    return j3 + 10 + 1;
                }
            }
            int a = 0;
            for (int j4 = 0; j4 < tempTar.size(); j4++) {
                if (a <= tempTar.get(j4).intValue()) {
                    a = tempTar.get(j4).intValue();
                    i3 = j4;
                    Log.d("qwert", "end " + i3);
                }
            }
            tempTar.clear();
        }
        return i3 + 10 + 1;
    }

    public void throwKodiOnTick() {
        String name2;
        Random random = new Random();
        this.currentKodiPointStr = "Score-";
        for (int i = 1; i <= 5; i++) {
            int nextInt = random.nextInt(this.s * 4);
            int i2 = this.s;
            int touchX = (nextInt - ((i2 * 4) / 2)) - 80;
            int touchY = (random.nextInt(i2 * 4) - ((this.s * 4) / 2)) - 80;
            this.touchView = (ImageView) findViewById(getResources().getIdentifier("imageView" + i, "id", getPackageName()));
            int num = random.nextInt(20) + 1;
            if (num < 11) {
                name2 = "up" + num;
                this.touchView.setTag(1);
            } else {
                name2 = "down" + num;
                this.touchView.setTag(0);
            }
            this.currentKodiPointStr += this.touchView.getTag().toString();
            this.touchView.setImageResource(getResources().getIdentifier(name2, "drawable", getPackageName()));
            this.touchView.setX((float) (this.x + touchX));
            this.touchView.setY((float) (this.y + touchY));
        }
        if (Front2.mode.equals("online")) {
            FirebaseDatabase.getInstance().getReference("Room").child(CreateRoom.roomCode).child("player0").setValue(new OnlinePlayer(this.countOfWay + 1, "throwKodi", this.currentKodiPointStr));
        }
    }

    public void throwKodiOnFinish() {
        int kodiY;
        int score = 0;
        int i = this.currentPlayer;
        if (i == 0 || i == 1) {
            kodiY = (this.y - this.x) - ((this.s * 3) / 4);
        } else {
            kodiY = this.x + this.y;
        }
        for (int i2 = 1; i2 <= 5; i2++) {
            this.touchView = (ImageView) findViewById(getResources().getIdentifier("imageView" + i2, "id", getPackageName()));
            if (Integer.parseInt(String.valueOf(this.currentKodiPointStr.charAt(i2 + 5))) == 1) {
                this.touchView.setImageResource(R.drawable.up2);
                score++;
            } else {
                this.touchView.setImageResource(R.drawable.down12);
            }
            ImageView imageView = this.touchView;
            int i3 = this.s;
            imageView.setX((float) (((-i3) / 8) + (((i2 * 3) * i3) / 4)));
            this.touchView.setY((float) kodiY);
        }
        switch (score) {
            case 0:
                this.currentKodiPoint = 5;
                break;
            case 1:
                this.currentKodiPoint = 1;
                break;
            case 2:
                this.currentKodiPoint = 2;
                break;
            case 3:
                this.currentKodiPoint = 3;
                break;
            case 4:
                this.currentKodiPoint = 8;
                break;
            case 5:
                this.currentKodiPoint = 10;
                break;
        }
        this.throwGotiFlag = true;
        Toast.makeText(this, "score is " + this.currentKodiPoint, Toast.LENGTH_SHORT).show();
        this.wonFlag = true;
        for (int i4 = 0; i4 < 4; i4++) {
            int[] iArr = this.player[this.currentPlayer].gotiScore;
            iArr[i4] = iArr[i4] + this.currentKodiPoint;
            if (this.player[this.currentPlayer].gotiScore[i4] < 25) {
                this.wonFlag = false;
            }
            int[] iArr2 = this.player[this.currentPlayer].gotiScore;
            iArr2[i4] = iArr2[i4] - this.currentKodiPoint;
        }
        if (this.wonFlag) {
            startProgress(false);
            blinkGoti(false);
            this.GotiOnClickFlag = false;
            int i5 = this.countOfWay + 1;
            this.countOfWay = i5;
            if (i5 == this.noOfPlayer) {
                this.countOfWay = 0;
            }
            this.currentPlayer = this.wayOfMovingPlayer[this.countOfWay];
            if (Front2.mode.equals("online") && CreateRoom.myId == this.turn) {
                FirebaseDatabase.getInstance().getReference("Room").child(CreateRoom.roomCode).child("player0").setValue(new OnlinePlayer(this.countOfWay + 1, "changePlayer", "null"));
            }
            if (!Front2.mode.equals("vsComputer") || this.countOfWay != 0) {
                this.throwGotiFlag = true;
                blinkHome(true);
                int identifier = getResources().getIdentifier("player" + (this.currentPlayer + 1) + "home", "id", getPackageName());
                this.Id = identifier;
                ImageView imageView2 = (ImageView) findViewById(identifier);
                this.touchView = imageView2;
                imageView2.setClickable(true);
                startProgress(true);
                gotiButtonMeth(true);
                this.touchView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (Front2.mode.equals("vsComputer") || Front2.mode.equals("offline") || (Front2.mode.equals("online") && CreateRoom.myId == MainActivity.this.turn)) {
                            MainActivity.this.throwKodi();
                        }
                    }
                });
                return;
            }
            new CountDownTimer(((long) (this.currentKodiPoint + 2)) * 333, 333) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    MainActivity.this.throwGotiFlag = true;
                    MainActivity.this.blinkHome(true);
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.Id = mainActivity.getResources().getIdentifier("player" + (MainActivity.this.currentPlayer + 1) + "home", "id", MainActivity.this.getPackageName());
                    MainActivity mainActivity2 = MainActivity.this;
                    mainActivity2.touchView = (ImageView) mainActivity2.findViewById(mainActivity2.Id);
                    MainActivity.this.touchView.setClickable(true);
                    MainActivity.this.startProgress(true);
                    MainActivity.this.gotiButtonMeth(true);
                    MainActivity.this.throwKodi();
                }
            }.start();
        }
    }

    public void throwKodiIllusionOnTick() {
        String name2;
        Random random = new Random();
        for (int i = 1; i <= 5; i++) {
            int nextInt = random.nextInt(this.s * 4);
            int i2 = this.s;
            int touchX = (nextInt - ((i2 * 4) / 2)) - 80;
            int touchY = (random.nextInt(i2 * 4) - ((this.s * 4) / 2)) - 80;
            this.touchView = (ImageView) findViewById(getResources().getIdentifier("imageView" + i, "id", getPackageName()));
            int num = random.nextInt(20) + 1;
            if (num < 11) {
                name2 = "up" + num;
            } else {
                name2 = "down" + num;
            }
            this.touchView.setImageResource(getResources().getIdentifier(name2, "drawable", getPackageName()));
            this.touchView.setX((float) (this.x + touchX));
            this.touchView.setY((float) (this.y + touchY));
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Do you want sava game").setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, Front2.class));
            }
        }).setNegativeButton("No", (DialogInterface.OnClickListener) null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit").setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.this.finish();
                MainActivity.this.startActivity(new Intent(MainActivity.this, Front2.class));
            }
        }).setNegativeButton("No", (DialogInterface.OnClickListener) null);
        builder.create().show();
    }
//End of MainActivity Class
}