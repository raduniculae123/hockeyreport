package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class ReportActivity extends AppCompatActivity {


    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    private TextView shiftTimeTxtView;
    private TextView periodTxtView;

    //Report Variables
    private static int period = 1;

    private static int goals = 0;
    private static int a1 = 0;
    private static int a2 = 0;
    private static int sog = 0;
    private static int notOnGoalShots = 0;
    private static int blk = 0;
    private static int pd = 0;
    private static int pt = 0;
    private static int fow = 0;
    private static int fol = 0;

    private static int shotsFor = 0; //of the team (for CORSI)
    private static int shotsAgainst = 0; //of the  team (for CORSI)
    private static int cf = 0;
    private static int ca = 0;

    private static int goalsFor = 0;
    private static int goalsAgainst = 0;
    private static float timeOnIce = 0;
    private static int shifts = 0;

    private static int possesionsWon = 0;
    private static int possesionsLost = 0;

    private static final int REQUEST_CODE_1 = 1; //SHOT ON GOAL ACTIVITY
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 1;



    private Button endShiftBtn;
    private Button startShiftBtn;
    private Button pauseShiftBtn;
    private Button prevPeriodBtn;
    private Button nextPeriodBtn;
    private Button endGameBtn;
    private Button shotBtn;
    private Button onNetBtn;
    private Button notOnNetBtn;
    private Button shotForBtn;
    private Button shotAgainstBtn;
    private Button penaltyDrawnBtn;
    private Button penaltyTakenBtn;
    private Button faceOff;
    private Button possessionBtn;
    private Button assistBtn;
    private Button fightBtn;
    private Button hitBtn;
    private Button goalBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        chronometer = findViewById(R.id.chronometer);
        shiftTimeTxtView = findViewById(R.id.shiftTimeTxtView);
        periodTxtView = findViewById(R.id.periodTxtView);

        //Buttons
        endShiftBtn = findViewById(R.id.endShiftBtn);
        startShiftBtn = findViewById(R.id.startShiftBtn);
        pauseShiftBtn = findViewById(R.id.pauseShiftBtn);
        prevPeriodBtn = findViewById(R.id.prevPeriodBtn);
        nextPeriodBtn = findViewById(R.id.nextPeriodBtn);
        endGameBtn = findViewById(R.id.endGameBtn);
        shotBtn = findViewById(R.id.shotBtn);
        onNetBtn = findViewById(R.id.onNetBtn);
        notOnNetBtn = findViewById(R.id.notOnNetBtn);
        shotForBtn = findViewById(R.id.shotForBtn);
        shotAgainstBtn = findViewById(R.id.shotAgainstBtn);
        penaltyDrawnBtn = findViewById(R.id.penaltyDrawnBtn);
        penaltyTakenBtn = findViewById(R.id.penaltyTakenBtn);
        faceOff = findViewById(R.id.faceoffBtn);
        possessionBtn = findViewById(R.id.possesionLostBtn);
        assistBtn = findViewById(R.id.assistBtn);
        fightBtn = findViewById(R.id.fightBtn);
        hitBtn = findViewById(R.id.hitBtn);
        goalBtn = findViewById(R.id.goalBtn);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        nextPeriodBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                prevPeriodBtn.setEnabled(true);
                period++;
                if (period <= 3) {
                    periodTxtView.setText("Period " + period);
                } else if (period == 4) {
                    periodTxtView.setText("Period OT");
                } else if (period == 5) {
                    periodTxtView.setText("Period SO");
                    nextPeriodBtn.setEnabled(false);
                }
            }
        });

        prevPeriodBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                nextPeriodBtn.setEnabled(true);
                period--;
                if (period <= 3) {
                    periodTxtView.setText("Period " + period);

                } else if (period == 4) {
                    periodTxtView.setText("Period OT");
                }
                if (period == 1) {
                    prevPeriodBtn.setEnabled(false);
                }
            }
        });


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

        endShiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
                chronometer.stop();
                running = false;
            }
        });

        endGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    generatePDF();
                }
            }
        });

        /*----------------------------------------FACEOFFS---------------------------------------------*/
        faceOff.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                //WIN/LOSE
            }
        });

        /*----------------------------------------PENALTIES------------------------------------------------*/
        penaltyDrawnBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ReportActivity.pd++;
            }
        });

        penaltyTakenBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ReportActivity.pt++;
            }
        });


        /*----------------------------------------SHOTS------------------------------------------------*/
        shotBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ReportActivity.shotsFor++;
                onNetBtn.setVisibility(View.VISIBLE);
                notOnNetBtn.setVisibility(View.VISIBLE);
                penaltyDrawnBtn.setEnabled(false);
                penaltyTakenBtn.setEnabled(false);
                faceOff.setEnabled(false);
                shotBtn.setEnabled(false);
                shotForBtn.setEnabled(false);
                shotAgainstBtn.setEnabled(false);
                possessionBtn.setEnabled(false);
                assistBtn.setEnabled(false);
                hitBtn.setEnabled(false);
                fightBtn.setEnabled(false);
                goalBtn.setEnabled(false);


            }
        });

        onNetBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ReportActivity.sog++;

                Intent myIntent = new Intent(ReportActivity.this, NetActivity.class);
                myIntent.putExtra("goal", "0");
                startActivityForResult(myIntent, REQUEST_CODE_1);
            }
        });


        notOnNetBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ReportActivity.notOnGoalShots++;
                onNetBtn.setVisibility(View.INVISIBLE);
                notOnNetBtn.setVisibility(View.INVISIBLE);
                penaltyDrawnBtn.setEnabled(true);
                penaltyTakenBtn.setEnabled(true);
                faceOff.setEnabled(true);
                shotBtn.setEnabled(true);
                shotForBtn.setEnabled(true);
                shotAgainstBtn.setEnabled(true);
                possessionBtn.setEnabled(true);
                assistBtn.setEnabled(true);
                hitBtn.setEnabled(true);
                fightBtn.setEnabled(true);
                goalBtn.setEnabled(true);

            }
        });

        goalBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ReportActivity.sog++;

                Intent myIntent = new Intent(ReportActivity.this, NetActivity.class);
                myIntent.putExtra("goal", "1");
                startActivityForResult(myIntent, REQUEST_CODE_1);
            }
        });

        shotForBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ReportActivity.shotsFor++;
            }
        });
        shotAgainstBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                ReportActivity.shotsAgainst++;
            }
        });

        /*----------------------------------------SHOTS------------------------------------------------*/

        hitBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ReportActivity.this, IceRink.class);
                ReportActivity.this.startActivity(myIntent);
            }
        });

        fightBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String goal = data.getStringExtra("goal");
                // SHOT ON GOAL
                if (goal.equals("0")) {
                    Toast.makeText(this, "Shot added", Toast.LENGTH_SHORT).show();
                    onNetBtn.setVisibility(View.INVISIBLE);
                    notOnNetBtn.setVisibility(View.INVISIBLE);
                    penaltyDrawnBtn.setEnabled(true);
                    penaltyTakenBtn.setEnabled(true);
                    faceOff.setEnabled(true);
                    shotBtn.setEnabled(true);
                    shotForBtn.setEnabled(true);
                    shotAgainstBtn.setEnabled(true);
                    possessionBtn.setEnabled(true);
                    assistBtn.setEnabled(true);
                    hitBtn.setEnabled(true);
                    fightBtn.setEnabled(true);
                    goalBtn.setEnabled(true);
                } else if (goal.equals("1")) {
                    Toast.makeText(this, "Goal added", Toast.LENGTH_SHORT).show();
                    onNetBtn.setVisibility(View.INVISIBLE);
                    notOnNetBtn.setVisibility(View.INVISIBLE);
                    penaltyDrawnBtn.setEnabled(true);
                    penaltyTakenBtn.setEnabled(true);
                    faceOff.setEnabled(true);
                    shotBtn.setEnabled(true);
                    shotForBtn.setEnabled(true);
                    shotAgainstBtn.setEnabled(true);
                    possessionBtn.setEnabled(true);
                    assistBtn.setEnabled(true);
                    hitBtn.setEnabled(true);
                    fightBtn.setEnabled(true);
                    goalBtn.setEnabled(true);

                }

            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void generatePDF() {
        Rectangle pageSize = PageSize.A4.rotate();
        Document document = new Document(pageSize);
        try {
            String fileName = "pdf_file.pdf";

            // Get the content resolver and create a new file
            ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            Uri uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

            // Open the output stream and write the PDF file
            FileOutputStream fos = (FileOutputStream) contentResolver.openOutputStream(uri);
            PdfWriter.getInstance(document, fos);
            document.open();
            document.add(new Paragraph("Hello World!"));
            document.close();

            Log.d("PDF", "Report Created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}