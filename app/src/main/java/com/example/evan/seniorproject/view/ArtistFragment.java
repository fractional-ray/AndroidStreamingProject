package com.example.evan.seniorproject.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evan.seniorproject.MainActivity;
import com.example.evan.seniorproject.R;
import com.example.evan.seniorproject.fragmentManagement.FragmentPagerInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 4/14/2018.
 */

public class ArtistFragment extends Fragment implements FragmentPagerInterface{

    RecyclerView artistRecyclerView;
    RecyclerView.Adapter artistAdapter;
    RecyclerView.LayoutManager layoutManager;
    MainActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.artist_list_fragment_layout,container,false);


        artistRecyclerView = (RecyclerView) v.findViewById(R.id.artistRecyclerView);
        artistRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(main);

        return v;
    }

    @Override
    public void onAttach(android.content.Context context)
    {
        super.onAttach(context);
        Log.i("fragment","artist attached");

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
        List<String> l = main.getSongDB().songDAO().getUniqueArtists();

        artistRecyclerView.setLayoutManager(layoutManager);
        artistAdapter = new ArtistAdapter((ArrayList) l);
        artistRecyclerView.setAdapter(artistAdapter);

//        if(l != null) {
//            for (String s : l) {
//                if(s!=null) {
//                    Log.i("artist", s);
//                }
//            }
//            Log.i("artist",l.size()+"");
//        }
    }

    @Override
    public void onPauseFragment() {

    }
}
