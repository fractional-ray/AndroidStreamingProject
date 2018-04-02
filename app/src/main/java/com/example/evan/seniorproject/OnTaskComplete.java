package com.example.evan.seniorproject;

import com.example.evan.seniorproject.db.Song;

import java.util.ArrayList;

/**
 * Created by Evan on 3/31/2018.
 */

public interface OnTaskComplete {

    void onTaskComplete(ArrayList<Song> a);
}
