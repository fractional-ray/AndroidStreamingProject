package com.example.evan.seniorproject;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Evan on 2/18/2018.
 */

public class ConnectionManagerAsyncTask {

    final static String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/music/";
    static String hostName = "192.168.1.117";
    static int port = 2222;
    static BufferedReader in;
    static DataOutputStream out;
    private static boolean running = false;

    MainActivity context;

    ConnectionManagerAsyncTask(MainActivity context) {
        this.context = context;
    }

    void connect() {
        ConnectionTask ct = new ConnectionTask();

        ct.execute();

    }

    private static class ConnectionTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("connection test", "opened");
            try {
                Socket socket = new Socket("192.168.1.117", 5000);
                out = new DataOutputStream(socket.getOutputStream());


                out.write("0\r\n".getBytes());
                out.flush();

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                InputStream inS = socket.getInputStream();
                String line;

                FileOutputStream newFile = new FileOutputStream(path+"test.mp3");
                byte[] by = new byte[1024];
                int read;
                do{
                    read = inS.read(by);
                    newFile.write(by);

//                    Log.i("message",line);
                }while(read>0);



//                Log.i("message",line);
                running = true;

//                do {
//                    Log.i("reading","reading");
//                    line = in.readLine();
//                    Log.i("line", line);
//                } while (!line.equals("-1"));

                socket.close();
                running = false;
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
            Log.i("connection test", "closed");
            return null;
        }
    }
}



