package com.example.evan.seniorproject.view;

import com.example.evan.seniorproject.db.Song;

import java.util.ArrayList;

/**
 * Created by Evan on 4/13/2018.
 */

public interface SongScrollContainer {

    void updateSongScroll(ArrayList<Song> a);

    void playAndUpdateContext(String id, int toPlay);
}
