package com.example.evan.seniorproject;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Evan on 2/18/2018.
 */

public class PlaybackManager {
    MediaPlayer mp = new MediaPlayer();
    MediaMetadataRetriever md = new MediaMetadataRetriever();

    MainActivity context;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/music/";
    ArrayList<String> files;

    int currentSong=0;
    int currentPosition = 0;

    PlaybackManager(MainActivity context)
    {
        this.context = context;
        files = new ArrayList<String>();

    }

    public void startManager()
    {
        loadFiles();
    }

    private void loadFiles()
    {
        loadFilesHelper(path);
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
//                Log.i("file",f.getAbsolutePath());
                this.files.add(f.getAbsolutePath());
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

    public void play()
    {
        if(!mp.isPlaying()){
            play(currentPosition);
        }
        else{
            currentPosition=mp.getCurrentPosition();
            mp.stop();
        }
    }

    private void play(int position)
    {
        try {
            mp.reset();
            mp.setDataSource(files.get(currentSong));
            mp.prepare();
            mp.seekTo(position);
            mp.start();

            updateNowPlaying();
        } catch (Exception e) {
            Log.e("error",e.getMessage());
        }
    }

    public void skipForward(){
        currentSong= (currentSong+1)%files.size();
        mp.stop();
        play();
    }

    public void skipBackward(){
        if(mp.getCurrentPosition() < 1500) {
            currentSong = ((currentSong - 1) + files.size()) % files.size();
            mp.stop();
            play();
        }
        else
        {
            mp.seekTo(0);
        }
    }

    private void updateNowPlaying()
    {
        md.setDataSource(files.get(currentSong));
        context.updateNowPlayingLabel("now laying: \n"+md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)+"\n"+
                md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)+"\n"+
                md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));

    }
}
