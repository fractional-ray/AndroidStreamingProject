package com.example.evan.seniorproject.view;

import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evan.seniorproject.Context;
import com.example.evan.seniorproject.MainActivity;
import com.example.evan.seniorproject.R;
import com.example.evan.seniorproject.db.Song;
import com.example.evan.seniorproject.fragmentManagement.FragmentPagerInterface;

import java.util.ArrayList;

/**
 * Created by Evan on 4/12/2018.
 */

public class SongFragment extends android.support.v4.app.Fragment implements SongScrollContainer, FragmentPagerInterface{

    RecyclerView songRecyclerView;
    RecyclerView.Adapter songAdapter;
    RecyclerView.LayoutManager layoutManager;
    MainActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.song_list_fragment_layout,container,false);

        songRecyclerView = (RecyclerView) v.findViewById(R.id.songRecyclerView);

        songRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(main);
        return v;
    }

    @Override
    public void onAttach(android.content.Context context)
    {
        super.onAttach(context);
        Log.i("fragment","song attached");
        if(getActivity() instanceof MainActivity) {
            main = (MainActivity) getActivity();
        }
        else
        {
            Log.e("fragment", "context is not correct");
        }

    }

    public void playAndUpdateContext(String id, int i)
    {
        main.getPlaybackManager().playAndSwitchContext(new Context(Context.Contexts.ALL_SONGS_IN_LIBRARY_SPECIFIC,id,i));
    }

    public void updateSongScroll(ArrayList<Song> s){

        songRecyclerView.setLayoutManager(layoutManager);
        songAdapter = new SongAdapter(s,this);
        songRecyclerView.setAdapter(songAdapter);

    }

    public Fragment getNewInstance()
    {
        return new SongFragment();
    }

    @Override
    public void onResumeFragment() {
        songRecyclerView.setLayoutManager(layoutManager);
        songAdapter = new SongAdapter((ArrayList)main.getSongDB().songDAO().getAll(),this);
        songRecyclerView.setAdapter(songAdapter);
    }

    @Override
    public void onPauseFragment() {

    }
}
