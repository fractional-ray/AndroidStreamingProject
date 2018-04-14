package com.example.evan.seniorproject.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evan.seniorproject.R;
import com.example.evan.seniorproject.db.Song;

import java.util.ArrayList;

/**
 * Created by Evan on 4/14/2018.
 */

public class AlbumAdapter  extends RecyclerView.Adapter
{
    private ArrayList<String> albums;
    SongScrollContainer songScrollContainer;

    public AlbumAdapter(ArrayList<String> a)
    {
        albums = a;
//        this.songScrollContainer = songScrollContainer;
    }

    @Override
    public AlbumAdapter.AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater infl = LayoutInflater.from(parent.getContext());
        View v = infl.inflate(R.layout.album_row_layout,parent,false);
        AlbumAdapter.AlbumViewHolder vh = new AlbumAdapter.AlbumViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final String name = albums.get(position);
//        Log.i("outp",name);

        if(holder instanceof AlbumAdapter.AlbumViewHolder) {
            AlbumAdapter.AlbumViewHolder vh = (AlbumAdapter.AlbumViewHolder) holder;
            vh.txtHeader.setText(name);
            vh.txtHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("outp", name);
//                    songScrollContainer.playAndUpdateContext(null,position);
                }
            });

//            String inf =name.getAlbum()+" - "+name.getArtist();
//            vh.txtFooter.setText(inf);
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }



    public class AlbumViewHolder extends RecyclerView.ViewHolder{

        public View layout;
        public TextView txtHeader;


        public AlbumViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            txtHeader = (TextView) itemView.findViewById(R.id.firstLine);
        }
    }

}
