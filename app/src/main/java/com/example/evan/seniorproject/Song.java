package com.example.evan.seniorproject;

/**
 * Created by Evan on 3/28/2018.
 */

public class Song {

    private String fullPath;
    private String artName;



    private String songName;
    private int runtime;
    private boolean explicit;

    public Song(String fullPath, String artName, String albumName, String songName)
    {
        this.fullPath = fullPath;
        this.artName = artName;
        this.albumName = albumName;
        this.songName = songName;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public void setArtistName(String artName) {
        this.artName = artName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    private String albumName;

    public String getFullPath() {
        return fullPath;
    }

    public String getArtistName() {
        return artName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getSongName() {
        return songName;
    }

    public int getRuntime() {
        return runtime;
    }

    public boolean isExplicit() {
        return explicit;
    }

    @Override
    public String toString()
    {
        return getSongName()+"\n"+getArtistName()+"\n"+getAlbumName();
    }
}
