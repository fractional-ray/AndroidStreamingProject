package com.example.evan.seniorproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Evan on 3/29/2018.
 */

public class SongPlayQueue<T> implements ContextQueue<T> {

    ArrayList<T> context;
    int currPos=0;

    Context contextWrap = new Context(Context.Contexts.ALL_SONGS_IN_LIBRARY_SPECIFIC,null,0);
    Context.Contexts currentContext = Context.Contexts.ALL_SONGS_IN_LIBRARY_SPECIFIC;

    public SongPlayQueue(ArrayList<T> context)
    {
        this.context = context;
    }

    @Override
    public boolean hasNext() {
        return !(currPos == context.size());
    }

    @Override
    public boolean hasPrevious() {
        return !(currPos==0);
    }

    @Override
    public T next() {
        return context.get(currPos+1);
    }

    @Override
    public T previous() {
        return context.get(currPos-1);
    }

    @Override
    public T current() {
        return context.get(currPos);
    }

    @Override
    public void moveForward() {
        currPos= (currPos+1)%context.size();
    }

    @Override
    public void moveBackwards() {
        currPos = ((currPos - 1) + context.size()) % context.size();
    }

    @Override
    public void setContext(List<T> list) {
    context = (ArrayList<T>)list;
    }

    @Override
    public List<T> getContainer() {
        return context;
    }

    @Override
    public int currentPosition() {
        return currPos;
    }

    @Override
    public void setPosition(int position) {
    currPos = position;
    }

    public void resetPosition()
    {
        currPos = 0;
    }

    public void shuffle()
    {
        T c = context.remove(currPos);
        Collections.shuffle(context);
        context.add(0,c);
    }

    public Context getContextWrap()
    {
        return contextWrap;
    }

    public void setContextWrap(Context c2)
    {
        contextWrap=c2;
    }
}
