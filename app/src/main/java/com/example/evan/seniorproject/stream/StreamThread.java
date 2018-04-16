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
    String file;
    ConnectionManager.ShutDown shutDown;

    public StreamThread(String ip, int port, ConnectionManager parent, PipedOutputStream baos, Object lock, String file, ConnectionManager.ShutDown shutDown)
    {
        this.ip = ip;
        this.port = port;
        this.parent = parent;
        this.baos = baos;
        this.lock = lock;
        this.file = file;
        this.shutDown = shutDown;
    }

    @Override
    public void run()
    {

        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

        try {
            Socket socket = new Socket(ip, port);

            out = new DataOutputStream(socket.getOutputStream());

//            out.write("0\r\n".getBytes());
            out.write(("0<>"+file+"<>0\r\n").getBytes());
            out.flush();

            BufferedInputStream inS = new BufferedInputStream(socket.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String servResponse = reader.readLine();


            if(servResponse.charAt(0)=='0') {

                Log.i("connect","server response good");
                byte[] by = new byte[ConnectionManager.BUFFER_SIZE];
                int read;

                int accumulated = 0;
                boolean notified = false;

                do {
                    if(shutDown.shutDown)
                        break;
//                    Log.i("connect","preparing to read");
                    read = inS.read(by, 0, ConnectionManager.BUFFER_SIZE);
//                    Log.i("connect",""+read);

                    accumulated += read;
                    baos.write(by, 0, read);
                    baos.flush();

                    if (!notified && accumulated > AudioTrackThread.AT_BUFFER_SIZE / 2) {
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                        notified = true;
                    }

                } while (read > 0);

                reader.close();
                out.close();
                socket.close();
                inS.close();
            }
            else
            {
                Log.e("connect",servResponse);
            }
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error stream", e.getMessage()+ " "+e.getClass());
        }
        parent.finishPlaying();
    }
}
