package com.mygdx.game;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class PreGameScreen extends AppCompatActivity {
    private Spinner spinnerContact;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String playerName = "You";
    private Button button;
    private int aircraftModel;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.prepare_layout);
        this.spinnerContact = findViewById(R.id.spinner);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ArrayList<String> contactList;
        contactList = getContactList();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contactList);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinnerContact.setAdapter(arrayAdapter);
        this.spinnerContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                playerName = arrayAdapter.getItem(i);
                Log.i("SPINNER SELECTED ","ITEM SELECTED " + playerName );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                playerName = "YOU";
            }
        });

        button = findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayGame(view);
            }
        });
    }

    private ArrayList<String> getContactList() {
        ArrayList<String> contactList = new ArrayList<>();
        contactList.add("YOU");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, 0);
        }

        ContentResolver contentResolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(uri, null, null, null, null);
        Log.i("Cursor status", "Count : " + Integer.toString(cursor.getCount()));
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                Log.i("ABCDE", "Contact name : " + contactName);
                contactList.add(contactName);
            }
        }
        return contactList;
    }

    private void PlayGame(View v) {
        Intent intent = new Intent(this, AndroidLauncher.class);
        intent.putExtra("name", playerName);
        intent.putExtra("model", 2);
        startActivity(intent);
    }

}
