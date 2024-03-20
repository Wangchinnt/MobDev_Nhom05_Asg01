package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class AndroidLauncher extends AndroidApplication implements AndroidAction{
	private boolean isGameRunning = false;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		config.useAccelerometer = false;
		config.useCompass = false;

		initialize(new MyGdxGame(this), config);
	}

	@Override
	public void returnMainScreen() {
		Intent intent = new Intent(this, TestClass.class);
		startActivity(intent);
	}

	@Override
	public void returnMainScreenWithScore(int score) {
		Intent intent = new Intent(this, TestClass.class);
		intent.putExtra("score", score);
		startActivity(intent);
	}
}
