package com.example.evan.seniorproject;

import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.evan.seniorproject.db.*;
import com.example.evan.seniorproject.db.Song;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 3/29/2018.
 */

public class FileManager extends AsyncTask<String,Integer,ArrayList<Song>> {

    ArrayList<Object> songAndFrameList;
    ArrayList<Song> fi;
    SongDatabase db;
    MediaMetadataRetriever md = new MediaMetadataRetriever();
    MainActivity main;
    final static String NO_ART_PATH = "<NONE>";


    private OnTaskComplete listener;

    public FileManager(OnTaskComplete listener, SongDatabase db, MainActivity main)
    {
        this.listener=listener;
        this.db = db;
        this.main = main;
    }

    @Override
    protected ArrayList<Song> doInBackground(String... strings) {
        if(strings.length==1) {
            fi = new ArrayList<Song>();

            if(db==null || db.songDAO().songCount()==0) {
                Log.i("db","new");
                loadFilesHelper(strings[0]);
//                Log.i("db")
            }
            else
            {
                Log.i("db","old");
                List<Song> a = db.songDAO().getAll();
                if(a instanceof ArrayList)
                {
                    fi.addAll(a);
                }
            }

            return fi;
        }
        else{
            Log.i("null","null");
            return null;
        }
    }

    private void loadFilesHelper(String subfolder)
    {
//        Log.i("path",subfolder);

//        Log.i()

            File directory = new File(subfolder);
            File[] files = directory.listFiles();
            for (File f : files) {
                if (!f.isDirectory() && (f.getName().endsWith(".mp3") || f.getName().endsWith(".m4a"))) {

                    Log.i("file", f.getAbsolutePath());

                    md.setDataSource(f.getAbsolutePath());

                    String title = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    String artist = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    String album = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                    String genre = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);

                    if(title == null)
                    {
                        title = "<unknown>";
                    }
                    if(artist == null)
                    {
                        artist = "<unknown>";
                    }
                    if(album == null)
                    {
                        album = "<unknown>";
                    }
                    if(genre == null)
                    {
                        genre = "<unknown>";
                    }

                    String imagePath = album+"---"+artist;

                    Log.i("image path",main.getFilesDir().getAbsoluteFile()+File.separator+imagePath);

                    File image = new File(main.getFilesDir().getAbsoluteFile()+File.separator+imagePath);

                    imagePath = main.getFilesDir().getAbsoluteFile()+File.separator+imagePath;
                    if(!image.exists())
                    {
                        try {

                            FileOutputStream fos = new FileOutputStream(image);

                            byte[] b =md.getEmbeddedPicture();
                            if(b!=null)
                                fos.write(b);
                            else
                                imagePath=NO_ART_PATH;


                        } catch (IOException  e) {
                            e.printStackTrace();
                        }
                    }

                    com.example.evan.seniorproject.db.Song s = new Song(f.getAbsolutePath(),title,artist,album,genre,imagePath);
                    db.songDAO().insertAll(s);
                    fi.add(s);

                } else if (f.isDirectory()) {
                    loadFilesHelper(f.getAbsolutePath());
                } else {
                    return;
                }

            }


    }


    @Override
    protected void onProgressUpdate(Integer... integers)
    {

    }

    @Override
    protected void onPostExecute(ArrayList<Song> a)
    {
        Log.i("Task","complete");
        if(a!=null) {
            listener.onTaskComplete(a);
        }
        else{
            Log.i("error","null song list");
        }
    }
}
