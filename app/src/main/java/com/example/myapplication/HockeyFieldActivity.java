package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HockeyFieldActivity extends AppCompatActivity {

    HockeyFieldView fieldView;
    private String eventType;

    /*
    EVENTS:
    0 - goal
    1 - shot on net
    2 - shot NOT on net
    3 - 1st assist
    4 - possession won
    5 - possession lost
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ice_rink);
        fieldView = findViewById(R.id.hockey_field_view);
        fieldView.setMainActivity(this);
        Bundle extras = getIntent().getExtras();
        this.eventType = extras.getString("eventType");
    }

    public String getEventType() {

        return eventType;
    }


}