package com.mygdx.game;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mygdx.game.databinding.ActivityCommunityBinding;

import java.io.File;
import java.util.ArrayList;

public class CommunityActivity extends FragmentActivity implements OnMapReadyCallback {
    //TODO : Toa do la 7 chu so sau dau phay

    private GoogleMap mMap;
    private ActivityCommunityBinding binding;
    private DatabaseHandle databaseHandle;
    private ArrayList<PlayerScore> playerScoreArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCommunityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        databaseHandle = new DatabaseHandle(getApplicationContext());

        playerScoreArrayList = databaseHandle.getPointsWithLocation();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng maidich = new LatLng(	21.04041, 105.77000);
        LatLng tmu = new LatLng(	21.03677, 105.77502);
        mMap.addMarker(new MarkerOptions().position(maidich).title("Mai dich nghia trang"));
        mMap.addMarker(new MarkerOptions().position(tmu).title("Dai hoc thuong mai siu")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        for (PlayerScore playerScore:playerScoreArrayList) {
            String fileName = "avatar_" + playerScore.getName() + ".png";
            File file = new File(getFilesDir(), fileName);

            if (file.exists()) {
                String imagePath = file.getAbsolutePath();
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                Bitmap scaleBit = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(scaleBit);
                mMap.addMarker(new MarkerOptions().position(new LatLng(playerScore.getLatitude(),
                        playerScore.getLongitude())).title(playerScore.getMessage()).icon(bitmapDescriptor));
            } else {
                mMap.addMarker(new MarkerOptions().position(new LatLng(playerScore.getLatitude(),
                        playerScore.getLongitude())).title(playerScore.getMessage()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tmu));
    }
}