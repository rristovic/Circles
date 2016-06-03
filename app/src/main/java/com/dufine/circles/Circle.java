package com.dufine.circles;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import java.util.Random;

/**
 * Created by Dusan on 3.3.2015.
 */
public class Circle {
    private float r;
//    private float growth=new Random().nextFloat()*5+15;
//    private float growth=new Random().nextFloat()*5+10;
//    private float growth=new Random().nextFloat()*5+5;
    private float growth=10;
    private Paint paint=new Paint();
    private CountDownTimer timer=null;
    private Circle preCircle;
    private boolean full=false;
    private int color;
    private boolean available=true;

    public Circle(int color){
        this.color=color;
        setPaint();
    }
    public Circle(Circle preCircle,int color){
        this.color=color;
        setPaint();
        this.preCircle=preCircle;
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
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4.5f);
    }

    public void spread(){
        timer =new CountDownTimer(5000,1) {

            @Override
            public void onTick(long millisUntilFinished) {
             if (!hasTouched() && !full) {
                if (preCircle != null && r + growth >= preCircle.getR()) {
                    r = preCircle.getR() - 1;
                    full=true;
                } else if (r + growth >= CircleActivity.bitMap.getWidth() / 2) {
                    r = CircleActivity.bitMap.getWidth() / 2;
                    full=true;
                } else {
                    r += growth;
                }
                drawCircle();
                }
             else timer.onFinish();
            }

            @Override
            public void onFinish() {
                available=false;
                CircleActivity.img.setEnabled(false);
            }

        };
        timer.start();
    }

    public void drawCircle(){
        CircleActivity.canvas.drawCircle(CircleActivity.bitMap.getWidth()/2, CircleActivity.bitMap.getHeight()/2,r,paint);
        CircleActivity.img.setImageBitmap(CircleActivity.bitMap);
    }
    private boolean hasTouched(){
        return ((preCircle!=null && preCircle.getR()<r)|| r>= CircleActivity.bitMap.getWidth() / 2);
    }

}
