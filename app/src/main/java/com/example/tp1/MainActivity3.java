package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tp1.databinding.ActivityMain3Binding;

import java.util.Locale;

public class MainActivity3 extends AppCompatActivity {

    ActivityMain3Binding binding;
    ImageView audio;
    TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = this.getIntent();
        if (intent != null) {
            String titre = intent.getStringExtra("title");
            String des = intent.getStringExtra("desc");
            String time = intent.getStringExtra("time");
            int image = intent.getIntExtra("img", R.drawable.writing);


            binding.txtTitle.setText(titre);
            binding.txtDesc.setText(des);
            binding.txtTime.setText(time);
            binding.image.setImageResource(image);

        }

        audio = findViewById(R.id.audio);
        boolean textToSpeechIsInitialized = false;
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if (i == TextToSpeech.SUCCESS) {
                            textToSpeech.setLanguage(Locale.ENGLISH);
                            textToSpeech.speak(binding.txtDesc.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                        else {
                            Log.e("TTS","failed to speak");
                        }
                    }

                });
            }

        });
    }
}


