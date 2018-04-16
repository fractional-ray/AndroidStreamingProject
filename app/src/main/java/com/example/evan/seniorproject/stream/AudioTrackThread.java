package com.example.evan.seniorproject.stream;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.provider.MediaStore;
import android.util.Log;

import com.example.evan.seniorproject.ConnectionManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

/**
 * Created by Evan on 4/8/2018.
 */

public class AudioTrackThread implements Runnable {

    private AudioTrack audioTrack;
    private String ip;
    private int port;
    ConnectionManager parent;
    PipedInputStream bais;
   Object lock;
   ConnectionManager.ShutDown shutDown;

    //This amounts to 4 seconds of audio.
    public final static int AT_BUFFER_SIZE = 44100*16*2*4;


    public AudioTrackThread(String ip, int port, ConnectionManager parent, PipedInputStream bais, Object lock, ConnectionManager.ShutDown shutDown) {
        this.ip = ip;
        this.port = port;
        this.parent = parent;
        this.bais = bais;
        this.lock = lock;
        this.shutDown = shutDown;
    }

    @Override
    public void run() {
        try {

            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, AudioTrack.getMinBufferSize(14400, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT), AudioTrack.MODE_STREAM);

            byte[] by = new byte[ConnectionManager.BUFFER_SIZE];
            int read=0;

            boolean started = false;

            int waitBuffer = 44100*16*2*5;

            int accumulated = 0;

//            Thread.sleep(2000);

            synchronized (lock) {
                lock.wait();
            }

            read = bais.read(by, 0, ConnectionManager.BUFFER_SIZE);
            audioTrack.write(by, 0, read);
            audioTrack.play();

            do {

                if(shutDown.shutDown)
                    break;

                read = bais.read(by, 0, ConnectionManager.BUFFER_SIZE);
                audioTrack.write(by, 0, read);


            } while (read > 0);
        }
        catch(Exception e)
        {
            Log.e("Reader error",e.getMessage());
            e.printStackTrace();
        }
        parent.finishPlaying();
    }

}