package com.dufine.circles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Dusan on 3.3.2015.
 */
public class CircleActivity extends ActionBarActivity {
    private SurfaceHolder mSurfaceHolder;
    private CircleView mSurfaceView;
    private TextView tvLevel, tvCLeft;
    private ToggleButton mToggleButton;

    private boolean isFirstRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

        init();
    }

    private void init() {
        mSurfaceView = (CircleView) findViewById(R.id.surfaceVIEW);
        tvCLeft = (TextView) findViewById(R.id.tvCLeft);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        mToggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        mSurfaceView.setZOrderOnTop(true);    // necessary
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        setOnClickListener();

    }

    private void setOnClickListener() {
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFirstRun){
                    mSurfaceView.spread();
                    isFirstRun = false;
                    return;
                }
                mSurfaceView.createNewCircle();
            }
        });

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSurfaceView.pause();
                } else
                    mSurfaceView.play();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void gameOver(){
        finish();
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

    public void updateInfo(String circlesLeft, String level){
        tvCLeft.setText(circlesLeft);
        tvLevel.setText(level);
    }
}
