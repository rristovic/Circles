package com.dufine.circles;

/**
 * Created by Sarma on 6/14/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * View that circles will be drawn on
 */
public class CircleView extends SurfaceView implements SurfaceHolder.Callback {

    private int mCanvas_w;
    private int mCanvas_h;

    private Bitmap mCanvasBitmap;
    private Canvas mCanvas = new Canvas();
    private int radius;
    private Matrix identityMatrix;

    private Random random = new Random();
    private Paint p;
    public int[] color;

    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private CircleDrawingThread mThread;

    // Circles count, radius and control
    private boolean isFirstRun = true;
    private int mCirclesCount = 10;
    private int mFirstRadius = Integer.MAX_VALUE;
    private int mPrevRadius = Integer.MAX_VALUE;
    private int mCurRadius = 0;
    private boolean isFirstDrawing;

    // Game variables
    private int SCALE_FACTOR = 3;
    private int mLevel = 1;

    public static final String LEVEL_COUNT = "CircleView_level_count";
    public static final String CIRCLES_COUNT = "CircleView_circles_count";

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
        mContext = context;
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // Height and width of the surface view
        mCanvas_w = getWidth();
        mCanvas_h = getHeight();

        // Bitmap used for rendering and drawing circles
        mCanvasBitmap = Bitmap.createBitmap(mCanvas_w, mCanvas_h, Bitmap.Config.ARGB_8888);

        // Canvas that ready bitmap is saved on
        mCanvas.setBitmap(mCanvasBitmap);

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

    @Override
    public void onDraw(Canvas canvas) {
        int w = mCanvas.getWidth();
        int h = mCanvas.getHeight();

        // Draw the outer circle before continuing with the game
        if (isFirstDrawing) {
            mCanvas.drawCircle(w / 2, h / 2, mFirstRadius * SCALE_FACTOR, p);
            canvas.drawBitmap(mCanvasBitmap, identityMatrix, null);
            isFirstDrawing = false;
            setPaint();
        }

        ++mCurRadius;
        mCanvas.drawCircle(w / 2, h / 2, mCurRadius * SCALE_FACTOR, p);
        canvas.drawBitmap(mCanvasBitmap, identityMatrix, null);


    }

    public void spread() {
        mThread = new CircleDrawingThread(mSurfaceHolder, this);
        mThread.setRunning(true);
        mThread.start();

        updateInfo();
        setPaint();
        mCirclesCount--;
    }

    public void pause() {
        mThread.pause();
    }

    public void stop() {
        mThread.setRunning(false);
    }

    public void play() {
        mThread.play();
    }

    /**
     * @return true is new circle can be drawn, false is game over.
     */
    public boolean isCircleValid() {
        return mCurRadius < mFirstRadius &&
                mCurRadius < mPrevRadius &&
                mCurRadius * SCALE_FACTOR < mContext.getResources().getDisplayMetrics().widthPixels / 2;
    }

    public void createNewCircle() {

        if (mCirclesCount == 0) {
            // TODO: Add circle count logic to the game
            mLevel++;
            mCirclesCount = 10;
            isFirstDrawing = true;
        }

        // Save previous radius to compare to current
        mPrevRadius = mCurRadius;
        mCurRadius = 0;

        // Saving first created circle and its radius
        if (isFirstRun) {
            mFirstRadius = mPrevRadius;
            isFirstRun = false;
        }

        if (isFirstDrawing) {
            mPrevRadius = mFirstRadius;
            isFirstRun = true;
        }

        updateInfo();
        setPaint();
        mCirclesCount--;


    }

    private void gameOver() {
        mThread.setRunning(false);

        Intent i = new Intent(mContext,ResultActivity.class);
        i.putExtra(LEVEL_COUNT,mLevel);
        i.putExtra(CIRCLES_COUNT,mCirclesCount);
        mContext.startActivity(i);

        ((CircleActivity)mContext).finish();
    }

    private void setPaint() {

        // TODO: Add painting logic to the game
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.argb(255, r, g, b));
        p.setStyle(Paint.Style.FILL);
    }

    public int getCircleCount(){
        return mCirclesCount;
    }

    public int getLevel(){
        return mLevel;
    }

    private void updateInfo(){
        ((CircleActivity) mContext).updateInfo(Integer.toString(mCirclesCount),Integer.toString(mLevel));
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
                if (isCircleValid() && !isPaused)
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
                else if (!isPaused)
                    gameOver();

            }
        }

    }


}