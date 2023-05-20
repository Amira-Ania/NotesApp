package com.example.tp1;

import static com.example.tp1.DatabaseHelper.TABLE_FAVORITES;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity {

    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_TITLE = "title";

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private ImageView imagePause;
    private ImageView imagePlay;
    private ImageView imageNext;
    private ImageView imagePrev;
    private ImageView heart;
    private TextView title;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private int currentSongIndex;
    private ArrayList<String> songList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        imagePause = findViewById(R.id.image_pause);
        imagePlay = findViewById(R.id.image_play);
        imageNext = findViewById(R.id.image_next);
        imagePrev = findViewById(R.id.image_previous);
        title = findViewById(R.id.id_title);
        heart = findViewById(R.id.image_heart);

        Intent intent = getIntent();
        String musicPath = intent.getStringExtra("musicPath");
        String musicName = intent.getStringExtra("musicName");
        currentSongIndex = intent.getIntExtra("index", 0);
        songList = intent.getStringArrayListExtra("songList");

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(musicPath);
            title.setText(musicName);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMusic();
            }
        });

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heart.setImageResource(R.drawable.ic_heart_filled);
                addSongToFavorites(musicName);

            }
        });


        imagePause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMusic();
            }
        });

        imageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });

        imagePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousSong();
            }
        });
    }

    private void startMusic() {
        // Start the music playback logic
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
            imagePlay.setVisibility(View.GONE);
            imagePause.setVisibility(View.VISIBLE);
        }
    }

    private void pauseMusic() {
        // Pause the music playback logic
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            imagePlay.setVisibility(View.VISIBLE);
            imagePause.setVisibility(View.GONE);
        }
    }
    private void nextSong() {
        if (currentSongIndex < songList.size() - 1) {
            currentSongIndex++;
            String nextSongPath = songList.get(currentSongIndex);
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                Toast.makeText(this, nextSongPath, Toast.LENGTH_SHORT).show();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
                title.setText(songList.get(currentSongIndex));
                mediaPlayer.setDataSource(nextSongPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying = true;
                imagePlay.setVisibility(View.GONE);
                imagePause.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No next song available", Toast.LENGTH_SHORT).show();
        }
    }

    private void previousSong() {
        if (currentSongIndex > 0) {
            currentSongIndex--;
            String prevSongPath = songList.get(currentSongIndex);
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                Toast.makeText(this, prevSongPath, Toast.LENGTH_SHORT).show();
                mediaPlayer.reset();
                mediaPlayer.setDataSource(prevSongPath);
                title.setText(songList.get(currentSongIndex));
                mediaPlayer.prepare();
                mediaPlayer.start();
                isPlaying = true;
                imagePlay.setVisibility(View.GONE);
                imagePause.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No previous song available", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSongToFavorites(String songName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, songName);
        // Add more columns and values as required for your database table

        long newRowId = database.insert(TABLE_FAVORITES, null, values);

        if (newRowId != -1) {
            // Successfully added to favorites
            Toast.makeText(this, "Song added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            // Failed to add to favorites
            Toast.makeText(this, "Failed to add song to favorites", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
