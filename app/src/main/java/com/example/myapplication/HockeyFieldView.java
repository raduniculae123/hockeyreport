package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

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

public class HockeyFieldView extends View {

    private Bitmap mBitmap;

    private float mScaleFactor = 2.75f;

    private Matrix matrix;

    HockeyFieldActivity fieldActivity;

    public void setMainActivity(HockeyFieldActivity fieldActivity) {
        this.fieldActivity = fieldActivity;
    }

    public HockeyFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hockeyrink);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        matrix = new Matrix();
        matrix.setScale(mScaleFactor, mScaleFactor);
        canvas.setMatrix(matrix);

        canvas.drawBitmap(mBitmap, 0, 0, null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            float x = event.getX();
            float y = event.getY();

            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            // Invert the matrix before drawing the circle
            Matrix inverse = new Matrix();
            matrix.invert(inverse);
            float[] touchPoint = {x, y};
            inverse.mapPoints(touchPoint);
            x = touchPoint[0];
            y = touchPoint[1];

            canvas.drawBitmap(mBitmap, 0, 0, null);

            // Draw a small circle at the touch point
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            canvas.drawCircle(x, y, 10, paint);

            mBitmap = bitmap;
            invalidate();

            String eventType = fieldActivity.getEventType();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("eventX", Float.toString(x));
            resultIntent.putExtra("eventY", Float.toString(y));
            resultIntent.putExtra("eventType", eventType);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fieldActivity.setResult(RESULT_OK, resultIntent);
                    fieldActivity.finish();
                }
            }, 700);
        }
        return true;
    }
}