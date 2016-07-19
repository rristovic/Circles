package com.dufine.circles;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Random;

/**
 * Created by Dusan on 3.3.2015.
 */
public class Circle {
    private SurfaceHolder mSurfaceHolder;
    private CircleView mCircleView;
    private float r;
    private float growth=10;
    private Paint paint=new Paint();
    private CountDownTimer timer=null;
    private Circle preCircle;
    private boolean full=false;
    public int[] color;
    private boolean available=true;

    private CircleDrawingThread mThread;

    public Circle(int[] color, SurfaceHolder surfaceHolder, CircleView view){
        this.color=color;
        setPaint();

        mSurfaceHolder = surfaceHolder;
        mCircleView = view;
    }
    public Circle(Circle preCircle, SurfaceHolder surfaceHolder, CircleView view){
        this.color=color;
        setPaint();
        this.preCircle=preCircle;

        mSurfaceHolder = surfaceHolder;
        mCircleView = view;
    }

    public float getR() {
        return r;
    }

    public float getGrowth() {
        return growth;
    }

    public Paint getPaint() {
        return paint;
    }

    public Circle getPreCircle() {
        return preCircle;
    }

    public CountDownTimer getTimer() {
        return timer;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setPreCircle(Circle preCircle) {
        this.preCircle = preCircle;
    }

    private void setPaint(){
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255,color[0],color[1],color[2]));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4.5f);
    }

    public void spread(){
        mThread = new CircleDrawingThread(mSurfaceHolder, mCircleView);
        mThread.setRunning(true);
        mThread.start();
    }

    public void pause(){
        mThread.pause();
    }

    public void stop(){
        mThread.setRunning(false);
    }

    /**
     * Thread that will be used for canvas drawing control and calculations
     */
    class CircleDrawingThread extends Thread {
        private SurfaceHolder mSurfaceHolder;
        private CircleView mView;
        private boolean mRun = false;

        private int mTime;
        private boolean isPaused = false;

        public CircleDrawingThread(SurfaceHolder surfaceHolder, CircleView view) {
            mSurfaceHolder = surfaceHolder;
            mView = view;
        }

        public void setRunning(boolean run) {
            mRun = run;
        }

        protected void pause() {
            isPaused = true;
        }

        protected void play() {
            isPaused = false;
        }

        @Override
        public void run() {
            Canvas c;
            while (mRun) {
                c = null;
                if (!hasTouched() && !full && !isPaused) {
                    if (preCircle != null && r + growth >= preCircle.getR()) {
                        r = preCircle.getR() - 1;
                        full = true;
                    } else if (r + growth >= CircleActivity.bitMap.getWidth() / 2) {
                        r = CircleActivity.bitMap.getWidth() / 2;
                        full = true;
                    } else {
                        r += growth;
                    }
                    try {
                        c = mSurfaceHolder.lockCanvas(null);
                        if (c != null)
                            synchronized (mSurfaceHolder) {
                                Log.d("CircleActivity", "onDraw");
                                mView.onDraw(c);
                            }
                    } finally {
                        // do this in a finally so that if an exception is thrown
                        // during the above, we don't leave the Surface in an
                        // inconsistent state
                        if (c != null) {
                            mSurfaceHolder.unlockCanvasAndPost(c);
                        }
                    }
                } else {
                    setRunning(false);
                }
            }
        }

        private void drawCircle(Canvas c) {

        }
    }

    public void drawCircle(){
        CircleActivity.canvas.drawCircle(CircleActivity.bitMap.getWidth()/2, CircleActivity.bitMap.getHeight()/2,r,paint);
        //CircleActivity.img.setImageBitmap(CircleActivity.bitMap);
    }
    private boolean hasTouched(){
        return ((preCircle!=null && preCircle.getR()<r)|| r>= CircleActivity.bitMap.getWidth() / 2);
    }

}
