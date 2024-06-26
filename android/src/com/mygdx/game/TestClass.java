package com.mygdx.game;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestClass extends Activity {
    private Button mainBtn;
    private DatabaseHandle databaseHandle;
    private Button exitBtn;
    private Button highBtn;
    private Button communityBtn;
    private Button loginButton;
    private Button signupButotn;
    private Button settingBtn;
    private Button aboutbtn;
    private TextView usernameView;
    private TextView gameTitle;
    private ImageView imageView;
    private Typeface typeface;
    private SharedPreferences sharedPreferences;
    private static final int REQUEST_SELECT_IMAGE = 100;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        Intent myIntent2 = new Intent(this, PreGameScreen.class);
        Intent musicIntent = new Intent(this, BackgroundMusicService.class);
        startService(musicIntent);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.test_layout);
        typeface = Typeface.createFromAsset(getAssets(), "thaleah.ttf");

        mainBtn = findViewById(R.id.button);
        mainBtn.setTypeface(typeface);
        highBtn = findViewById(R.id.button2);
        highBtn.setTypeface(typeface);
        exitBtn = findViewById(R.id.button3);
        exitBtn.setTypeface(typeface);
        communityBtn = findViewById(R.id.button6);
        communityBtn.setTypeface(typeface);
        loginButton = findViewById(R.id.button_login);
        loginButton.setTypeface(typeface);
        signupButotn = findViewById(R.id.button_signup);
        signupButotn.setTypeface(typeface);
        aboutbtn = findViewById(R.id.button_about);
        aboutbtn.setTypeface(typeface);
        settingBtn = findViewById(R.id.button5);
        settingBtn.setTypeface(typeface);
        gameTitle = findViewById(R.id.textView2);
        gameTitle.setTypeface(typeface);
        usernameView = findViewById(R.id.user_name);
        usernameView.setTypeface(typeface);

        imageView = findViewById(R.id.user_image);
        databaseHandle = new DatabaseHandle(this);

        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        this.registerForContextMenu(imageView);

        String name = sharedPreferences.getString("username", null);
        boolean logged_in = sharedPreferences.getBoolean("is_logged_in", false);
        String avatarFile = "avatar_" + name + ".png";

        if (logged_in) {
            loginButton.setVisibility(View.GONE);
            signupButotn.setVisibility(View.GONE);
            usernameView.setText("Hi, " + name);
            File file = new File(getFilesDir(), avatarFile);

            if (file.exists()) {
                Uri avatarUri = Uri.fromFile(file);
                imageView.setImageURI(avatarUri);
            } else {
                imageView.setImageResource(R.drawable.user);
            }
        } else {
            imageView.setVisibility(View.GONE);
            usernameView.setVisibility(View.GONE);
        }

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logged_in) {
                    startActivity(myIntent2);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Sign In First", Toast.LENGTH_SHORT).show();
                }
            }
        });

        highBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(TestClass.this, ScoreView.class);
                startActivity(intent1);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BackgroundMusicService.class);
                stopService(intent);
                finishAffinity();
            }
        });

        communityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });

        signupButotn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignupAnalog();
            }
        });

        aboutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                startActivity(intent);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(imageView.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.edit_avatar) {
                            Intent photoPickIntent = new Intent(Intent.ACTION_PICK);
                            photoPickIntent.setType("image/*");
                            startActivityForResult(photoPickIntent, REQUEST_SELECT_IMAGE);
                            return true;
                        } else if (menuItem.getItemId() == R.id.log_out) {
                            logoutUser();
                            return true;
                        } else return true;
                    }
                });
                popupMenu.show();
//                openContextMenu(view);
            }
        });
    }
    public void openSetting(View v) {
        Intent i = new Intent(this, SettingActivity.class);
        startActivity(i);
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SIGN IN");
        builder.setIcon(R.drawable.user);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editText = new EditText(this);
        editText.setHint("User Name");
        editText.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.CENTER;
        editText.setLayoutParams(layoutParams);
        layout.addView(editText);

        builder.setView(layout);
        builder.setPositiveButton("SignIn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username = editText.getText().toString();

                if (databaseHandle.checkUser(username)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putBoolean("is_logged_in", true);
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                    recreate();
                } else {
                    Toast.makeText(getApplicationContext(), "User name does not exist !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background2);
        dialog.show();
    }

    private void showSignupAnalog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SIGN UP");
        builder.setIcon(R.drawable.user);


        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editText = new EditText(this);
        editText.setHint("User Name");
        editText.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.CENTER;
        editText.setLayoutParams(layoutParams);

        layout.addView(editText);

        builder.setView(layout);
        builder.setPositiveButton("SignUp", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username = editText.getText().toString();
                String response = databaseHandle.addUser(username);
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background2);
        dialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_avatar) {
            Intent photoPickIntent = new Intent(Intent.ACTION_PICK);
            photoPickIntent.setType("image/*");
            startActivityForResult(photoPickIntent, REQUEST_SELECT_IMAGE);
            return true;
        } else if (item.getItemId() == R.id.log_out) {
            logoutUser();
            return true;
        } else return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
                saveImageToInternalStorage(selectedImageUri);
            }
        }
    }

    private void saveImageToInternalStorage(Uri selectedImageUri) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        String name = sharedPreferences.getString("username", "");
        String fileName = "avatar_" + name + ".png";
        File newFile = new File(getFilesDir(), fileName);
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            FileOutputStream outputStream = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            Uri avatarUri = Uri.fromFile(newFile);
            imageView.setImageURI(avatarUri);
            Toast.makeText(this, "Avatar saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving avatar", Toast.LENGTH_SHORT).show();
        }
    }

    private void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("is_logged_in");
        editor.apply();

        recreate();
    }
}
