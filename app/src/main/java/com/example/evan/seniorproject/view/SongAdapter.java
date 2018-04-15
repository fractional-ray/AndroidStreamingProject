package com.example.evan.seniorproject.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evan.seniorproject.MainActivity;
import com.example.evan.seniorproject.R;
import com.example.evan.seniorproject.db.Song;

import java.util.ArrayList;

/**
 * Created by Evan on 3/31/2018.
 */

public class SongAdapter extends RecyclerView.Adapter {
    final static int ALL = 0;
    final static int ALBUM = 1;
    final static int ARTIST = -2000;

    int type;
    private ArrayList<Song> files;
    SongScrollContainer songScrollContainer;

    public SongAdapter(ArrayList<Song> a, SongScrollContainer songScrollContainer, int type)
    {
        files = a;
        this.songScrollContainer = songScrollContainer;
        this.type=type;
    }

    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater infl = LayoutInflater.from(parent.getContext());
        View v = infl.inflate(R.layout.row_layout,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//    }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Song name = files.get(position);

        if(holder instanceof SongAdapter.ViewHolder) {
            SongAdapter.ViewHolder vh = (SongAdapter.ViewHolder) holder;
            vh.txtHeader.setText(name.getSongName());
            vh.txtHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Log.i("outp", name.getSongName());
                        if(type == SongAdapter.ALL)
                            songScrollContainer.playAndUpdateContext(null,position);
                        else if(type == SongAdapter.ALBUM)
                            songScrollContainer.playAndUpdateContext(name.getAlbum(),position);
                        else if(type == SongAdapter.ARTIST) {
                            Log.i("outp","album select");
                            songScrollContainer.playAndUpdateContext(name.getArtist(), position);
                    }


                }
            });

            String inf =name.getAlbum()+" - "+name.getArtist();
            vh.txtFooter.setText(inf);
        }
    }

    @Override
    public int getItemCount() {
        return files.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public View layout;
        public TextView txtHeader;
        public TextView txtFooter;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            txtHeader = (TextView) itemView.findViewById(R.id.firstLine);
            txtFooter = (TextView) itemView.findViewById(R.id.secondLine);
        }
    }


}
