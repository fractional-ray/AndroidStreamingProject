package com.example.evan.seniorproject.view;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.evan.seniorproject.Context;
import com.example.evan.seniorproject.MainActivity;
import com.example.evan.seniorproject.R;
import com.example.evan.seniorproject.db.Song;
import com.example.evan.seniorproject.fragmentManagement.FragmentPagerInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Evan on 4/14/2018.
 */

public class AlbumFragment extends Fragment implements FragmentPagerInterface, PopupScrollContainer, SongScrollContainer{

    RecyclerView albumRecyclerView;
    RecyclerView.Adapter albumAdapter;
    RecyclerView.LayoutManager layoutManager;
    MainActivity main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.album_list_fragment_layout,container,false);

        albumRecyclerView = (RecyclerView) v.findViewById(R.id.albumRecyclerView);
        albumRecyclerView.setHasFixedSize(true);
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

        List<String> l = main.getSongDB().songDAO().getUniqueAlbums();


        albumRecyclerView.setLayoutManager(layoutManager);
        albumAdapter = new AlbumAdapter((ArrayList) l,this);
        albumRecyclerView.setAdapter(albumAdapter);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void showPopUp(View view, String id) {

        View popupView = getActivity().getLayoutInflater().inflate(R.layout.song_popup_view_layout,null);

        TextView t = popupView.findViewById(R.id.collectionLabel);
        t.setText(id);

        RecyclerView r = popupView.findViewById(R.id.popupRecyclerView);
        r.setHasFixedSize(true);
        r.setLayoutManager(new LinearLayoutManager(main));

        ArrayList<Song> a = (ArrayList) main.getSongDB().songDAO().getAlbum(id);
        for(Song s:a)
        {
            Log.i("a",s.getSongName());
        }

        r.setAdapter(new SongAdapter(a,this,SongAdapter.ALBUM));

        popupView.setBackgroundColor(Color.WHITE);
        PopupWindow window = new PopupWindow(popupView);

        Display display = main.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        window.setHeight((int)(height*.80f));
        window.setWidth((int)(width*.80f));

        window.setFocusable(true);
        window.setBackgroundDrawable(new ColorDrawable());

        window.showAtLocation(view, Gravity.CENTER,0,-(int)(height*.1f));


    }

    @Override
    public void updateSongScroll(ArrayList<Song> a) {

    }

    @Override
    public void playAndUpdateContext(String id, int toPlay) {
        main.getPlaybackManager().playAndSwitchContext(new Context(Context.Contexts.ALBUM,id,toPlay));
    }
}
