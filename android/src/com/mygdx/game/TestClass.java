package com.mygdx.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class TestClass extends Activity {
    private Button mainBtn;
    private DatabaseHandle databaseHandle;
    private Button exitBtn;
    private Button highBtn;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        Intent myIntent = new Intent(this, AndroidLauncher.class);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.test_layout);

        mainBtn = findViewById(R.id.button);
        highBtn = findViewById(R.id.button2);
        exitBtn = findViewById(R.id.button3);

        databaseHandle = new DatabaseHandle(this);

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        PlayerScore playerScore = new PlayerScore(score);
        databaseHandle.addPoint(playerScore);

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(myIntent);
            }
        });

        highBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(TestClass.this, ScoreView.class);
                startActivity(intent1);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestClass.this.finish();
                System.exit(0);
            }
        });
    }
}
