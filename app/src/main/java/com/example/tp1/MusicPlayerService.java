package com.example.tp1;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private int currentSongIndex = 0;

    private ArrayList<String> songList;

    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    String currentSongName;


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getAction() != null) {
                switch (intent.getAction()) {
                    case "PLAY":
                        String filePath = intent.getStringExtra("FILE_PATH");
                        songList = intent.getStringArrayListExtra("songList");
                        playMusic(filePath);
                        break;
                    case "PAUSE":
                        pauseMusic();
                        break;
                    case "NEXT":
                        nextMusic();
                        break;
                    case "STOP":
                        stopMusic();
                        break;
                    case "PREVIOUS":
                        previousMusic();
                        break;

                }
            }
        }
        return START_NOT_STICKY;
    }

    private void playMusic(String filePath) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            currentSongIndex = songList.indexOf(filePath);
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
            startForeground(NOTIFICATION_ID, createNotification());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }



    private void nextMusic() {
        String nextSongPath = getNextSongPath();
        playMusic(nextSongPath);
    }

    private void previousMusic() {
        String previousSongPath = getPreviousSongPath();
        playMusic(previousSongPath);
    }



    private void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            updateNotification();
        } else {
            mediaPlayer.start();
            isPlaying = true;
            updateNotification();
        }
    }

    private void stopMusic() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        isPlaying = false;
        stopForeground(true);
        stopSelf();
    }


    private String getNextSongPath() {
        int nextSongIndex = (currentSongIndex + 1) % songList.size();
        return songList.get(nextSongIndex);
    }

    private String getPreviousSongPath() {
        int previousSongIndex = (currentSongIndex - 1 + songList.size()) % songList.size();
        return songList.get(previousSongIndex);
    }


    private Notification createNotification() {
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_layout);

        Intent pauseIntent = new Intent(this, MusicPlayerService.class);
        pauseIntent.setAction("PAUSE");
        PendingIntent pausePendingIntent = PendingIntent.getService(this, 0, pauseIntent, 0);

        Intent nextIntent = new Intent(this, MusicPlayerService.class);
        nextIntent.setAction("NEXT");
        PendingIntent nextPendingIntent = PendingIntent.getService(this, 0, nextIntent, 0);

        Intent stopIntent = new Intent(this, MusicPlayerService.class);
        stopIntent.setAction("STOP");
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, 0);

        Intent previousIntent = new Intent(this, MusicPlayerService.class);
        previousIntent.setAction("PREVIOUS");
        PendingIntent previousPendingIntent = PendingIntent.getService(this, 0, previousIntent, 0);


        // Get the current song name from your song list based on the currentSongIndex
        if (currentSongIndex >= 0 && currentSongIndex < songList.size()) {
            currentSongName = songList.get(currentSongIndex);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle("Music Player")
                .setContentText("Playing music: " + currentSongName)
                .setSmallIcon(R.drawable.ic_music_note)
                .setOngoing(true)
                .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
                .addAction(R.drawable.ic_pause, "Pause/Play", pausePendingIntent)
                .addAction(R.drawable.ic_next, "Next", nextPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true);
        return builder.build();
    }

    private void updateNotification() {
        Notification notification = createNotification();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}
