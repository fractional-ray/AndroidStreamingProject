package com.example.evan.seniorproject;

//import android.arch.persistence.room.Room;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.evan.seniorproject.db.Song;
import com.example.evan.seniorproject.db.SongDatabase;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Evan on 2/18/2018.
 */

public class PlaybackManager {


//    SongDatabase db;
    MediaPlayer mp = new MediaPlayer();
    MediaMetadataRetriever md = new MediaMetadataRetriever();

    private MainActivity context;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/music/";
    private ArrayList<Song> files;
    private ArrayList<String> currentQueue;
    private ArrayList<Song> songs;

    private SongPlayQueue<Song> queue;

    int currentPosition = 0;

    private boolean isRepeat = true;

    ///////////////////////////////Setup//////////////////////////////

    public static String getPath()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/music/";
    }

    PlaybackManager(MainActivity context)
    {
        this.context = context;
    }

    void startManager(ArrayList<Song> a)
    {
        files = a;
        songs = new ArrayList<Song>();
        queue = new SongPlayQueue<Song>(files);
        setUpListeners();
        context.updateSongScroll(files);

        Log.i("amount",files.size()+"");
    }

    private void loadFiles()
    {
        LoadFilesTask t = new LoadFilesTask();
//        files = t.doInBackground(path);
        context.updateSongScroll(files);
    }



    private void setUpListeners()
    {
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                skipForward();
            }
        });
    }

    //////////////////////////play management/////////////////////////

    /**
     * Play song or pause it if already playing.
     */
    public void play()
    {
        if(!mp.isPlaying()){
            play(0);
        }
        else{
            mp.stop();
        }
    }

    /**
     * Update context and play song
     * @param context the context to be updated to.
     */
    public void playAndSwitchContext(Context context)
    {
        updateContext(context);
        play();
    }

    public void checkResumeAndPlay()
    {

    }

    /**
     * Play song at current position in queue starting at certain time in song.
     * @param position
     */
    private void play(int position)
    {
        try {
            mp.reset();
            mp.setDataSource(queue.current().getSongName());
            mp.prepare();
            mp.seekTo(position);
            mp.start();
            updateNowPlaying();
        } catch (Exception e) {
            Log.e("error",e.getMessage());
        }
    }

    public void skipForward(){
        queue.moveForward();

        if((queue.currentPosition()==0 && isRepeat)||queue.currentPosition()!=0)
            play();
    }

    public void skipBackward(){
        if(mp.getCurrentPosition() < 1500) {
            queue.moveBackwards();
            play();
        }
        else
        {
            mp.seekTo(0);
        }
    }

    private void updateNowPlaying()
    {
        context.updateNowPlayingLabel("now playing: \n");
    }

    private void updateContext(Context c)
    {
        if(c.getContextType()==Context.Contexts.ALL_SONGS_IN_LIBRARY_SPECIFIC)
        {
            queue.setContext(files);
            queue.setPosition(c.getPosition());
            Log.i("context","specific "+queue.current());
        }
    }

    ////////////////////////////inner classes////////////////////////////////

    private class LoadFilesTask extends AsyncTask<String,Void,ArrayList<String>>
    {
        ArrayList<String> fi;
        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            if(strings.length==1) {
                fi = new ArrayList<String>();
                loadFilesHelper(strings[0]);
                return fi;
            }
            else{
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
//                Log.i("file",f.getAbsolutePath());
//                Log.i("file","a");
                    fi.add(f.getAbsolutePath());
//                this.songs.add(new Song(f.getAbsolutePath(),));
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

    }
}
