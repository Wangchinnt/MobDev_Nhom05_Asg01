package com.mygdx.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ScoreArrayAdapter extends ArrayAdapter<PlayerScore> {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<PlayerScore> mScores;
    public ScoreArrayAdapter(@NonNull Context context, List<PlayerScore> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mScores = objects;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlayerScore playerScore = mScores.get(position);

        convertView = mLayoutInflater.inflate(R.layout.score_item, parent, false);

        ((TextView) convertView.findViewById(R.id.textViewTop)).setText("TOP " + String.format("%d :", playerScore.getId() + 1));
        ((TextView) convertView.findViewById(R.id.textViewName)).setText(playerScore.getName());
        ((TextView) convertView.findViewById(R.id.textViewScore)).setText(String.format("%d", playerScore.getPoint()));


        return convertView;
    }
}
