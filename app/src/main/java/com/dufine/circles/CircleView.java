package com.dufine.circles;

/**
 * Created by Sarma on 6/14/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Calendar;
import java.util.Random;

/**
 * View that circles will be drawn on
 */
public class CircleView extends SurfaceView implements SurfaceHolder.Callback {

    private int mCenterX, mCenterY;
    private Circle mCircle;
    private int myCanvas_w;
    private int myCanvas_h;
    private Bitmap myCanvasBitmap;
    private Canvas myCanvas = new Canvas();
    private int radius;
    private Matrix identityMatrix;


    public CircleView(Context context) {
        super(context);
        init(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        getHolder().addCallback(this);
        mCenterX = 0;
    }
    public void spread(){
        Random r = new Random();
        mCircle = new Circle(new int[]{r.nextInt(256),r.nextInt(256),r.nextInt(256)},getHolder(),this);
        mCircle.spread();
    }

    public void pause(){
        mCircle.pause();
    }

    public void stop(){
        mCircle.stop();
    }


    @Override
    public void onDraw(Canvas canvas) {
        Random random = new Random();
        int w = myCanvas.getWidth();
        int h = myCanvas.getHeight();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);

        Paint p = new Paint();
        p.setARGB(255,mCircle.color[0],mCircle.color[1],mCircle.color[2]);
        myCanvas.drawCircle(w/2, h/2,++radius,p);

        canvas.drawBitmap(myCanvasBitmap, identityMatrix, null);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        myCanvas_w = getWidth();
        myCanvas_h = getHeight();
        myCanvasBitmap = Bitmap.createBitmap(myCanvas_w, myCanvas_h, Bitmap.Config.ARGB_8888);

        myCanvas.setBitmap(myCanvasBitmap);

        identityMatrix = new Matrix();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // simply copied from sample application LunarLander:
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
//        boolean retry = true;
//        mThread.setRunning(false);
//        while (retry) {
//            try {
//                mThread.join();
//                retry = false;
//            } catch (InterruptedException e) {
//                // we will try it again and again...
//            }
//        }
    }




}