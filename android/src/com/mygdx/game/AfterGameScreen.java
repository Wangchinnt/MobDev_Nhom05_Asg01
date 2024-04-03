package com.mygdx.game;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class AfterGameScreen extends AppCompatActivity {
    //TODO: Nếu có thể thì không save ở ngay khi kết thúc game nữa mà sẽ gọi intent với extra qua đây trước
    private final int FINE_PERMISSION_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextInputEditText textInputEditText;
    private DatabaseHandle databaseHandle;
    private Button shareButton;
    private Button skipButton;
    private String playerName;
    private String playerMessage;
    private int score;
    private double latitude;
    private double longitude;
    private PlayerScore playerScoreLocation;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.after_screen);

        shareButton = findViewById(R.id.button7);
        skipButton = findViewById(R.id.button8);
        textInputEditText = findViewById(R.id.editText);
        textInputEditText.setText("");
        databaseHandle = new DatabaseHandle(getApplicationContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        shareButton.setEnabled(false);

        Intent intent = getIntent();
        playerName = intent.getStringExtra("name");
        score = intent.getIntExtra("score", 0);

        databaseHandle.addPoint(new PlayerScore(score, playerName));

        getLocation();

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TestClass.class);
                playerScoreLocation.setMessage(textInputEditText.getText().toString());
                databaseHandle.addPointWithLocation(playerScoreLocation);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Shared your score! Access Community Page to see", Toast.LENGTH_SHORT).show();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TestClass.class);
                startActivity(intent);
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        shareButton.setEnabled(true);
                        shareButton.setText("Share");
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            playerScoreLocation = new PlayerScore(score, playerName, latitude, longitude, playerMessage);
                            Log.i("AFTER DATA: ", "Name : " + playerName + " Score : " + score + "Lat : " + String.valueOf(latitude) + " Long : " + String.valueOf(longitude));
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Location permission is denied, please allow the permission!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
