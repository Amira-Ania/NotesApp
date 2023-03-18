package com.example.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import com.example.tp1.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    ArrayList<Note> notes = new ArrayList<>();

    ArrayAdapter<Note> arrayAdapter;
    String truncatedText;

    public void showPopup(View view) {
        Dialog dialog = new Dialog(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_popup, null);

        View View1 = getLayoutInflater().inflate(R.layout.activity_main, null);

        EditText titre = dialogView.findViewById((R.id.titre));
        EditText description = dialogView.findViewById((R.id.description));

        // Inflate the dialog layout
        dialog.setContentView(dialogView);

        // Set the size and position of the dialog
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // Set the background dim amount of the dialog
        dialog.getWindow().setDimAmount(0.7f);

        Button addButton = dialogView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notetitre = titre.getText().toString();
                String notedesc = description.getText().toString();

                //long createdTime = System.currentTimeMillis();
                int maxLength = 10;
                 Note n= new Note();
                 n.setTitre(notetitre);
                 n.setImgUrl(R.drawable.writing);
                 String date = java.text.DateFormat.getDateTimeInstance().format(new Date());
                 n.setCreationTime(date);
                 n.setDescription(notedesc);

//                if (notedesc.length() > maxLength) {
//                    truncatedText = TextUtils.substring(notedesc, 0, maxLength) + "...";
//                    n.setDescription(truncatedText);
//                }

                notes.add(n);

                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.show();



    }


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ListView listView = (ListView) findViewById(R.id.list);

        arrayAdapter = new ArrayAdapter<Note>(this, R.layout.single_item, R.id.textView2, notes);
        binding.list.setAdapter(arrayAdapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int item = i;
                new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to delete this note ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(item);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("No", null).show();
                return true;
            }
        });


        binding.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);

                intent.putExtra("title", notes.get(i).getTitre());
                intent.putExtra("desc", notes.get(i).getDescription());
                intent.putExtra("time", notes.get(i).getCreationTime());
                intent.putExtra("img", notes.get(i).getImgUrl());
                startActivity(intent);

            }
        });

    }

}


