package com.dufine.circles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Dusan on 29.3.2015.
 */
public class ResultActivity extends ActionBarActivity {

    private TextView tvScore, tvHighScore;
    private ImageButton btnBackToMenu;
    private ImageButton btnTryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        init();
        setInfo();
    }

    // Controlling back button
    @Override
    public void onBackPressed() {
        Intent i = new Intent(ResultActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void init() {
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvHighScore = (TextView) findViewById(R.id.tvHighScore);
        btnBackToMenu = (ImageButton) findViewById(R.id.btnBackToMenu);
        btnTryAgain = (ImageButton) findViewById(R.id.btnTryAgain);

        setButtonListeners();
    }

    private void setButtonListeners() {
        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this, CircleActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void setInfo() {
        // Getting current score
        int level = getIntent().getIntExtra(CircleView.LEVEL_COUNT, 0);
        int count = getIntent().getIntExtra(CircleView.CIRCLES_COUNT, 0) + 1;

        // Setting and getting high score
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int levelHigh = sharedPref.getInt(getString(R.string.high_score_pref_level), -1);
        int countHigh = sharedPref.getInt(getString(R.string.high_score_pref_count), -1);

        if (levelHigh == -1) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.high_score_pref_level), level);
            editor.putInt(getString(R.string.high_score_pref_count), count);
            editor.commit();
        } else if (levelHigh < level || (levelHigh == level && countHigh > count)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.high_score_pref_level), level);
            editor.putInt(getString(R.string.high_score_pref_count), count);
            editor.commit();
        }

        // Updating info
        String score = "Level: " + Integer.toString(level) + "; Circles left: " + Integer.toString(count);
        tvScore.setText(score);
        score = "Level: " + Integer.toString(sharedPref.getInt(getString(R.string.high_score_pref_level), -1)) + "; Circles left: " + Integer.toString(sharedPref.getInt(getString(R.string.high_score_pref_count), -1));
        tvHighScore.setText(score);

    }

}
