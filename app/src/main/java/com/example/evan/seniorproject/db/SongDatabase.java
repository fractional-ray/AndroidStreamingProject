package com.example.evan.seniorproject.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.provider.SyncStateContract;

/**
 * Created by Evan on 3/29/2018.
 */
@Database(entities = {Song.class},version = 1)
public abstract class SongDatabase extends RoomDatabase{
//{

    static final String DATABASE_NAME = "songdb";

    public abstract SongDAO songDAO();

    private static SongDatabase songDB;

    public static SongDatabase getInstance(Context context)
    {
        if(songDB==null)
        {
            songDB = buildDatabaseInstance(context);
        }
        return songDB;
    }

    private static SongDatabase buildDatabaseInstance(Context context)
    {
        synchronized(SongDatabase.class) {
            return Room.databaseBuilder(context.getApplicationContext(), SongDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        }
    }




}
