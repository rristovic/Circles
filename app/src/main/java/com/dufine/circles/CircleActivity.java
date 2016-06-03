package com.dufine.circles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Dusan on 3.3.2015.
 */
public class CircleActivity extends ActionBarActivity {
    private LinkedList<Circle> circles;
    private int score=0;
    private int count=0;
    private TextView txtScore;
    protected static ImageView img;
    protected static Bitmap bitMap;
    protected static Canvas canvas;
    private int change=0;
    private int[] colors=setColor();
    private int number=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        txtScore=(TextView) findViewById(R.id.txtScore);
        img=(ImageView) findViewById(R.id.imageView);
        circles=new LinkedList<Circle>();
        setBitmap();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score++;
                count++;
                if (circles.size() == 0) {
                    Circle circle = new Circle(colors[change]);
                    change=1;
                    circles.add(circle);
                    circle.spread();
                } else {
                    Circle circle = new Circle(circles.get((circles.size() - 1)),colors[2*(count/10)+change]);
                    if(change==1) change=0;
                        else change=1;
                    circles.add(circle);
                    circle.getPreCircle().getTimer().cancel();


                    if (count % 10 == 0) {
                        int capacity = circles.size() - 1;
                        for (int i = 1; i < 10 - count / 10+number; i++) {
                            circles.remove(capacity - i);
                        }
                        number=1;
                        circle.setPreCircle(circles.get(circles.size() - 2));
                        for (int i = 0; i < circles.size() - 1; i++) {
                            circles.get(i).drawCircle();
                        }
                        img.setEnabled(true);
                    }
                    circle.spread();
                }
                txtScore.setText(Integer.toString(score));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        img.setEnabled(true);
    }

    public void setBitmap(){
        //creating bitmap for every display
        Display display=getWindowManager().getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        int x=point.x*1000/480;
        int y=point.y*1000/800;

        bitMap = Bitmap.createBitmap(x,y, Bitmap.Config.ARGB_8888);  //creates bmp
        bitMap = bitMap.copy(bitMap.getConfig(), true);     //lets bmp to be mutable
        bitMap.eraseColor(Color.BLACK);
        canvas=new Canvas(bitMap);    //draw a canvas in defined bmp
        img.setImageBitmap(bitMap);
    }

    private int[] setColor() {
        int[] colors = new int[10];
        colors[0] = Color.rgb(0xC9, 0x1E, 0x1E);
        colors[1] = Color.rgb(0xFF, 0x00, 0x00);
        colors[2] = Color.rgb(0x00, 0x00, 0xFF);
        colors[3] = Color.rgb(0x82, 0x82, 0xFF);
        colors[4] = Color.rgb(0xFF, 0xA2, 0x00);
        colors[5] = Color.rgb(0xFF, 0xCD, 0x75);
        colors[6] = Color.rgb(0x68, 0xD6, 0x00);
        colors[7] = Color.rgb(0xBB, 0xFF, 0x00);
        colors[8] = Color.rgb(0xBC, 0x43, 0xDE);
        colors[9] = Color.rgb(0xE6, 0x00, 0xFF);
        return colors;
    }
    private boolean gameOver(){
        for (int i=0;i<circles.size();i++){
         if(circles.get(i).isAvailable()) return false;
        }
        return true;
    }


}
