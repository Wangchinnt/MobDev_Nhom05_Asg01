package com.mygdx.game;

public class PlayerScore {
    int id;
    int point;
    public PlayerScore(int id, int point) {
        this.id = id;
        this.point = point;
    }

    public PlayerScore(int point) {
        this.point = point;
    }

    public int getId() {
        return id;
    }

    public int getPoint() {
        return point;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
