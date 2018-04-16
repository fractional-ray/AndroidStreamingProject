package com.example.evan.seniorproject.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.evan.seniorproject.Context;
import com.example.evan.seniorproject.MainActivity;
import com.example.evan.seniorproject.R;
import com.example.evan.seniorproject.db.Song;
import com.example.evan.seniorproject.fragmentManagement.FragmentPagerInterface;

import java.util.ArrayList;

/**
 * Created by Evan on 4/14/2018.
 */

public class RemoteFragment extends Fragment implements FragmentPagerInterface, SongScrollContainer{

    EditText ipText;
    TextView remoteLabel;
    RecyclerView remoteRecyclerView;
    RecyclerView.Adapter remoteAdapter;
    RecyclerView.LayoutManager layoutManager;
    MainActivity main;
    Button connectButton;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.remote_list_fragment_layout,container,false);

        ipText = v.findViewById(R.id.ipText2);
        ipText.setText(MainActivity.STARTING_IP);
        connectButton = v.findViewById(R.id.connectButtonRemote);
        connectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i("connect","Connect from fragment");
                main.connect(ipText.getText().toString());
            }
        });
        remoteLabel = v.findViewById(R.id.noSongsLoadedLabel);

        remoteRecyclerView = (RecyclerView) v.findViewById(R.id.remoteRecyclerView);
        remoteRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(main);

        return v;
    }

    @Override
    public void onAttach(android.content.Context context)
    {
        super.onAttach(context);

        Log.i("fragment","album attached");
        if(getActivity() instanceof MainActivity) {
            main = (MainActivity) getActivity();
        }
        else
        {
            Log.e("fragment", "context is not correct");
        }

    }

    @Override
    public void onResumeFragment() {

        Log.i("fragment","resume remote");

        if(main.getConnectionManager().hasSongsLoaded()) {

            ArrayList<String> l = main.getConnectionManager().getRemoteFiles();

            remoteRecyclerView.setLayoutManager(layoutManager);
            remoteAdapter = new RemoteAdapter((ArrayList) l, this);
            remoteRecyclerView.setAdapter(remoteAdapter);
        }
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void updateSongScroll(ArrayList<Song> a) {

    }

    @Override
    public void playAndUpdateContext(String id, int toPlay) {
        main.playRemote(id,ipText.getText().toString());
    }

    public void connectRemote(View v)
    {

    }

    public void refresh(View v)
    {

    }
}
