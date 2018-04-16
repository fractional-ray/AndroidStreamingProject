package com.example.evan.seniorproject;

import android.util.Log;

import com.example.evan.seniorproject.stream.AudioTrackThread;

import com.example.evan.seniorproject.stream.StreamThread;


import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by Evan on 4/6/2018.
 */

public class ConnectionManager {

    final static int PORT_NUMBER = 5555;
    String ip;

    ShutDown shutDown = new ShutDown();

    public static volatile ArrayList<Byte> data = new ArrayList<Byte>();
    ConnectionManagerAsyncTask task;

    public final static int BUFFER_SIZE = 4096;

    private PipedOutputStream baos;
    private PipedInputStream bais;

    private ArrayList<String> remoteFiles;

    MainActivity context;

    private static final Executor executor = Executors.newFixedThreadPool(2);


    public ConnectionManager(MainActivity context){
        this.context = context;

        remoteFiles = new ArrayList<String>();

    }


    public void connect(String ip) {
        task = new ConnectionManagerAsyncTask(this);
        task.runTask(ip,ClientCodes.GET_LIST);
        this.ip = ip;
    }

    public void updateSongList(String s)
    {
        loadServerSongStringIntoList(s);
        context.updateRemoteViewList();
    }

    public void play(String file, String ip2)
    {

        shutDown.shutDown = false;
        Object lock = new Object();

        try {
            baos= new PipedOutputStream();
            bais = new PipedInputStream(baos,AudioTrackThread.AT_BUFFER_SIZE);

        } catch (IOException e) {
            e.printStackTrace();
        }

        StreamThread st = new StreamThread(ip2,PORT_NUMBER,this,baos,lock,file,shutDown);
        AudioTrackThread att = new AudioTrackThread(ip2,PORT_NUMBER,this,bais,lock,shutDown);

        Thread t1 = new Thread(st);
        Thread t2 = new Thread(att);

        executor.execute(t1);
        executor.execute(t2);

//        t1.start();
//        t2.start();

//        executor.shutDownNow();

        Log.i("connect","test");

//        try {
//            t1.join();
//            t2.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Log.i("connect","threads joined");




    }

    public void loadServerSongStringIntoList(String serverString)
    {
        Log.i("song",serverString);
        String[] s = serverString.split("<>");

        if(s[0].equals("0")&&s.length>2) {

            for (int i = 1; i < s.length-1; i++) {
                String s1 = s[i];
                remoteFiles.add(s1);
                Log.i("song",s1);
            }
            if(s[s.length-1].equals("0"))
            {
                Log.i("connect","all good with song list from server");
            }

        }
        else
        {
            Log.e("connect","error in song list from server");
        }
    }

    public boolean hasSongsLoaded()
    {
        Log.i("connect","remote file size "+remoteFiles.size());
        return remoteFiles.size() != 0;
    }

    public ArrayList<String> getRemoteFiles()
    {
        return remoteFiles;
    }

    public void stopPlaying()
    {
        shutDown.shutDown = true;
    }

    public void resetAfterPlaying()
    {

    }


    public class ShutDown
    {
        public volatile boolean shutDown = false;

    }

    final class ClientCodes {

        static final char PLAY = '0';
        static final char STOP = '1';
        static final char GET_LIST = '2';
        static final char DISCONNECT = '3';

        char currentCode = PLAY;

        ClientCodes(char code) {
            currentCode = code;
        }

        public char getCurrentCode() {
            return currentCode;
        }

        public void setCurrentCode(char code) {
            currentCode = code;
        }

    }
}
