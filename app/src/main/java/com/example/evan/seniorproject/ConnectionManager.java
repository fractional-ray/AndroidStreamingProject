package com.example.evan.seniorproject;

import android.content.*;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.evan.seniorproject.stream.AudioTrackThread;
import com.example.evan.seniorproject.stream.ByteDataSource;
import com.example.evan.seniorproject.stream.StreamThread;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Evan on 4/6/2018.
 */

public class ConnectionManager {

    final int PORT_NUMBER = 2222;
    String ip;

    public static volatile ArrayList<Byte> data = new ArrayList<Byte>();
    ConnectionManagerAsyncTask task;
    ByteDataSource source;
    MediaPlayer mpC;
    boolean started=false;
    public final static int BUFFER_SIZE = 4096;

    private PipedOutputStream baos;
    private PipedInputStream bais;


    public static volatile int segmentsPlayed = 0;
//    AtomicInteger segPlayed;


    public static volatile int segmentsReceived = 0;


    public ConnectionManager(MainActivity context){
        task = new ConnectionManagerAsyncTask(context,data,this);

    }

    public void initialize()
    {
        source = new ByteDataSource(data,this);
    }

    public int getSegmentsPlayed()
    {
        return segmentsPlayed;
    }

    public static int getSegmentsReceived() {
        return segmentsReceived;
    }

    public static void setSegmentsPlayed(int segmentsPlayed) {
        ConnectionManager.segmentsPlayed = segmentsPlayed;
    }

    public static void setSegmentsReceived(int segmentsReceived) {
        ConnectionManager.segmentsReceived = segmentsReceived;
    }

    public void incrementSegmentsReceived()
    {
        segmentsReceived++;
        Log.i("segments",segmentsReceived+"");
        if(!started && segmentsReceived>20)
        {
            started = true;
            start();
        }
//        else if(segmentsReceived-segmentsPlayed>20 && !mpC.isPlaying() && mpC.getCurrentPosition()>1)
//        {
//            mpC.start();
//        }
    }

    public void          incrementSegmentsPlayed()
    {
        segmentsPlayed++;
//        if(segmentsPlayed == segmentsReceived && mpC!= null && mpC.isPlaying())
//        {
//            mpC.pause();
//        }
    }


    public static void test(){

    }

    public void connect(String ip)
    {
        Object lock = new Object();

        try {
            baos= new PipedOutputStream();
            bais = new PipedInputStream(baos,AudioTrackThread.AT_BUFFER_SIZE);

        } catch (IOException e) {
            e.printStackTrace();
        }


        this.ip = ip;
        segmentsPlayed=0;
        segmentsReceived=0;

        StreamThread st = new StreamThread(ip,PORT_NUMBER,this,baos,lock);
        AudioTrackThread att = new AudioTrackThread(ip,PORT_NUMBER,this,bais,lock);

        Thread t1 = new Thread(st);
        Thread t2 = new Thread(att);


        t1.start();
        t2.start();

//        task.connect(ip);
        Log.i("connect","test");


    }



    private void start()
    {
        Log.i("player","starting player");
        mpC = new MediaPlayer();
        mpC.reset();
        try {
            mpC.setDataSource(source);
            mpC.prepare();
            mpC.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Illegal state",e.getMessage());
        }
//        catch (Exception e)
//        {
//            Log.e("Illegal state",e.getMessage());
//        }
    }
}
