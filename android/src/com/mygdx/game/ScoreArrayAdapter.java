package com.mygdx.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.List;

public class ScoreArrayAdapter extends ArrayAdapter<PlayerScore> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<PlayerScore> mScores;
    public ScoreArrayAdapter(@NonNull Context context, List<PlayerScore> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.mScores = objects;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlayerScore playerScore = mScores.get(position);
        String avatarFile = "avatar_" + playerScore.getName() + ".png";

        File file = new File(mContext.getFilesDir(), avatarFile);

        convertView = mLayoutInflater.inflate(R.layout.score_item_const, parent, false);

        ((TextView) convertView.findViewById(R.id.text_top)).setText("TOP " + String.format("%d  ", playerScore.getId() + 1));
        ((TextView) convertView.findViewById(R.id.text_name)).setText(playerScore.getName());
        if (file.exists()) {
            String path = file.getAbsolutePath();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ((ImageView) convertView.findViewById(R.id.avatar_score_board)).setImageBitmap(bitmap);
        } else {
            ((ImageView) convertView.findViewById(R.id.avatar_score_board)).setImageResource(R.drawable.user);
        }
        ((TextView) convertView.findViewById(R.id.text_score)).setText(String.format("%d", playerScore.getPoint()));


        return convertView;
    }
}
