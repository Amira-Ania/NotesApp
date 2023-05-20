package com.example.tp1;

import static com.example.tp1.DatabaseHelper.TABLE_FAVORITES;
import static com.example.tp1.DatabaseHelper.COLUMN_TITLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private ListView listView;
    private ArrayList<String> favoriteSongs;
    private ArrayAdapter<String> adapter;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        listView = findViewById(R.id.listViewFav);
        favoriteSongs = new ArrayList<>();

        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getReadableDatabase();

        loadFavoriteSongs();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favoriteSongs);
        listView.setAdapter(adapter);

        if (checkPermission()) {
            loadFavoriteSongs();
        }
//        else {
//            //requestPermission();
//        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favoriteSongs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filePath = getFilePath(position);
                startMusicService(filePath);

                Intent musicPlayerIntent = new Intent(FavoritesActivity.this, MusicPlayer.class);

                musicPlayerIntent.putExtra("musicPath", filePath);
                musicPlayerIntent.putExtra("musicName",  favoriteSongs.get(position));
                musicPlayerIntent.putExtra("index", position);
                musicPlayerIntent.putStringArrayListExtra("songList", favoriteSongs);
                startActivity(musicPlayerIntent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteSongFromFavorites(position);
                return true;
            }
        });

    }



    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

//    private void requestPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//    }

    private void loadFavoriteSongs() {
        Cursor cursor = database.query(TABLE_FAVORITES, new String[]{COLUMN_TITLE}, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String songTitle = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                favoriteSongs.add(songTitle);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    private String getFilePath(int position) {
        String[] projection = {MediaStore.Audio.Media.DATA};
        String selection = MediaStore.Audio.Media.DISPLAY_NAME + "=?";
        String[] selectionArgs = {favoriteSongs.get(position)};
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
                Log.d("FavoritesActivity", "File Path: " + filePath); // Add this debug log
                return filePath;
            }
            cursor.close();
        }
        return null;
    }

    private void deleteSongFromFavorites(int position) {
        String songTitle = favoriteSongs.get(position);

        // Delete the song from the favorites database
        String selection = COLUMN_TITLE + "=?";
        String[] selectionArgs = {songTitle};
        database.delete(TABLE_FAVORITES, selection, selectionArgs);

        // Remove the song from the list and update the adapter
        favoriteSongs.remove(position);
        adapter.notifyDataSetChanged();
        loadFavoriteSongs();
        Toast.makeText(FavoritesActivity.this, "Song deleted from favorites", Toast.LENGTH_SHORT).show();
    }


    private void startMusicService(String filePath) {
        Intent serviceIntent = new Intent(this, MusicPlayerService.class);
        serviceIntent.setAction("PLAY");
        serviceIntent.putExtra("FILE_PATH", filePath);
        serviceIntent.putExtra("songList", favoriteSongs);
        startService(serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadFavoriteSongs();
            } else {
                System.out.println("Permission denied");
            }
        }
    }
}
