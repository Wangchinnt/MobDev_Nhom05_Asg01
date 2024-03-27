package com.mygdx.game;

public class PlayerScore {
    int id;
    String name;
    int point;
    public PlayerScore(int id, int point, String name) {
        this.id = id;
        this.point = point;
        this.name = name;
    }

    public PlayerScore(int point, String name) {

        this.point = point;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
