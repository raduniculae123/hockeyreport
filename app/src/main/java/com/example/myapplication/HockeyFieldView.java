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
    private Paint mFaceOffDotPaint;
    private Paint mCreasePaint;
    private float mCornerRadius;

    public HockeyFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mIcePaint = new Paint();
        mIcePaint.setColor(Color.WHITE);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.BLUE);
        mLinePaint.setStrokeWidth(5f);

        mGoalLinePaint = new Paint();
        mGoalLinePaint.setColor(Color.RED);
        mGoalLinePaint.setStrokeWidth(5f);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setStrokeWidth(5f);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mFaceOffDotPaint = new Paint();
        mFaceOffDotPaint.setColor(Color.BLACK);
        mFaceOffDotPaint.setStrokeWidth(5f);

        mCreasePaint = new Paint();
        mCreasePaint.setColor(Color.BLUE);

        mCornerRadius = 50f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scaleFactor = getWidth() / 200;

        Matrix matrix = new Matrix();
        matrix.setScale(scaleFactor, scaleFactor);
        canvas.setMatrix(matrix);

        RectF rect = new RectF(0, 0, 197, 98.5f);
        canvas.drawRoundRect(rect, 14, 14, mIcePaint);
        canvas.drawRect(12, 0, 13, 98.5f, mGoalLinePaint);
        canvas.drawRect(67, 0, 70, 98.5f, mLinePaint);
        canvas.drawRect(197-12, 0, 197-13, 98.5f, mGoalLinePaint);
        canvas.drawRect(197-67, 0, 197-70, 98.5f, mLinePaint);
        canvas.drawRect(97, 0, 100, 98.5f, mGoalLinePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            Log.d("TEST", "X: " + x + " Y: " + y);
        }
        return true;
    }
}