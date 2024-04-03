package com.mygdx.game;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class PreGameScreen extends AppCompatActivity {
    private Spinner spinnerContact;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String playerName = "You";
    private SharedPreferences sharedPreferences;
    private Button button;
    private RadioGroup radioGroup;
    private ImageView imageView;
    private static final int RADIO_BUTTON_1_ID = R.id.radioButton;
    private static final int RADIO_BUTTON_2_ID = R.id.radioButton2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String IMAGE_FILE_NAME = "custombg.png";
    private String customPath = null;
    private int aircraftModel = 1;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.prepare_layout);
//        this.spinnerContact = findViewById(R.id.spinner);
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);
        radioGroup = findViewById(R.id.radioGr);
        imageView = findViewById(R.id.imageView);

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        playerName = sharedPreferences.getString("username", "None");

//        ArrayList<String> contactList;
//        contactList = getContactList();

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contactList);
//
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        this.spinnerContact.setAdapter(arrayAdapter);
//        this.spinnerContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                playerName = arrayAdapter.getItem(i);
//                Log.i("SPINNER SELECTED ","ITEM SELECTED " + playerName );
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                playerName = "YOU";
//            }
//        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        aircraftModel = 2;
                        break;
                    case 2:
                        aircraftModel = 3;
                        break;
                    default: aircraftModel = 1;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        button = findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayGame(view);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == RADIO_BUTTON_1_ID) {
                    customPath = null;
                    Toast.makeText(getApplicationContext(), "fsfs 1", Toast.LENGTH_SHORT).show();
                } else if (i == RADIO_BUTTON_2_ID) {
                    dispatchTakePictureIntent();
                    Toast.makeText(getApplicationContext(), "fsfs 2", Toast.LENGTH_SHORT).show();
                }

            }
        });

        imageView.setImageResource(R.drawable.background);
    }

//    private ArrayList<String> getContactList() {
//        ArrayList<String> contactList = new ArrayList<>();
//        contactList.add("YOU");
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, 0);
//        }
//
//        ContentResolver contentResolver = getContentResolver();
//        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, null, null, null);
//        assert cursor != null;
//        Log.i("Cursor status", "Count : " + cursor.getCount());
//        if (cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                Log.i("ABCDE", "Contact name : " + contactName);
//                contactList.add(contactName);
//            }
//        }
//        return contactList;
//    }

    private void PlayGame(View v) {
        BackgroundMusicService.pauseMusic();
        Intent intent = new Intent(this, AndroidLauncher.class);
        intent.putExtra("name", playerName);
        intent.putExtra("model", aircraftModel);
        intent.putExtra("path", customPath);
        startActivity(intent);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 0);
        }
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            assert imageBitmap != null;
            customPath = saveImage(getApplicationContext(), imageBitmap);
            Toast.makeText(getApplicationContext(), "Saved image wwith path: " + customPath, Toast.LENGTH_SHORT);
        }
    }

    private String saveImage(Context context, Bitmap imgBitmap) {
        String imagePath = null;
        File cacheDir = context.getCacheDir();
        File imageFile = new File(cacheDir, IMAGE_FILE_NAME);
        try {
            OutputStream outputStream = new FileOutputStream(imageFile);
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            imagePath = imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }
}
