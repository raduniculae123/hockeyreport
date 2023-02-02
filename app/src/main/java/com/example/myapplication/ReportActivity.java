package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class ReportActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    private TextView shiftTimeTxtView;
    private TextView periodTxtView;

    //Report Variables
    private int period = 1;


    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        chronometer = findViewById(R.id.chronometer);
        shiftTimeTxtView = findViewById(R.id.shiftTimeTxtView);
        periodTxtView = findViewById(R.id.periodTxtView);

        //Buttons
        Button endShiftBtn = findViewById(R.id.endShiftBtn);
        Button startShiftBtn = findViewById(R.id.startShiftBtn);
        Button pauseShiftBtn = findViewById(R.id.pauseShiftBtn);
        Button prevPeriodBtn = findViewById(R.id.prevPeriodBtn);
        Button nextPeriodBtn = findViewById(R.id.nextPeriodBtn);
        Button endGameBtn = findViewById(R.id.endGameBtn);


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

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void generatePDF() {
        Document document = new Document();
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