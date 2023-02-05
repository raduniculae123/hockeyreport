package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class HockeyFieldView extends View {
    private Paint mIcePaint;
    private Paint mLinePaint;
    private Paint mGoalLinePaint;
    private Paint mCirclePaint;

    private Paint mCircleCenterPaint;
    private Paint mFaceOffDotPaint;

    private Paint mFaceOffCentreDotPaint;

    private Paint mText;
    private Paint mCreasePaint;

    public HockeyFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mIcePaint = new Paint();
        mIcePaint.setColor(Color.WHITE);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.BLUE);
        mLinePaint.setStrokeWidth(1f);

        mGoalLinePaint = new Paint();
        mGoalLinePaint.setColor(Color.RED);
        mGoalLinePaint.setStrokeWidth(1f);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setStrokeWidth(1f);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mCircleCenterPaint = new Paint();
        mCircleCenterPaint.setColor(Color.BLUE);
        mCircleCenterPaint.setStrokeWidth(1f);
        mCircleCenterPaint.setStyle(Paint.Style.STROKE);

        mFaceOffDotPaint = new Paint();
        mFaceOffDotPaint.setColor(Color.RED);
        mFaceOffDotPaint.setStrokeWidth(2f);

        mFaceOffCentreDotPaint = new Paint();
        mFaceOffCentreDotPaint.setColor(Color.BLUE);
        mFaceOffCentreDotPaint.setStrokeWidth(2f);

        mCreasePaint = new Paint();
        mCreasePaint.setColor(Color.BLUE);

        mText = new Paint();
        mText.setColor(Color.GRAY);
        mText.setStrokeWidth(0.5f);
        mText.setTextSize(5f);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scaleFactor = getWidth() / 200;

        Matrix matrix = new Matrix();
        matrix.setScale(scaleFactor, scaleFactor);
        canvas.setMatrix(matrix);


        //Rink
        RectF rect = new RectF(0, 0, 197, 98.5f);
        canvas.drawRoundRect(rect, 14, 14, mIcePaint);

        //Lines
        canvas.drawRect(12, 0, 13, 98.5f, mGoalLinePaint);
        canvas.drawRect(67, 0, 70, 98.5f, mLinePaint);
        canvas.drawRect(197 - 12, 0, 197 - 13, 98.5f, mGoalLinePaint);
        canvas.drawRect(197 - 67, 0, 197 - 70, 98.5f, mLinePaint);
        canvas.drawRect(97, 0, 100, 98.5f, mGoalLinePaint);

        //Circles
        canvas.drawCircle(197f / 2, 98.5f / 2, 15, mCircleCenterPaint);
        canvas.drawCircle(32.80f, 23.11f, 15, mCirclePaint);
        canvas.drawCircle(32.80f, 98.5f - 23.11f, 15, mCirclePaint);
        canvas.drawCircle(197f - 32.80f, 23.11f, 15, mCirclePaint);
        canvas.drawCircle(197f - 32.80f, 98.5f - 23.11f, 15, mCirclePaint);

        //FaceOff Dots
        canvas.drawPoint(197f / 2, 98.5f / 2, mFaceOffCentreDotPaint);
        canvas.drawPoint(32.80f, 23.11f, mFaceOffDotPaint);
        canvas.drawPoint(32.80f, 98.5f - 23.11f, mFaceOffDotPaint);
        canvas.drawPoint(197f - 32.80f, 23.11f, mFaceOffDotPaint);
        canvas.drawPoint(197f - 32.80f, 98.5f - 23.11f, mFaceOffDotPaint);
        canvas.drawPoint(73 + 3f, 23.11f, mFaceOffDotPaint);
        canvas.drawPoint(73 + 3f, 98.5f - 23.11f, mFaceOffDotPaint);
        canvas.drawPoint(197f - 73 - 3f, 23.11f, mFaceOffDotPaint);
        canvas.drawPoint(197f - 73 - 3f, 98.5f - 23.11f, mFaceOffDotPaint);

        //Nets Creases
        canvas.drawRect(12f, 46.25f, 15f, 52.25f, mCreasePaint);
        canvas.drawRect(197f - 15f, 46.25f, 197f - 12f, 52.25f, mCreasePaint);


        canvas.drawText("Attacking zone", 18f, 98.5f / 2f, mText);
        canvas.drawText("Defensive zone", 197f - 60f, 98.5f / 2f, mText);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            float x = event.getX();
//            float y = event.getY();
//            Log.d("TEST", "X: " + x + " Y: " + y);
//        }
        float maxX = 0, maxY = 0;

        float x = event.getX();
        float y = event.getY();
        if (maxX < x) {
            maxX = x;
            Log.d("TEST", "X: " + maxX + " Y: " + maxY);
        }
        if (maxY < y) {
            maxY = y;
            Log.d("TEST", "X: " + maxX + " Y: " + maxY);
        }
        return true;
    }
}