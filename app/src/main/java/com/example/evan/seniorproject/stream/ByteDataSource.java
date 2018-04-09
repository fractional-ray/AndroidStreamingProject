package com.example.evan.seniorproject.stream;

import android.media.MediaDataSource;
import android.util.Log;

import com.example.evan.seniorproject.ConnectionManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Evan on 4/6/2018.
 */

public class ByteDataSource extends MediaDataSource {

    ArrayList<Byte> data;
    ConnectionManager cm;

    public ByteDataSource(ArrayList<Byte> data,ConnectionManager cm)
    {
        this.data = data;
        this.cm = cm;
    }

    @Override
    public int readAt(long srcPos, byte[] bytes, int buffOffset, int size) throws IOException {
//        cm.incrementSegmentsPlayed();
//
//        int srcPointer = (int) srcPos;
//        for (int buffPointer = buffOffset; buffPointer < size; buffPointer++,srcPointer++) {
//            bytes[buffPointer]=data.get(srcPointer);
//
//        }
//

        return size;
    }

    @Override
    public long getSize() throws IOException {
        return 9227948;
    }

    @Override
    public void close() throws IOException {
        Log.i("data","close");
    }
}
