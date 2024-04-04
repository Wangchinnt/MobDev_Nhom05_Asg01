package com.mygdx.game;

public class PlayerScore {
    int id;
    String name;
    int point;
    double latitude;
    double longitude;
    String message;
    public PlayerScore(int id, int point, String name) {
        this.id = id;
        this.point = point;
        this.name = name;
    }
    public PlayerScore(int id, int point, String name, double latitude, double longitude, String message) {
        this.id = id;
        this.point = point;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
    }

    public PlayerScore(int point, String name) {
        this.point = point;
        this.name = name;
    }

    public PlayerScore(int point, String name, double latitude, double longitude, String message) {
        this.point = point;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = message;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getMessage() {
        return message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
