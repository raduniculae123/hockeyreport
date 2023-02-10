package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class NetActivity extends AppCompatActivity {

    NetView netView;
    private String goal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        netView = findViewById(R.id.net_view);
        netView.setMainActivity(this);
        Bundle extras = getIntent().getExtras();
        this.goal = extras.getString("goal");

    }


    public String getGoal() {

        return goal;
    }

}