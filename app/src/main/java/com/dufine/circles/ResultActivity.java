package com.dufine.circles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Dusan on 29.3.2015.
 */
public class ResultActivity extends ActionBarActivity {

    private TextView tvScore;
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
        String level = Integer.toString(getIntent().getIntExtra(CircleView.LEVEL_COUNT, 1));
        tvScore.setText(level);
    }


}
