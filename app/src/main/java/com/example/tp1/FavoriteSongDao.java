//package com.example.tp1;
//
//import androidx.lifecycle.LiveData;
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//
//import java.util.List;
//
//@Dao
//public interface FavoriteSongDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertFavoriteSong(FavoriteSongEntity favoriteSong);
//
//    @Delete
//    void deleteFavoriteSong(FavoriteSongEntity favoriteSong);
//
//    @Query("SELECT * FROM favorites")
//    LiveData<List<FavoriteSongEntity>> getAllFavoriteSongs();
//}
