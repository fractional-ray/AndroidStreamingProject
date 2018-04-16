package com.example.evan.seniorproject;

//import android.arch.persistence.room.Room;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.evan.seniorproject.db.Song;
import com.example.evan.seniorproject.db.SongDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2/18/2018.
 */

public class PlaybackManager {

    private boolean internetMode = false;

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
     * @return true if started playing false if stopped playing
     */
    public boolean play()
    {
        if(!internetMode) {
            if (!mp.isPlaying()) {
                play(0);
                updateNowPlaying();
                return true;
            } else {
                mp.stop();
                return false;
            }
        }
        else
        {
            context.stopRemoteMode();
            context.updateNowPlayingLabel("Now playing:");
            internetMode = false;
            return false;
        }
    }

    /**
     * Update context and play song
     * @param context the context to be updated to.
     */
    public void playAndSwitchContext(Context context)
    {
        mp.stop();
        if(updateContext(context))
            play();
        else{
            Toast t = Toast.makeText(this.context,"Error, cannot play selection",Toast.LENGTH_LONG);
            t.show();
        }

    }

    public void checkResumeAndPlay()
    {

    }


    public void setRemoteMode(boolean remoteState)
    {
        this.internetMode = remoteState;
    }

    /**
     * Play song at current position in queue starting at certain time in song.
     * @param position
     */
    private void play(int position)
    {
        try {
            mp.reset();
            mp.setDataSource(queue.current().getFileName());
            mp.prepare();
            mp.seekTo(position);
            mp.start();

            updateNowPlayingSeekbar();
            updateNowPlaying();

        } catch (Exception e) {
            Log.e("error",e.getMessage());
        }
    }

    public void skipForward(){
        if(internetMode)
            return;

        queue.moveForward();

        if((queue.currentPosition()==0 && isRepeat)||queue.currentPosition()!=0)
            mp.stop();
            play();
    }

    public void skipBackward(){
        if(internetMode)
            return;

        if(mp.getCurrentPosition() < 2000) {
            queue.moveBackwards();
            mp.stop();
            play();
        }
        else
        {
            mp.seekTo(0);
        }
    }

    public void seekTo(int toSeek)
    {
        if(mp.isPlaying())
            mp.seekTo(toSeek);
    }
    private void updateNowPlaying()
    {
        context.updateNowPlayingLabel(queue.current().toString());

    }

    private void updateNowPlayingSeekbar()
    {
        context.updateNowPlayingSeekbar();
    }

    private boolean updateContext(Context c)
    {
        if(c.getContextType()==Context.Contexts.ALL_SONGS_IN_LIBRARY_SPECIFIC)
        {
            queue.setContext(files);
            queue.setPosition(c.getPosition());
            Log.i("context","specific "+queue.current());
            return true;
        }
        else if(c.getContextType()==Context.Contexts.ARTIST)
        {


            List<Song> l = context.getSongDB().songDAO().getArtistSongs(c.id);

            if(l.size()!=0)
            {
                Log.i("outp","another test "+c.id+" "+l.size());

                queue.setContext(l);

                queue.setPosition(c.getPosition());
                return true;
            }

            Toast t = Toast.makeText(context,"No songs in artist "+c.id,Toast.LENGTH_SHORT);
            t.show();

        }
        else if(c.getContextType()==Context.Contexts.ALBUM)
        {


            List<Song> l = context.getSongDB().songDAO().getAlbum(c.id);

            if(l.size()!=0)
            {
                Log.i("outp","another test "+c.id+" "+l.size());

                queue.setContext(l);

                queue.setPosition(c.getPosition());
                return true;
            }

            Toast t = Toast.makeText(context,"No songs in album "+c.id,Toast.LENGTH_SHORT);
            t.show();

        }
        else if(c.getContextType()==Context.Contexts.REMOTE)
        {
//            context.getConnectionManager().play(c.id);
            internetMode=true;
            return false;
        }

        return false;
    }

    public int getDuration()
    {
        return mp.getDuration();
    }

    public int getCurrentPosition()
    {
        return mp.getCurrentPosition();
    }

    public int getDurationSeconds()
    {
        return mp.getDuration()/1000;
    }

    public int getCurrentPositionSeconds()
    {
        return mp.getCurrentPosition()/1000;
    }

    public String getDurationFormattedString()
    {
        return getDurationSeconds()/60 +":"+getDurationSeconds()%60;
    }

    public String getCurrentPositionFormattedString()
    {
        return getCurrentPositionSeconds()/60 +":"+getCurrentPositionSeconds()%60;
    }

    public String getTimeRemainingFormattedString()
    {
        int t = getDurationSeconds()-getCurrentPositionSeconds();
        return t/60+":"+t%60;

    }

    public void setRepeat(boolean repeat)
    {

    }

    public void setShuffle(boolean shuffle)
    {

    }

    public ArrayList<Song> getAllSongs()
    {
        return null;
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
