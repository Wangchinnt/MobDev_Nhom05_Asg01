package com.mygdx.game;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BackgroundMusicService extends Service {
    private static MediaPlayer mediaPlayer;
    private static float volume = 1.0f;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.retro_bg_music);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.start();
        mediaPlayer.release();
        super.onDestroy();
    }

    public static void setVolume(float newVol) {
        if (newVol >= 0.0f && newVol <= 1f && mediaPlayer != null) {
            volume = newVol;
            mediaPlayer.setVolume(volume, volume);
        }
    }
}
