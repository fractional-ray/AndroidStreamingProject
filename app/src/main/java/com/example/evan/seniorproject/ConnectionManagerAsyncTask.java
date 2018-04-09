package com.example.evan.seniorproject;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Evan on 2/18/2018.
 */

public class ConnectionManagerAsyncTask {

    final static String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/music/";
    static String hostName = "192.168.1.117";
    static int port = 2222;
    static BufferedReader in;
    static DataOutputStream out;
    static ByteArrayOutputStream byteOut;
    private static boolean running = false;
    final static int BUFFER_SIZE = 1024;
   static ArrayList<Byte> data;
    static ConnectionManager parent;
    static String ip;

    MainActivity context;
    static AudioTrack audioTrack;

    ConnectionManagerAsyncTask(MainActivity context, ArrayList<Byte> data,ConnectionManager parent) {
        this.context = context;
        this.parent = parent;
        this.data = data;
    }

    void connect(String ip) {
        this.ip = ip;
        ConnectionTask ct = new ConnectionTask();

        ct.execute();

    }

    private class ConnectionTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("connection test", "opened");
            try {
                Socket socket = new Socket(ip, 5000);
                audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,44100, AudioFormat.CHANNEL_OUT_STEREO,AudioFormat.ENCODING_PCM_16BIT,AudioTrack.getMinBufferSize(14400,AudioFormat.CHANNEL_OUT_STEREO,AudioFormat.ENCODING_PCM_16BIT),AudioTrack.MODE_STREAM);
                out = new DataOutputStream(socket.getOutputStream());

                out.write("0\r\n".getBytes());
                out.flush();

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedInputStream inS = new BufferedInputStream(socket.getInputStream());
                String line;

                FileOutputStream newFile = new FileOutputStream(path+"test.mp3");
//                byteOut = new ByteArrayOutputStream(1024);
                byte[] by = new byte[BUFFER_SIZE];
                int read;



                int accumulated = 0;

                read = inS.read(by,0,BUFFER_SIZE);
                accumulated += audioTrack.write(by, 0, read);

                audioTrack.play();

                do{

                    long start = System.nanoTime();
                    read = inS.read(by,0,BUFFER_SIZE);
                    accumulated += audioTrack.write(by, 0, read);
                    Long end = System.nanoTime();



                    Log.i("connect","time "+(end-start));
//                    int bufferPlaybackFrame = (audioTrack.getPlaybackHeadPosition() & 0xFF)*32;

//                    Log.i("connect",accumulated+"");
//                    Log.i("connect",bufferPlaybackFrame+" p");
//                        if(accumulated - bufferPlaybackFrame<100)
//                        {
//                            audioTrack.pause();
//                        }
//                    else
//                    {
//                        if(audioTrack.getPlayState()==AudioTrack.PLAYSTATE_PAUSED)
//                        {
//                            audioTrack.play();
//                        }
//                    }

//                    Log.i("connect",read+"");
//                    if(data==null)
//                        Log.i("connect","null");
//                    for(int i =0; i < read;i++)
//                    {
//                        data.add(by[i]);
//                    }

//                    byteOut.write(data,0,read);

//                    parent.incrementSegmentsReceived();
                    //                    Log.i("message",line);
                }while(read>0);




                running = true;


                socket.close();
                running = false;
            } catch (Exception e) {
                Log.e("error stream", e.getMessage()+ " "+e.getClass());
            }

            Log.i("connection test", "closed");
            return null;
        }
    }
}



