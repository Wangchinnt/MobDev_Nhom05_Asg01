package com.mygdx.game;

public interface AndroidAction {
    public void returnMainScreenWithScore(int score, String name);
    public void enableGyro();
    public void disableGyro();
    float getGyroX();
    float getGyroY();
}
