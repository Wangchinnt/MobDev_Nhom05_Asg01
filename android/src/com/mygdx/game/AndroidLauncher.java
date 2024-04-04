package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class AndroidLauncher extends AndroidApplication implements AndroidAction {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		config.useAccelerometer = false;
		config.useCompass = false;
		config.useGyroscope = true;

		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		int model = intent.getIntExtra("model", 1);
		String path = intent.getStringExtra("path");

		Log.i("LAUNCHER", "Name : " + name + " Model : " + model + " Path : " + path);
		initialize(new MyGdxGame(this, name, model, path), config);
	}


	@Override
	public void returnMainScreenWithScore(int score, String name) {
		BackgroundMusicService.continueMusic();
		Intent intent = new Intent(this, AfterGameScreen.class);
		intent.putExtra("score", score);
		intent.putExtra("name", name);
		startActivity(intent);
		finish();
	}
}
