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
import android.os.Handler;
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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    private static void resetVar() {
        goals = 0;
        a1 = 0;
        a2 = 0;
        sog = 0;
        notOnGoalShots = 0;
        blk = 0;
        pd = 0;
        pt = 0;
        fow = 0;
        fol = 0;

        shotsFor = 0; //of the team (for CORSI)
        shotsAgainst = 0; //of the  team (for CORSI)


        goalsFor = 0;
        goalsAgainst = 0;
        timeOnIce = 0;
        shifts = 0;

        possessionsWon = 0;
        possessionsLost = 0;

        hits = 0;

        fights = 0;

        firstPeriod = new float[8];
        secondPeriod = new float[8];
        thirdPeriod = new float[8];
        otPeriod = new float[8];


        xlsxData = new int[51];
        fieldEvents = new ArrayList<>();
        netEvents = new ArrayList<>();


        reportData = new String[]{"Team for: ", "Team against: ", "Position: ", "Time on ice: ", "Shift average: ", "Goals: ", "1st Assists: ", "2nd Assists: ", "Points: ", "Shots: ", "Shots on Goal: ", "SOG%: ", "FaceOffs Won: ", "FaceOffs Lost: ", "FOW%: ", "Penalties Drawn: ", "Penalties Taken: ", "Possessions Won: ", "Possessions Lost: "};


        columnNames = new String[]{"Name", "Date", "Team Against", "Location", "Goals For", "Goals Against", "p1g", "p1a", "p1sog", "p1toi", "p1shfavg", "p1posw", "p1posl", "p1shfnr", "p2g", "p2a", "p2sog", "p2toi", "p2shfavg", "p2posw", "p2posl", "p2shfnr", "p3g", "p3a", "p3sog", "p3toi", "p3shfavg", "p3posw", "p3posl", "p3shfnr", "p4g", "p4a", "p4sog", "p4toi", "p4shfavg", "p4posw", "p4posl", "p4shfnr", "a1", "a2", "sog", "nsog", "toi", "posw", "posl", "shfnr", "blk", "pd", "pt", "fow", "fol", "shotsFor", "shotsAgaints", "shfavg", "goals"};

        String name = "Radu Niculae";
        String date = "24102021";
        String location = "iceSheffield";
        String teamFor = "Steaua";
        String teamAgainst = "Brasov";
        String position = "Right Wing";


    }

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


    public static int goalsFor = 0;
    public static int goalsAgainst = 0;
    private static int timeOnIce = 0;
    private static int shifts = 0;

    private static int possessionsWon = 0;
    private static int possessionsLost = 0;

    private static int hits = 0;

    private static int fights = 0;


    private static final int REQUEST_CODE_1 = 1; //Hockey field ACTIVITY
    private static final int FIRST_ACTIVITY_REQUEST_CODE = 1;

    private static final int REQUEST_CODE_2 = 2; //NET ACTIVITY
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 2;


    private static final int REQUEST_CODE_3 = 3; //GameInfoActivity
    private static final int THIRD_ACTIVITY_REQUEST_CODE = 3;


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
    private Button faceOffWonBtn;
    private Button faceOffLostBtn;
    private Button possessionLostBtn;
    private Button possessionWonBtn;
    private Button assistBtn;
    private Button fightBtn;
    private Button hitBtn;
    private Button goalBtn;

    private Button firstAssistBtn;

    private Button secondAssistBtn;

    private Button shotBlockedBtn;

    // Each index represents a metric:
    /*

        0 - goals
        1 - a1+a2
        2 - sog
        3 - time on ice in seconds
        4 - shift avg
        5 - possessions won
        6 - possessions lost
        7 - number of shifts



     */
    private static float[] firstPeriod = new float[8];
    private static float[] secondPeriod = new float[8];
    private static float[] thirdPeriod = new float[8];
    private static float[] otPeriod = new float[8];


    private static int[] xlsxData = new int[51];
    private static ArrayList<Triplet> fieldEvents = new ArrayList<>();
    private static ArrayList<Triplet> netEvents = new ArrayList<>();


    private static String[] reportData = {"Team for: ", "Team against: ", "Position: ", "Time on ice: ", "Shift average: ", "Goals: ", "1st Assists: ", "2nd Assists: ", "Points: ", "Shots: ", "Shots on Goal: ", "SOG%: ", "FaceOffs Won: ", "FaceOffs Lost: ", "FOW%: ", "Penalties Drawn: ", "Penalties Taken: ", "Possessions Won: ", "Possessions Lost: "};


    private static String[] columnNames = {"Name", "Date", "Team Against", "Location", "Goals For", "Goals Against", "p1g", "p1a", "p1sog", "p1toi", "p1shfavg", "p1posw", "p1posl", "p1shfnr", "p2g", "p2a", "p2sog", "p2toi", "p2shfavg", "p2posw", "p2posl", "p2shfnr", "p3g", "p3a", "p3sog", "p3toi", "p3shfavg", "p3posw", "p3posl", "p3shfnr", "p4g", "p4a", "p4sog", "p4toi", "p4shfavg", "p4posw", "p4posl", "p4shfnr", "a1", "a2", "sog", "nsog", "toi", "posw", "posl", "shfnr", "blk", "pd", "pt", "fow", "fol", "shotsFor", "shotsAgaints", "shfavg", "goals"};

    public static String name = "Radu Niculae";
    public static String date = "24102021";
    public static String location = "iceSheffield";
    public static String teamFor = "Steaua";
    public static String teamAgainst = "Brasov";
    public static String position = "Right Wing";

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
        faceOffWonBtn = findViewById(R.id.faceoffWonBtn);
        faceOffLostBtn = findViewById(R.id.faceoffLostBtn);
        possessionLostBtn = findViewById(R.id.possesionLostBtn);
        possessionWonBtn = findViewById(R.id.possesionWonBtn);
        assistBtn = findViewById(R.id.assistBtn);
        fightBtn = findViewById(R.id.fightBtn);
        hitBtn = findViewById(R.id.hitBtn);
        goalBtn = findViewById(R.id.goalBtn);
        firstAssistBtn = findViewById(R.id.firstAssistBtn);
        secondAssistBtn = findViewById(R.id.secondAssistBtn);
        shotBlockedBtn = findViewById(R.id.shotBlockedBtn);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        nextPeriodBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                prevPeriodBtn.setEnabled(true);
                period++;
                Toast.makeText(getBaseContext(), "Next period", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getBaseContext(), "Previous period", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getBaseContext(), "Started shift", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getBaseContext(), "Paused shift", Toast.LENGTH_SHORT).show();
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
            }
        });

        endShiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "End shift", Toast.LENGTH_SHORT).show();
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                int elapsedSeconds = (int) (elapsedMillis / 1000);
                shifts++;
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
                chronometer.stop();
                running = false;

                if (period == 1) {
                    firstPeriod[3] += elapsedSeconds;
                    firstPeriod[7]++;

                } else if (period == 2) {
                    secondPeriod[3] += elapsedSeconds;
                    secondPeriod[7]++;

                } else if (period == 3) {
                    thirdPeriod[3] += elapsedSeconds;
                    thirdPeriod[7]++;
                } else if (period == 4) {
                    otPeriod[3] += elapsedSeconds;
                    otPeriod[7]++;
                }
            }
        });

        endGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(ReportActivity.this, GameInfoActivity.class);
                startActivityForResult(intent, THIRD_ACTIVITY_REQUEST_CODE);


            }
        });


        /*----------------------------------------FACEOFFS---------------------------------------------*/
        faceOffWonBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                fow++;
                Toast.makeText(getBaseContext(), "FaceOff Won", Toast.LENGTH_SHORT).show();
            }
        });

        faceOffLostBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                fol++;
                Toast.makeText(getBaseContext(), "FaceOff Lost", Toast.LENGTH_SHORT).show();
            }
        });

        /*----------------------------------------PENALTIES------------------------------------------------*/
        penaltyDrawnBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                pd++;
                Toast.makeText(getBaseContext(), "Penalty Drawn", Toast.LENGTH_SHORT).show();
            }
        });

        penaltyTakenBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                pt++;
                Toast.makeText(getBaseContext(), "Penalty Taken", Toast.LENGTH_SHORT).show();
            }
        });
        /*----------------------------------------ASSISTS------------------------------------------------*/
        assistBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                firstAssistBtn.setVisibility(View.VISIBLE);
                secondAssistBtn.setVisibility(View.VISIBLE);
                penaltyDrawnBtn.setEnabled(false);
                penaltyTakenBtn.setEnabled(false);
                faceOffWonBtn.setEnabled(false);
                faceOffLostBtn.setEnabled(false);
                shotBtn.setEnabled(false);
                shotForBtn.setEnabled(false);
                shotAgainstBtn.setEnabled(false);
                possessionLostBtn.setEnabled(false);
                possessionWonBtn.setEnabled(false);
                assistBtn.setEnabled(false);
                hitBtn.setEnabled(false);
                fightBtn.setEnabled(false);
                goalBtn.setEnabled(false);


                if (period == 1) {
                    firstPeriod[1]++;
                } else if (period == 2) {
                    secondPeriod[1]++;
                } else if (period == 3) {
                    thirdPeriod[1]++;
                } else if (period == 4) {
                    otPeriod[1]++;
                }

            }
        });

        firstAssistBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                a1++;
                Intent myIntent = new Intent(ReportActivity.this, HockeyFieldActivity.class);
                myIntent.putExtra("eventType", "3");
                startActivityForResult(myIntent, REQUEST_CODE_1);
                buttonsEnable();
            }
        });
        secondAssistBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "2nd Assist", Toast.LENGTH_SHORT).show();
                a2++;
                buttonsEnable();
            }
        });




        /*----------------------------------------POSSESSIONS------------------------------------------------*/
        possessionLostBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                possessionsLost++;
                if (period == 1) {
                    firstPeriod[6]++;
                } else if (period == 2) {
                    secondPeriod[6]++;
                } else if (period == 3) {
                    thirdPeriod[6]++;
                } else if (period == 4) {
                    otPeriod[6]++;
                }

                Intent myIntent = new Intent(ReportActivity.this, HockeyFieldActivity.class);
                myIntent.putExtra("eventType", "5");
                startActivityForResult(myIntent, REQUEST_CODE_1);
            }
        });

        possessionWonBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                possessionsWon++;
                if (period == 1) {
                    firstPeriod[5]++;
                } else if (period == 2) {
                    secondPeriod[5]++;
                } else if (period == 3) {
                    thirdPeriod[5]++;
                } else if (period == 4) {
                    otPeriod[5]++;
                }
                Intent myIntent = new Intent(ReportActivity.this, HockeyFieldActivity.class);
                myIntent.putExtra("eventType", "4");
                startActivityForResult(myIntent, REQUEST_CODE_1);
            }
        });


        /*----------------------------------------SHOTS------------------------------------------------*/
        shotBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                shotsFor++;
                onNetBtn.setVisibility(View.VISIBLE);
                notOnNetBtn.setVisibility(View.VISIBLE);
                shotBlockedBtn.setVisibility(View.VISIBLE);
                penaltyDrawnBtn.setEnabled(false);
                penaltyTakenBtn.setEnabled(false);
                faceOffWonBtn.setEnabled(false);
                faceOffLostBtn.setEnabled(false);
                shotBtn.setEnabled(false);
                shotForBtn.setEnabled(false);
                shotAgainstBtn.setEnabled(false);
                possessionLostBtn.setEnabled(false);
                possessionWonBtn.setEnabled(false);
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
                sog++;
                if (period == 1) {
                    firstPeriod[2]++;
                } else if (period == 2) {
                    secondPeriod[2]++;
                } else if (period == 3) {
                    thirdPeriod[2]++;
                } else if (period == 4) {
                    otPeriod[2]++;
                }
                Intent myIntent = new Intent(ReportActivity.this, HockeyFieldActivity.class);
                myIntent.putExtra("eventType", "1");
                startActivityForResult(myIntent, REQUEST_CODE_1);
            }
        });

        shotBlockedBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                notOnGoalShots++;
                blk++;
                shotsFor++;
                Toast.makeText(getBaseContext(), "Blocked shot", Toast.LENGTH_SHORT).show();
                buttonsEnable();

            }
        });


        notOnNetBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                ReportActivity.notOnGoalShots++;
                buttonsEnable();
                Intent myIntent = new Intent(ReportActivity.this, HockeyFieldActivity.class);
                myIntent.putExtra("eventType", "2");
                startActivityForResult(myIntent, REQUEST_CODE_1);

            }
        });

        goalBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                goals++;
                sog++;
                shotsFor++;
                if (period == 1) {
                    firstPeriod[2]++;
                    firstPeriod[0]++;
                } else if (period == 2) {
                    secondPeriod[2]++;
                    secondPeriod[0]++;
                } else if (period == 3) {
                    thirdPeriod[2]++;
                    thirdPeriod[0]++;
                } else if (period == 4) {
                    otPeriod[2]++;
                    otPeriod[0]++;
                }
                Intent myIntent = new Intent(ReportActivity.this, HockeyFieldActivity.class);
                myIntent.putExtra("eventType", "0");
                startActivityForResult(myIntent, REQUEST_CODE_1);
            }
        });

        shotForBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Shot for", Toast.LENGTH_SHORT).show();
                shotsFor++;
            }
        });
        shotAgainstBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Shot against", Toast.LENGTH_SHORT).show();
                shotsAgainst++;
            }
        });

        /*----------------------------------------HITS & FIGHTS------------------------------------------------*/

        hitBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Hit", Toast.LENGTH_SHORT).show();
                hits++;
            }
        });

        fightBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Fight", Toast.LENGTH_SHORT).show();
                fights++;
            }
        });

    }

    protected void buttonsEnable() {
        onNetBtn.setVisibility(View.INVISIBLE);
        notOnNetBtn.setVisibility(View.INVISIBLE);
        firstAssistBtn.setVisibility(View.INVISIBLE);
        secondAssistBtn.setVisibility(View.INVISIBLE);
        shotBlockedBtn.setVisibility(View.INVISIBLE);
        penaltyDrawnBtn.setEnabled(true);
        penaltyTakenBtn.setEnabled(true);
        faceOffWonBtn.setEnabled(true);
        faceOffLostBtn.setEnabled(true);
        shotBtn.setEnabled(true);
        shotForBtn.setEnabled(true);
        shotAgainstBtn.setEnabled(true);
        possessionWonBtn.setEnabled(true);
        possessionLostBtn.setEnabled(true);
        assistBtn.setEnabled(true);
        hitBtn.setEnabled(true);
        fightBtn.setEnabled(true);
        goalBtn.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FIRST_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String eventType = data.getStringExtra("eventType");
                float eventX = Float.parseFloat(data.getStringExtra("eventX"));
                float eventY = Float.parseFloat(data.getStringExtra("eventY"));
                // GOAL
                if (eventType.equals("0")) {
                    fieldEvents.add(new Triplet(eventType, eventX, eventY));
                    Intent myIntent = new Intent(ReportActivity.this, NetActivity.class);
                    myIntent.putExtra("goal", "0");
                    startActivityForResult(myIntent, REQUEST_CODE_2);
                    // SHOT ON NET
                } else if (eventType.equals("1")) {
                    fieldEvents.add(new Triplet(eventType, eventX, eventY));
                    Intent myIntent = new Intent(ReportActivity.this, NetActivity.class);
                    myIntent.putExtra("goal", "1");
                    startActivityForResult(myIntent, REQUEST_CODE_2);
                }
                // SHOT NOT ON NET
                else if (eventType.equals("2")) {
                    fieldEvents.add(new Triplet(eventType, eventX, eventY));
                    Toast.makeText(this, "Shot NOT on net", Toast.LENGTH_SHORT).show();
                }
                // 1st ASSIST
                else if (eventType.equals("3")) {
                    fieldEvents.add(new Triplet(eventType, eventX, eventY));
                    Toast.makeText(this, "1st Assist", Toast.LENGTH_SHORT).show();
                }
                // POSSESSION WON
                else if (eventType.equals("4")) {
                    fieldEvents.add(new Triplet(eventType, eventX, eventY));
                    Toast.makeText(this, "Possession won", Toast.LENGTH_SHORT).show();
                }
                // POSSESSION LOST
                else if (eventType.equals("5")) {
                    fieldEvents.add(new Triplet(eventType, eventX, eventY));
                    Toast.makeText(this, "Possession lost", Toast.LENGTH_SHORT).show();
                }


            }
        }
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String eventType = data.getStringExtra("goal");
                float shotX = Float.parseFloat(data.getStringExtra("shotX"));
                float shotY = Float.parseFloat(data.getStringExtra("shotY"));

                // SHOT ON GOAL
                if (eventType.equals("0")) {
                    Toast.makeText(this, "Goal!", Toast.LENGTH_SHORT).show();
                    buttonsEnable();
                    netEvents.add(new Triplet(eventType, shotX, shotY));


                } else if (eventType.equals("1")) {
                    Toast.makeText(this, "Shot on net", Toast.LENGTH_SHORT).show();
                    buttonsEnable();
                    netEvents.add(new Triplet(eventType, shotX, shotY));
                }

            }
        }

        if (requestCode == THIRD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    createEmptyXLSXFile();
                    generatePDF();
                    Toast.makeText(getBaseContext(), "Report Generated", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            resetVar();
                        }
                    }, 500);
                }
            }
        }

    }

    private void insertData() {

        if (firstPeriod[7] != 0)
            firstPeriod[4] = firstPeriod[3] * 1.0f / firstPeriod[7];
        if (secondPeriod[7] != 0)
            secondPeriod[4] = secondPeriod[3] * 1.0f / secondPeriod[7];
        if (thirdPeriod[7] != 0)
            thirdPeriod[4] = thirdPeriod[3] * 1.0f / thirdPeriod[7];
        if (otPeriod[7] != 0)
            otPeriod[4] = otPeriod[3] * 1.0f / otPeriod[7];
        int k = 0;
        xlsxData[k] = goalsFor;
        k++;
        xlsxData[k] = goalsAgainst;
        k++;
        for (int i = 0; i < firstPeriod.length; i++) {
            xlsxData[k] = (int) firstPeriod[i];
            k++;
        }
        for (int i = 0; i < secondPeriod.length; i++) {
            xlsxData[k] = (int) secondPeriod[i];
            k++;
        }
        for (int i = 0; i < thirdPeriod.length; i++) {
            xlsxData[k] = (int) thirdPeriod[i];
            k++;
        }
        for (int i = 0; i < otPeriod.length; i++) {
            xlsxData[k] = (int) otPeriod[i];
            k++;
        }
        timeOnIce = (int) (firstPeriod[3] + secondPeriod[3] + thirdPeriod[3] + otPeriod[3]);

        xlsxData[k] = a1;
        k++;
        xlsxData[k] = a2;
        k++;
        xlsxData[k] = sog;
        k++;
        xlsxData[k] = notOnGoalShots;
        k++;
        xlsxData[k] = timeOnIce;
        k++;
        xlsxData[k] = possessionsWon;
        k++;
        xlsxData[k] = possessionsLost;
        k++;
        xlsxData[k] = shifts;
        k++;
        xlsxData[k] = blk;
        k++;
        xlsxData[k] = pd;
        k++;

        xlsxData[k] = pt;
        k++;
        xlsxData[k] = fow;
        k++;
        xlsxData[k] = fol;
        k++;
        xlsxData[k] = shotsFor;
        k++;
        xlsxData[k] = shotsAgainst;
        k++;
        xlsxData[k] = (int) (timeOnIce * 1.0 / shifts);
        k++;
        xlsxData[k] = goals;

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    void generatePDF() {
        Rectangle pageSize = PageSize.A4.rotate();
        Document document = new Document(pageSize);
        try {
            String fileName = "pdf_file.pdf";
            String timeOnIceString = Integer.toString(timeOnIce / 60) + " mins " + Integer.toString(timeOnIce % 60) + " sec";
            String[] reportValues = {" ", " ", " ", " ", Integer.toString(xlsxData[49]), Integer.toString(goals), Integer.toString(a1), String.valueOf(a2), Integer.toString(a1 + a2 + goals), Integer.toString(notOnGoalShots + sog), Integer.toString(sog), sog * 1.0 / (notOnGoalShots + sog) * 100 + "%", Integer.toString(fow), Integer.toString(fol), fow * 1.0 / (fow + fol) * 100 + "%", Integer.toString(pd), Integer.toString(pt), Integer.toString(possessionsWon), Integer.toString(possessionsLost)};
            reportValues[3] = timeOnIceString;
            Log.d("blk", "+++" + Integer.toString(notOnGoalShots + sog));
            // Get the content resolver and create a new file
            ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            Uri uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

            // Open the output stream and write the PDF file
            FileOutputStream fos = (FileOutputStream) contentResolver.openOutputStream(uri);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            document.open();

            // Create a PdfContentByte instance and set the font
            PdfContentByte cb = writer.getDirectContent();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb.setFontAndSize(bf, 40);
            cb.setColorFill(BaseColor.BLUE);
            float maxHeight = pageSize.getHeight();
            float maxWidth = pageSize.getWidth();

            PdfContentByte canvas = writer.getDirectContent();
            canvas.moveTo(230, document.bottom());
            canvas.lineTo(230, document.top());
            canvas.stroke();

            // Set the text location with coordinates
            cb.beginText();
            cb.setTextMatrix(3, 565);
            cb.showText(name + " - " + location + " - " + date);


            cb.setFontAndSize(bf, 17);
            int y = 535;
            cb.setTextMatrix(3, y);
            cb.showText(reportData[0] + teamFor + " - " + goalsFor);
            y -= 27;

            cb.setTextMatrix(3, y);
            cb.showText(reportData[1] + teamAgainst + " - " + goalsAgainst);
            y -= 27;

            cb.setTextMatrix(3, y);
            cb.showText(reportData[2] + position);
            y -= 27;


            for (int i = 3; i < reportData.length; i++) {
                cb.setTextMatrix(3, y);
                cb.showText(reportData[i] + reportValues[i]);
                y -= 27.5;
            }

            cb.setTextMatrix(3, y);
            cb.showText("Hits: " + hits);
            y -= 27;


            // Each index represents a metric:
    /*

        0 - goals
        1 - a1+a2
        2 - sog
        3 - time on ice in seconds
        4 - shift avg
        5 - possessions won
        6 - possessions lost
        7 - number of shifts



     */
            y = 535;
            String[] periodNames = {"Goals: ", "Assists: ", "SOG: ", "Time on ice: ", "Possessions won: ", "Possessions lost: ", "Shifts: "};
            cb.setTextMatrix(250, y);
            cb.showText("Period 1: ");
            y -= 27;
            for (int i = 0; i < periodNames.length; i++) {
                cb.setTextMatrix(250, y);
                cb.showText(periodNames[i] + (int) firstPeriod[i]);
                y -= 27;
            }

            y = 535;
            cb.setTextMatrix(455, y);
            cb.showText("Period 2: ");
            y -= 27;
            for (int i = 0; i < periodNames.length; i++) {
                cb.setTextMatrix(455, y);
                cb.showText(periodNames[i] + (int) secondPeriod[i]);
                y -= 27;
            }

            y = 535;
            cb.setTextMatrix(665, y);
            cb.showText("Period 3: ");
            y -= 27;
            for (int i = 0; i < periodNames.length; i++) {
                cb.setTextMatrix(665, y);
                cb.showText(periodNames[i] + (int) thirdPeriod[i]);
                y -= 27;
            }


            cb.endText();



            /*
            // Load the image from the drawable resource
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.net);

            // Create an Image object from the bitmap
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());

            // Set the image position and size
            image.setAbsolutePosition(100, 100);
            image.scaleToFit(400, 400);

            // Add the image to the PDF
            cb.addImage(image);
            */
            document.close();

            Log.d("PDF", "Report Created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    void createEmptyXLSXFile() {
        insertData();
        String fileName = "empty_file.xlsx";

        // Create a new XSSFWorkbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        // Add a sheet and set the value of the first cell to 1
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow row0 = sheet.createRow(0);
        int i, j, k, l;
        for (i = 0; i < columnNames.length; i++) {
            XSSFCell cell = row0.createCell(i);
            cell.setCellValue(columnNames[i]);
        }

        XSSFRow row1 = sheet.createRow(1);


        XSSFCell cell = row1.createCell(0);
        cell.setCellValue("Name");

        cell = row1.createCell(1);
        cell.setCellValue("Date");

        cell = row1.createCell(2);
        cell.setCellValue("Team against");

        cell = row1.createCell(3);
        cell.setCellValue("Location");

        l = 4;


        for (j = 0; j < xlsxData.length; j++) {
            cell = row1.createCell(l);
            cell.setCellValue(xlsxData[j]);
            l++;
        }

        for (k = 0; k < netEvents.size(); k++) {
            cell = row0.createCell(i);
            cell.setCellValue("netType");
            cell = row0.createCell(i + 1);
            cell.setCellValue("netX");
            cell = row0.createCell(i + 2);
            cell.setCellValue("netY");
            i += 3;
        }

        for (k = 0; k < fieldEvents.size(); k++) {
            cell = row0.createCell(i);
            cell.setCellValue("fieldType");
            cell = row0.createCell(i + 1);
            cell.setCellValue("fieldX");
            cell = row0.createCell(i + 2);
            cell.setCellValue("fieldY");
            i += 3;
        }

        for (j = 0; j < netEvents.size(); j++) {
            cell = row1.createCell(l);
            cell.setCellValue(netEvents.get(j).eventType);
            l++;
            cell = row1.createCell(l);
            cell.setCellValue(netEvents.get(j).x);
            l++;
            cell = row1.createCell(l);
            cell.setCellValue(netEvents.get(j).y);
            l++;
        }

        for (j = 0; j < fieldEvents.size(); j++) {
            cell = row1.createCell(l);
            cell.setCellValue(fieldEvents.get(j).eventType);
            l++;
            cell = row1.createCell(l);
            cell.setCellValue(fieldEvents.get(j).x);
            l++;
            cell = row1.createCell(l);
            cell.setCellValue(fieldEvents.get(j).y);
            l++;
        }


        // Save the file to the Downloads directory
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
        ContentResolver contentResolver = getContentResolver();
        Uri uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

        // Write the XSSFWorkbook to the output stream
        try {
            OutputStream outputStream = contentResolver.openOutputStream(uri);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
            Log.d("XLSX", "File created");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class Triplet {
        String eventType;
        float x;
        float y;

        public Triplet(String eventType, float x, float y) {
            this.eventType = eventType;
            this.x = x;
            this.y = y;
        }
    }


}