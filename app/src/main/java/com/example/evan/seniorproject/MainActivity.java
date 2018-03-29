package com.example.evan.seniorproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    PlaybackManager playbackManager;
    ConnectionManagerAsyncTask connectionManager;
    TextView nowPlaying;
    LinearLayout songScroll;
    ScrollView songScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nowPlaying = findViewById(R.id.nowPlayingLabel);
        songScroll = findViewById(R.id.linLayoutScroll);
        songScrollView = findViewById(R.id.songScrollMain);
        songScrollView.setScrollbarFadingEnabled(false);

        requestPermissionsRuntime();
        playbackManager = new PlaybackManager(this);
        connectionManager = new ConnectionManagerAsyncTask(this);
        Log.i("test","test");
        playbackManager.startManager();

    }

    private void requestPermissionsRuntime()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},22);

        }
    }

    public void updateSongScroll(ArrayList<String> s)
    {

        for(int i = 0; i < s.size();i++) {

            final SongScrollButton b = new SongScrollButton(this,s.get(i));
            b.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                        Log.i("toPlay",b.getReference());
                                         play(b.getReference());
                                     }
                                 }
            );
            songScroll.addView(b);
        }
    }

    public void play(String toPlay)
    {
        playbackManager.play(toPlay);
    }

    public void play(View v)
    {
        playbackManager.play();
    }

    public void back(View v)
    {
        playbackManager.skipBackward();
    }

    public void forward(View v)
    {
        playbackManager.skipForward();
    }

    public void connect(View v)
    {
        connectionManager.connect();
    }

    public void updateNowPlayingLabel(String update)
    {
        nowPlaying.setText(update);
    }
}
