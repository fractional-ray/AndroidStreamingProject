package com.example.evan.seniorproject.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evan.seniorproject.R;

import java.util.ArrayList;

/**
 * Created by Evan on 4/15/2018.
 */

public class RemoteAdapter extends RecyclerView.Adapter {

    ArrayList<String> remoteFiles;
    SongScrollContainer container;

    public RemoteAdapter(ArrayList<String> a, SongScrollContainer container)
    {
        remoteFiles = a;
        this.container = container;
    }

    @Override
    public RemoteAdapter.RemoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater infl = LayoutInflater.from(parent.getContext());
        View v = infl.inflate(R.layout.remote_row_layout,parent,false);
        RemoteAdapter.RemoteViewHolder vh = new RemoteAdapter.RemoteViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final String name = remoteFiles.get(position);
        Log.i("outp",name);

        if(holder instanceof RemoteAdapter.RemoteViewHolder) {
            RemoteAdapter.RemoteViewHolder vh = (RemoteAdapter.RemoteViewHolder) holder;
            vh.txtHeader.setText(name);
            vh.txtHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("outp", name);
                    container.playAndUpdateContext(name,-1);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return remoteFiles.size();
    }



    public class RemoteViewHolder extends RecyclerView.ViewHolder{

        public View layout;
        public TextView txtHeader;


        public RemoteViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            txtHeader = (TextView) itemView.findViewById(R.id.firstLine);
        }
    }
}
