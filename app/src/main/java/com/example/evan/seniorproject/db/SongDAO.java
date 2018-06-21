package com.example.evan.seniorproject.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.provider.SyncStateContract;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 3/29/2018.
 */

@Dao
public interface SongDAO {

    @Query("SELECT * FROM songs ORDER BY song_name ASC")
    List<Song> getAll();

    @Query("SELECT DISTINCT artist FROM songs WHERE (artist IS NOT NULL) ORDER BY artist ASC")
    List<String> getUniqueArtists();

    @Query("SELECT DISTINCT album FROM songs ORDER BY album ASC")
    List<String> getUniqueAlbums();

    @Query("SELECT DISTINCT genre FROM songs ORDER BY genre ASC")
    List<String> getUniqueGenres();

    @Query("SELECT * FROM songs WHERE album LIKE :album")
    List<Song> getAlbum(String album);

    @Query("SELECT * FROM songs WHERE artist LIKE :artist")
    List<Song> getArtistSongs(String artist);

    @Query("SELECT COUNT(*) from songs")
    int songCount();

    @Query("SELECT * FROM songs WHERE genre LIKE :genre")
    List<Song> getGenreSongs(String genre);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Song... songs);

    @Query("DELETE FROM songs")
    void nukeDatabase();
}

