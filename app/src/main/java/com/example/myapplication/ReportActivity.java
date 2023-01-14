package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class ReportActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    private TextView shiftTimeTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        chronometer = findViewById(R.id.chronometer);
        Button endShiftBtn = findViewById(R.id.endShiftBtn);
        Button startShiftBtn = findViewById(R.id.startShiftBtn);
        Button pauseShiftBtn = findViewById(R.id.pauseShiftBtn);
        Button resetButton = findViewById(R.id.endShiftBtn);

        shiftTimeTxtView = findViewById(R.id.shiftTimeTxtView);


        startShiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }
            }
        });

        pauseShiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
                chronometer.stop();
                running = false;
            }
        });
    }
}