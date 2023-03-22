package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class PastPerformance extends AppCompatActivity {

    private EditText g1;
    private EditText g2;
    private EditText g3;
    private EditText a1;
    private EditText a2;
    private EditText a3;
    private EditText s1;
    private EditText s2;
    private EditText s3;

    private Button submitPastBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_performance);

        g1 = findViewById(R.id.g1);
        g2 = findViewById(R.id.g2);
        g3 = findViewById(R.id.g3);
        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        submitPastBtn = findViewById(R.id.submitPastBtn);

        submitPastBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                ReportActivity.pg1 = TextUtils.isEmpty(g1.getText()) ? 0 : Integer.parseInt(g1.getText().toString());
                ReportActivity.pg2 = TextUtils.isEmpty(g2.getText()) ? 0 : Integer.parseInt(g2.getText().toString());
                ReportActivity.pg3 = TextUtils.isEmpty(g3.getText()) ? 0 : Integer.parseInt(g3.getText().toString());

                ReportActivity.pa1 = TextUtils.isEmpty(a1.getText()) ? 0 : Integer.parseInt(a1.getText().toString());
                ReportActivity.pa2 = TextUtils.isEmpty(a2.getText()) ? 0 : Integer.parseInt(a2.getText().toString());
                ReportActivity.pa3 = TextUtils.isEmpty(a3.getText()) ? 0 : Integer.parseInt(a3.getText().toString());

                ReportActivity.ps1 = TextUtils.isEmpty(s1.getText()) ? 0 : Integer.parseInt(s1.getText().toString());
                ReportActivity.ps2 = TextUtils.isEmpty(s2.getText()) ? 0 : Integer.parseInt(s2.getText().toString());
                ReportActivity.ps3 = TextUtils.isEmpty(s3.getText()) ? 0 : Integer.parseInt(s3.getText().toString());






                Intent resultIntent = new Intent();
                setResult(GameInfoActivity.RESULT_OK, resultIntent);
                finish();

            }
        });
    }
}