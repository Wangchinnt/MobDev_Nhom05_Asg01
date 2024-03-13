package com.mygdx.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class TestClass extends Activity {
    private Button mainBtn;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        Intent myIntent = new Intent(this, AndroidLauncher.class);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.test_layout);

        mainBtn = (Button) findViewById(R.id.button);

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(myIntent);
            }
        });
    }


}
