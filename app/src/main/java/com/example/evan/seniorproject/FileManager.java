package com.example.evan.seniorproject;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Evan on 3/29/2018.
 */

public class FileManager extends AsyncTask<String,Integer,ArrayList<String>> {

    ArrayList<Object> songAndFrameList;
    ArrayList<String> fi;


    private OnTaskComplete listener;

    public FileManager(OnTaskComplete listener)
    {
        this.listener=listener;
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        if(strings.length==1) {
            fi = new ArrayList<String>();
            loadFilesHelper(strings[0]);
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
        File directory = new File(subfolder);
        File[] files = directory.listFiles();
        for(File f:files)
        {
            if(!f.isDirectory() && (f.getName().endsWith(".mp3")||f.getName().endsWith(".m4a")))
            {

//                md.setDataSource(f.getAbsolutePath());
                Log.i("file",f.getAbsolutePath());
//                Log.i("file","a");
                fi.add(f.getAbsolutePath());
//                this.songs.add(new Song(f.getAbsolutePath(),md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)));
            }
            else if(f.isDirectory())
            {
                loadFilesHelper(f.getAbsolutePath());
            }
            else
            {
                return;
            }

        }
    }


    @Override
    protected void onProgressUpdate(Integer... integers)
    {

    }

    @Override
    protected void onPostExecute(ArrayList<String> a)
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
