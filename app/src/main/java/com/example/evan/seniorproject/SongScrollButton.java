package com.example.evan.seniorproject;

import android.content.Context;
import android.widget.Button;

/**
 * Created by Evan on 3/28/2018.
 */

public class SongScrollButton extends android.support.v7.widget.AppCompatButton {
    String ref;

    public SongScrollButton(Context context, String reference) {
        super(context);
        ref = reference;
        this.setText(reference);
    }

    public String getReference() {
        return ref;
    }

    public void setReference(String ref) {
        this.ref = ref;
        this.setText(ref);
    }

}
