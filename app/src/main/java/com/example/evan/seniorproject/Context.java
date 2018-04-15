package com.example.evan.seniorproject;

/**
 * Created by Evan on 3/29/2018.
 */

public class Context {
    public enum  Contexts {ALL_SONGS_IN_LIBRARY_SPECIFIC,ALL_SONGS_IN_LIBRARY_BEGINNING,ALBUM,ARTIST,GENRE,PLAYLIST,REMOTE}
    Contexts c;
    String id;
    int position;

    public Context(Contexts c, String id, int position)
    {
        this.c = c;
        this.id = id;
        this.position = position;
    }

    public Contexts getContextType()
    {
        return c;
    }

    public String getIdentifier()
    {
        return id;
    }

    public int getPosition()
    {
        return position;
    }
}
