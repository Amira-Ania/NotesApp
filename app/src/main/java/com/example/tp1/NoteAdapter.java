//package com.example.tp1;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Context;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.text.DateFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//public class NoteAdapter extends ArrayAdapter<Note> {
//    public NoteAdapter(Context context, ArrayList<Note> notes) {
//        super(context, 0, notes);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // Get the data item for this position
//        Note note = getItem(position);
//        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_item, parent, false);
//        }
//        // Lookup view for data population
//        TextView titre = (TextView) convertView.findViewById(R.id.titre);
//        TextView desc = (TextView) convertView.findViewById(R.id.description);
//        ImageView img = (ImageView) convertView.findViewById(R.id.img);
//        // Populate the data into the template view using the data object
//        titre.setText(note.getTitre());
//        desc.setText(note.getDescription());
//        img.setImageResource(note.getImgUrl());
//        // Return the completed view to render on screen
//        return convertView;
//    }
//
//}
//
//
