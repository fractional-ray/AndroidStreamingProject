package com.example.evan.seniorproject.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Evan on 3/29/2018.
 */

@Entity(tableName = "songs")
public class Song {

    /**
     * @param fileName
     * @param songName
     * @param artist
     * @param album
     */
    public Song(String fileName,String songName, String artist, String album,String genre, String imagePath)
    {
        this.songName = songName;
        this.fileName = fileName;
        this.artist = artist;
        this.album=album;
        this.length=0;
        this.country="<UNKNOWN>";
        this.genre=genre;
        this.imagePath = imagePath;
    }

    @PrimaryKey(autoGenerate = true)
    int key;


    @ColumnInfo(name = "song_name")
    private String songName;

    @ColumnInfo(name = "file_name")
    private String fileName;

    @ColumnInfo(name = "artist")
    private String artist;

    @ColumnInfo(name = "album")
    private String album;

    @ColumnInfo(name = "length")
    private int length;

    @ColumnInfo(name = "genre")
    private String genre;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo(name = "imagePath")
    private String imagePath;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImagePath(){return imagePath;}

    public void setImagePath(String imagePath){this.imagePath = imagePath;}

    @Override
    public String toString()
    {
        return songName+" - "+artist+" - "+album;
    }
}
