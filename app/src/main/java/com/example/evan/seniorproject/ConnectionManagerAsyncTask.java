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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Evan on 2/18/2018.
 */

public class ConnectionManagerAsyncTask {




    static BufferedReader in;
    static DataOutputStream out;

    static ConnectionManager parent;
    static String ip;




    ConnectionManagerAsyncTask(ConnectionManager parent) {

        this.parent = parent;

    }

    void runTask(String ip, char code) {
        this.ip = ip;

        ConnectionTask ct = new ConnectionTask(code);

        ct.execute();
    }

    private static class ConnectionTask extends AsyncTask<Void, Void, String> {


        char code;

        ConnectionTask(char code)
        {
            this.code = code;
        }

        @Override
        protected String doInBackground(Void... voids) {

            Log.i("connection test", "opened");
            try {

                if(code == ConnectionManager.ClientCodes.GET_LIST)
                {
                    return getSongList();
                }

            } catch (Exception e) {
                Log.e("error stream", e.getMessage()+ " "+e.getClass());
            }
            return null;
        }

        private String getSongList() throws IOException {
            Socket socket = new Socket(ip, ConnectionManager.PORT_NUMBER);
            out = new DataOutputStream(socket.getOutputStream());

            out.write((code+"\r\n").getBytes());
            out.flush();

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedInputStream inS = new BufferedInputStream(socket.getInputStream());

            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String serverMessage = serverReader.readLine();

            socket.close();

            return serverMessage;
        }

        @Override
        protected void onPostExecute(String result)
        {
            parent.updateSongList(result);
        }

    }


}



