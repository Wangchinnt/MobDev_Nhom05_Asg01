package com.mygdx.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ScoreView extends Activity {
    private ListView listView;
    private DatabaseHandle databaseHandle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_view);

        listView = findViewById(R.id.list_view);
        databaseHandle = new DatabaseHandle(this);

        ArrayList<PlayerScore> highscores = databaseHandle.getTopHighscores(10 );
        ScoreArrayAdapter adapter = new ScoreArrayAdapter(this, highscores);

        listView.setAdapter(adapter);

        Button button = findViewById(R.id.button_back);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
