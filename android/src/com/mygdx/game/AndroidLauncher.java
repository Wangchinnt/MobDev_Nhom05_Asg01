package com.mygdx.game;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class AndroidLauncher extends AndroidApplication implements AndroidAction, SensorEventListener {
	private SensorManager sensorManager;
	private Sensor gyroSensor;
	private float gyroX, gyroY;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		config.useAccelerometer = false;
		config.useCompass = false;
		config.useGyroscope = true;

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
			gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		} else {
			Toast.makeText(this, "Thiết bị không hỗ trợ gyroscope!", Toast.LENGTH_SHORT).show();
		}

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

	@Override
	public void enableGyro() {
		sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void disableGyro() {
		sensorManager.unregisterListener(this);
	}

	@Override
	public float getGyroX() {
		return gyroX;
	}

	@Override
	public float getGyroY() {
		return gyroY;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			gyroX = event.values[0];
			gyroY = event.values[1];
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

}
