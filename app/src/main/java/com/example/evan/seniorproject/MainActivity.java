package com.example.evan.seniorproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evan.seniorproject.view.SongAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnTaskComplete {

    PlaybackManager playbackManager;
    ConnectionManagerAsyncTask connectionManager;
    TextView nowPlaying;
//    LinearLayout songScroll;

    ProgressBar pb;
    RecyclerView songRecyclerView;
    RecyclerView.Adapter songAdapter;
    RecyclerView.LayoutManager layoutManager;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nowPlaying = findViewById(R.id.nowPlayingLabel);
//        songScroll = findViewById(R.id.linLayoutScroll);
        songRecyclerView = (RecyclerView) findViewById(R.id.songRecyclerView);
//        songRecyclerView.setScrollbarFadingEnabled(false);
        songRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        pb = findViewById(R.id.songLoadWheel);
        pb.setIndeterminate(true);
        pb.setEnabled(true);

        ArrayList<String> temp = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            temp.add("Test" + i);
        }


        pb = new ProgressBar(this);
        pb.setIndeterminate(true);

//        songScroll.addView(pb);

        requestPermissionsRuntime();

        playbackManager = new PlaybackManager(this);
        connectionManager = new ConnectionManagerAsyncTask(this);

        Log.i("test","test");
//        playbackManager.startManager();

        FileManager f = new FileManager(this);
//        f.doInBackground(PlaybackManager.getPath());


        f.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,PlaybackManager.getPath());

        Toast toast = Toast.makeText(this,"e",Toast.LENGTH_SHORT);
        toast.show();

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

        ProgressBar p = findViewById(R.id.songLoadWheel);
        p.setVisibility(View.GONE);

        songRecyclerView.setLayoutManager(layoutManager);
        songAdapter = new SongAdapter(s);
        songRecyclerView.setAdapter(songAdapter);

//        songScroll.removeView(pb);
//        for(int i = 0; i < s.size();i++) {
//
//            final MainSongScrollButton b = new MainSongScrollButton(this,s.get(i),i);
//            b.setOnClickListener(new View.OnClickListener() {
//                                     @Override
//                                     public void onClick(View view) {
//                                        Log.i("toPlay",b.getReference());
//                                         playAndUpdateContextAll(b.getPosition());
//                                     }
//                                 }
//            );
//            songScroll.addView(b);
//        }
    }

    public void playAndUpdateContextAll(int toPlay)
    {
        playbackManager.playAndSwitchContext(new Context(Context.Contexts.ALL_SONGS_IN_LIBRARY_SPECIFIC,null,toPlay));
    }

    public  void playAndUpdateContext(View v)
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


    @Override
    public void onTaskComplete(ArrayList<String>a) {
        playbackManager.startManager(a);
    }
}
