package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TelechargementActivity extends AppCompatActivity {

    Button button;
    EditText lien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telechargement);

        button = (Button) findViewById(R.id.btn_tel) ;
        lien = (EditText) findViewById(R.id.link) ;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //téléchargerFichierMP3(lien.getText().toString(),"music telechargé");
                téléchargerFichierMP3("https://file-examples.com/storage/fea9880a616463cab9f1575/2017/11/file_example_MP3_700KB.mp3","music telechargé");
            }
        });



    }

    private void téléchargerFichierMP3(String url, String nomFichier) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Téléchargement de musique");
        request.setDescription(nomFichier);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, nomFichier + ".mp3");

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }

}