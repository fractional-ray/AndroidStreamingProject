package com.example.evan.seniorproject.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evan.seniorproject.MainActivity;
import com.example.evan.seniorproject.R;
import com.example.evan.seniorproject.db.Song;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Evan on 3/31/2018.
 */

public class SongAdapter extends RecyclerView.Adapter {
    final static int ALL = 0;
    final static int ALBUM = 1;
    final static int ARTIST = -2000;
    final static int GENRE = 99293;

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
                        else if(type == SongAdapter.GENRE)
                            songScrollContainer.playAndUpdateContext(name.getGenre(),position);


                }
            });

            vh.txtHeader.setText(name.getSongName());
            String inf =name.getAlbum()+" - "+name.getArtist();
            vh.txtFooter.setText(inf);

            File art = new File(name.getImagePath());
            if(art.exists())
            {
//                byte[] b = art
                Bitmap c = BitmapFactory.decodeFile(art.getAbsolutePath());
                if(c!=null) {
                    Log.i("album art", c.getWidth() + " " + c.getHeight());
                    vh.icon.setImageBitmap(c);
                }
//                BitmapFactory.decodeFile()
            }



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
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            txtHeader = (TextView) itemView.findViewById(R.id.firstLine);
            txtFooter = (TextView) itemView.findViewById(R.id.secondLine);
            icon = (ImageView) itemView.findViewById(R.id.albumIcon);
        }
    }


}
