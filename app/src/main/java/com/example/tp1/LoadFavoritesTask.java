//package com.example.tp1;
//
//import static com.example.tp1.DatabaseHelper.TABLE_FAVORITES;
//
//import android.database.Cursor;
//
//import java.time.DayOfWeek;
//import java.util.ArrayList;
//import java.util.List;
//
//private class LoadFavoritesTask extends AsyncTask<Void, Void, List<Song>> {
//    private static final Object TABLE_FAVORITES = ;
//
//    @Override
//    protected List<String> doInBackground(Void... voids) {
//        List<String> favorites = new ArrayList<>();
//
//
//        Cursor cursor = database.query(TABLE_FAVORITES, null, null, null, null, null, null);
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
//                String artist = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST));
//                String path = cursor.getString(cursor.getColumnIndex(COLUMN_PATH));
//
//                Song song = new Song(title, artist, path);
//                favorites.add(song);
//            } while (cursor.moveToNext());
//
//            cursor.close();
//        }
//
//        return favorites;
//    }
//
//    @Override
//    protected void onPostExecute(List<Song> favorites) {
//        // Update UI with the list of favorite songs
//        // For example, display the favorites in a separate list or update the song list adapter
//    }
//}
