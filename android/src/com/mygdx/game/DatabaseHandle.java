package com.mygdx.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandle extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pointManager";
    private static final int DATABASE_VERSION = 2; //Ver2: add String: name column
    private static final String TABLE_NAME = "pointTable";

    private static final String KEY_ID = "id";
    private static final String KEY_POINT = "point";
    private static final String KEY_NAME = "name";

    public DatabaseHandle(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_students_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT)", TABLE_NAME, KEY_ID, KEY_POINT, KEY_NAME);
        sqLiteDatabase.execSQL(create_students_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i < 2) {
            String drop_students_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
            sqLiteDatabase.execSQL(drop_students_table);
            onCreate(sqLiteDatabase);
        }
    }

    public void addPoint(PlayerScore score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POINT, score.getPoint());
        values.put(KEY_NAME, score.getName());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<PlayerScore> getAllPoints() {
        ArrayList<PlayerScore>  pointList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            PlayerScore score = new PlayerScore(cursor.getInt(0), cursor.getInt(1), cursor.getString(2));
            pointList.add(score);
            cursor.moveToNext();
        }
        return pointList;
    }

    public ArrayList<PlayerScore> getTopHighscores(int limit) {
        ArrayList<PlayerScore> highscores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_POINT + " DESC LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(limit) });

        if (cursor.moveToFirst()) {
            do {
                PlayerScore score = new PlayerScore(cursor.getPosition(), cursor.getInt(1), cursor.getString(2));
//                PlayerScore score = new PlayerScore(cursor.getInt(0), cursor.getInt(1));
                Log.i("CURSOR", "POSITION " + cursor.getPosition() + "POINT " + cursor.getInt(1) + "NAME : " + cursor.getString(2));
                highscores.add(score);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return highscores;
    }

    public void deletePoint(int pointId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(pointId) });
        db.close();
    }

}
