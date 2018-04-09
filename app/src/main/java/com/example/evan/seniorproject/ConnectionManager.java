package com.example.evan.seniorproject;

import android.util.Log;

import com.example.evan.seniorproject.stream.AudioTrackThread;

import com.example.evan.seniorproject.stream.StreamThread;


import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;


/**
 * Created by Evan on 4/6/2018.
 */

public class ConnectionManager {

    final static int PORT_NUMBER = 5000;
    String ip;

    public static volatile ArrayList<Byte> data = new ArrayList<Byte>();
    ConnectionManagerAsyncTask task;

    public final static int BUFFER_SIZE = 4096;

    private PipedOutputStream baos;
    private PipedInputStream bais;

    ArrayList<String> remoteFiles;

    MainActivity context;


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
    }

    public void play()
    {
        Object lock = new Object();

        try {
            baos= new PipedOutputStream();
            bais = new PipedInputStream(baos,AudioTrackThread.AT_BUFFER_SIZE);

        } catch (IOException e) {
            e.printStackTrace();
        }




        String file = remoteFiles.get(3);
        StreamThread st = new StreamThread(ip,PORT_NUMBER,this,baos,lock,file);
        AudioTrackThread att = new AudioTrackThread(ip,PORT_NUMBER,this,bais,lock);

        Thread t1 = new Thread(st);
        Thread t2 = new Thread(att);


        t1.start();
        t2.start();

        Log.i("connect","test");


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
        return remoteFiles.size() != 0;
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
