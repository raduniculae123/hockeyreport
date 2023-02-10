package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class NetView extends View {

    private Bitmap mBitmap;
    private Bitmap mBitmap2;
    private float mScaleFactor = 0.95f;
    private float mScaleFactor2 = 0.5f;

    private float mXPos = 0;
    private float mYPos = 0;

    NetActivity netActivity;
    public void setMainActivity(NetActivity netActivity) {
        this.netActivity = netActivity;
    }


    public NetView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.net);
        mBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.puck);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Matrix matrix = new Matrix();
        matrix.setScale(mScaleFactor, mScaleFactor);
        canvas.setMatrix(matrix);

        canvas.drawBitmap(mBitmap, 5, 5, null);

        float width = mBitmap2.getWidth();
        float height = mBitmap2.getHeight();

        float scaledWidth = width * mScaleFactor2;
        float scaledHeight = height * mScaleFactor2;

        float left = mXPos - scaledWidth / 2;
        float top = mYPos - scaledHeight / 2;
        float right = mXPos + scaledWidth / 2;
        float bottom = mYPos + scaledHeight / 2;

        RectF rectF = new RectF(left, top, right, bottom);

        canvas.drawBitmap(mBitmap2, null, rectF, null);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            mXPos = x;
            mYPos = y;
            invalidate();

            String goal = netActivity.getGoal();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("shotX", Float.toString(x));
            resultIntent.putExtra("shotY", Float.toString(y));
            resultIntent.putExtra("goal", goal);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    netActivity.setResult(RESULT_OK, resultIntent);
                    netActivity.finish();
                }
            }, 700);
        }
        return true;
    }
}
