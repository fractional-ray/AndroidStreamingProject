package com.example.evan.seniorproject.fragmentManagement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.evan.seniorproject.view.AlbumFragment;
import com.example.evan.seniorproject.view.ArtistFragment;
import com.example.evan.seniorproject.view.RemoteFragment;
import com.example.evan.seniorproject.view.SongFragment;

import java.util.ArrayList;

/**
 * Created by Evan on 4/14/2018.
 */

public class MusicFragmentAdapter extends FragmentPagerAdapter {

    private final int SCREEN_NUMBER = 4;
    ArrayList<Fragment> fragmentList;

    public MusicFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new SongFragment());
        fragmentList.add(new AlbumFragment());
        fragmentList.add(new ArtistFragment());
        fragmentList.add(new RemoteFragment());
    }

    @Override
    public Fragment getItem(int position) {

        if(position >=0 && position < fragmentList.size())
            return fragmentList.get(position);
        return null;
    }

    @Override
    public int getCount() {
        return SCREEN_NUMBER;
    }
}
