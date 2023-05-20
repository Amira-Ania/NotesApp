package com.example.tp1;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MusicActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_TITLE = "title";

    private ArrayList<String> songList;
    private ListView listView;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        listView = findViewById(R.id.listView);
        songList = new ArrayList<>();

        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();


        if (checkPermission()) {
            loadSongs();
        } else {
            requestPermission();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_item1:
                startActivity(new Intent(this, TelechargementActivity.class));
                return true;
            case R.id.menu_item2:
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void loadSongs() {
        String[] projection = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
        );

        if (cursor != null && cursor.moveToFirst()) {
            int filePathColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int displayNameColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);

            if (filePathColumnIndex != -1 && displayNameColumnIndex != -1) {
                do {
                    String filePath = cursor.getString(filePathColumnIndex);
                    String displayName = cursor.getString(displayNameColumnIndex);
                    songList.add(displayName);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filePath = getFilePath(position);
                startMusicService(filePath);

                Intent musicPlayerIntent = new Intent(MusicActivity.this, MusicPlayer.class);
                musicPlayerIntent.putExtra("musicPath", filePath);
                musicPlayerIntent.putExtra("musicName",  songList.get(position));
                musicPlayerIntent.putExtra("index", position);
                musicPlayerIntent.putStringArrayListExtra("songList", songList);
                startActivity(musicPlayerIntent);
            }
        });
    }

    private String getFilePath(int position) {
        String[] projection = {MediaStore.Audio.Media.DATA};
        String selection = MediaStore.Audio.Media.DISPLAY_NAME + "=?";
        String[] selectionArgs = {songList.get(position)};
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int filePathColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            if (filePathColumnIndex != -1) {
                String filePath = cursor.getString(filePathColumnIndex);
                cursor.close();
                return filePath;
            }
            cursor.close();
        }
        return null;
    }

    private void startMusicService(String filePath) {
        Intent serviceIntent = new Intent(this, MusicPlayerService.class);
        serviceIntent.setAction("PLAY");
        serviceIntent.putExtra("FILE_PATH", filePath);
        serviceIntent.putStringArrayListExtra("songList", songList); // Add songList as an extra
        startService(serviceIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSongs();
            } else {
                System.out.println("Permission denied");
            }
        }
    }
}
