package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class GameInfoActivity extends AppCompatActivity {
    public static EditText position;
    public static EditText teamAgainst;
    public static EditText goalsFor;
    public static EditText date;
    public static EditText name;
    public static EditText goalsAgainst;
    public static EditText teamFor;
    public static EditText location;

    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        position = findViewById(R.id.positionInput);
        teamAgainst = findViewById(R.id.teamAgainstInput);
        teamFor = findViewById(R.id.teamForInput);
        name = findViewById(R.id.nameInput);
        date = findViewById(R.id.dateInput);
        goalsFor = findViewById(R.id.goalsForInput);
        goalsAgainst = findViewById(R.id.goalsAgainstInput);
        submitBtn = findViewById(R.id.submitBtn);
        location = findViewById(R.id.locationInput);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ReportActivity.position = String.valueOf(position.getText());
                ReportActivity.teamFor = String.valueOf(teamFor.getText());
                ReportActivity.teamAgainst = String.valueOf(teamAgainst.getText());
                ReportActivity.name = String.valueOf(name.getText());
                ReportActivity.date = String.valueOf(date.getText());
                ReportActivity.location = String.valueOf(location.getText());
                if(!String.valueOf(goalsFor.getText()).equals("") && !String.valueOf(goalsAgainst.getText()).equals("") ){
                    ReportActivity.goalsFor = Integer.valueOf(String.valueOf(goalsFor.getText()));
                    ReportActivity.goalsAgainst = Integer.valueOf(String.valueOf(goalsAgainst.getText()));
                }

                Intent resultIntent = new Intent();
                setResult(GameInfoActivity.RESULT_OK, resultIntent);
                finish();

            }
        });
    }


}