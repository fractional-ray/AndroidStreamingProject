package com.example.evan.seniorproject;

import android.content.Context;
import android.widget.Button;

/**
 * Created by Evan on 3/28/2018.
 */

public class MainSongScrollButton extends android.support.v7.widget.AppCompatButton {
    String ref;
    int pos;

    public MainSongScrollButton(Context context, String reference, int pos) {
        super(context);
        ref = reference;
        this.setText(reference);
        this.pos = pos;
    }

    public String getReference() {
        return ref;
    }

    public int getPosition()
    {
        return pos;
    }

    public void setReference(String ref) {
        this.ref = ref;
        this.setText(ref);
    }

}
