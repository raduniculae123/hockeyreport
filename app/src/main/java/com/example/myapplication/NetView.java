package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class NetView extends View {

    private Paint mIcePaint;
    private Paint mGoalLinePaint;


    public NetView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mIcePaint = new Paint();
        mIcePaint.setColor(Color.WHITE);

        mGoalLinePaint = new Paint();
        mGoalLinePaint.setColor(Color.RED);
        mGoalLinePaint.setStrokeWidth(1f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scaleFactor = getWidth() / 200;

        Matrix matrix = new Matrix();
        matrix.setScale(scaleFactor, scaleFactor);
        canvas.setMatrix(matrix);


        //Background
        RectF rect = new RectF(0, 0, 197, 98.5f);
        canvas.drawRoundRect(rect, 14, 14, mIcePaint);

        RectF goalie = new RectF(40, 15, 72*2+25, 48*2);
        canvas.drawRoundRect(goalie, 7, 7, mGoalLinePaint);
        //add bar on bottom with white

    }
}
