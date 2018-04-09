package com.example.evan.seniorproject.stream;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Process;
import android.util.Log;

import com.example.evan.seniorproject.ConnectionManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created by Evan on 4/8/2018.
 */

public class StreamThread implements Runnable{

    String ip;
    int port;
    ConnectionManager parent;


    static DataOutputStream out;

    PipedOutputStream baos;
    PipedInputStream bais;
    Object lock;

    public StreamThread(String ip, int port, ConnectionManager parent, PipedOutputStream baos, Object lock)
    {
        this.ip = ip;
        this.port = port;
        this.parent = parent;
        this.baos = baos;
        this.lock = lock;
    }

    @Override
    public void run()
    {

        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

        try {
            Socket socket = new Socket(ip, 5000);

            out = new DataOutputStream(socket.getOutputStream());

            out.write("0\r\n".getBytes());
            out.flush();

            BufferedInputStream inS = new BufferedInputStream(socket.getInputStream());

            byte[] by = new byte[ConnectionManager.BUFFER_SIZE];
            int read;

            int accumulated = 0;
            boolean notified = false;

            do{
                read = inS.read(by,0,ConnectionManager.BUFFER_SIZE);
                accumulated+=read;
                baos.write(by,0,read);
                baos.flush();

                if(!notified && accumulated > AudioTrackThread.AT_BUFFER_SIZE/2)
                {
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                    notified = true;
                }

            }while(read>0);

            socket.close();

        } catch (Exception e) {
            Log.e("error stream", e.getMessage()+ " "+e.getClass());
        }
    }
}
