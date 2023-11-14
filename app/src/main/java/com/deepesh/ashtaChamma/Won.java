package com.deepesh.ashtaChamma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Won extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);
        TextView textView = findViewById(R.id.wonPlayerName);
        String str = "player " + getIntent().getIntExtra("player",1)+" won";
        textView.setText(str);
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Front2.class));
        finish();
    }

}