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
import android.os.CountDownTimer;
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

    private int myCanvas_w;
    private int myCanvas_h;

    private Bitmap myCanvasBitmap;
    private Canvas myCanvas = new Canvas();
    private int radius;
    private Matrix identityMatrix;

    private Random random = new Random();
    private Paint p;
    private SurfaceHolder mSurfaceHolder;
    private CircleView mCircleView;
    private float r;
    private float growth = 10;
    private Paint paint = new Paint();
    public int[] color;

    private CircleDrawingThread mThread;


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
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // Height and width of the surface view
        myCanvas_w = getWidth();
        myCanvas_h = getHeight();

        // Bitmap used for rendering and drawing circles
        myCanvasBitmap = Bitmap.createBitmap(myCanvas_w, myCanvas_h, Bitmap.Config.ARGB_8888);

        // Canvas that ready bitmap is saved on
        myCanvas.setBitmap(myCanvasBitmap);

        // Matrix used for drawing bitmaps onto the canvas
        identityMatrix = new Matrix();

        // Creating a thread for first use
        mThread = new CircleDrawingThread(mSurfaceHolder = getHolder(), this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("CircleView", "entered surfaceDestroyed()");
        // simply copied from sample application LunarLander:
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        mThread.setRunning(false);
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
    }

    public void spread() {
        setPaint();
        if (mThread!=null && Thread.State.TERMINATED.equals(mThread.getState())) {
            Log.d("CircleView","Thread is terminated!");
            mThread = new CircleDrawingThread(mSurfaceHolder, this);
            mThread.setRunning(true);
            mThread.start();
        } else {
            mThread.setRunning(true);
            mThread.start();
        }
    }

    private void setPaint() {
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        p = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, r, g, b));
        paint.setStyle(Paint.Style.FILL);
    }

    public void pause() {
        mThread.pause();
    }

    public void stop() {
        mThread.setRunning(false);
    }

    @Override
    public void onDraw(Canvas canvas) {

        int w = myCanvas.getWidth();
        int h = myCanvas.getHeight();
        myCanvas.drawCircle(w / 2, h / 2, ++radius, p);

        canvas.drawBitmap(myCanvasBitmap, identityMatrix, null);
    }

    /**
     * Thread that will be used for canvas drawing control and calculations
     */
    class CircleDrawingThread extends Thread {
        private SurfaceHolder mSurfaceHolder;
        private CircleView mView;
        private boolean mRun = false;

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

            }
        }
    }

}