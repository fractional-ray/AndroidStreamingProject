package com.example.evan.seniorproject.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.provider.SyncStateContract;

import java.util.List;

/**
 * Created by Evan on 3/29/2018.
 */

@Dao
public interface SongDAO {

    @Query("SELECT * FROM songs")
    List<Song> getAll();

    @Query("SELECT * FROM songs WHERE album LIKE :album")
    List<Song> getAlbum(String album);

    @Query("SELECT COUNT(*) from songs")
    int songCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Song... songs);
}
