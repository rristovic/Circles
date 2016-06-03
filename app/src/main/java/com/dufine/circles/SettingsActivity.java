package com.dufine.circles;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.RadioButton;

/**
 * Created by Dusan on 9.3.2015.
 */
public class SettingsActivity extends ActionBarActivity {
    private RadioButton radioButtonBackgroundBlack;
    private RadioButton radioButtonBackgroundWhite;
    private RadioButton radioButtonSoundOn;
    private RadioButton radioButtonSoundOff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radioButtonBackgroundBlack=(RadioButton) findViewById(R.id.radioButtonBlack);
        radioButtonBackgroundWhite=(RadioButton) findViewById(R.id.radioButtonWhite);
        radioButtonSoundOn=(RadioButton) findViewById(R.id.radioButtonOn);
        radioButtonSoundOff=(RadioButton) findViewById(R.id.radioButtonOff);
    }


    private void change(View view){
        switch (view.getId()){
            case R.id.radioButtonBlack:{
                view.setBackgroundColor(Color.BLACK);
                break;
            }
            case R.id.radioButtonWhite:{
                view.setBackgroundColor(Color.WHITE);
                break;
            }
            case R.id.radioButtonOn:{

            }
            case R.id.radioButtonOff:{

            }
        }
    }
}