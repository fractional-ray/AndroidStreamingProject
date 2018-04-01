package com.example.evan.seniorproject;

import java.util.List;

/**
 * Created by Evan on 3/29/2018.
 */

public interface ContextQueue<T> {
    /**
     * @return Whether this ContextQueue has a next element or not
     */
    boolean hasNext();

    boolean hasPrevious();

    /**
     * @return The next element in this ContextQueue
     */
    T next();

    /**
     * @return The previous element in this ContextQueue
     */
    T previous();

    /**
     * @return The current element in this ContextQueue
     */
    T current();

    /**
     * Move the position of the queue forwards
     */
    void moveForward();


    /**
     * Move the position of the queue backwards
     */
    void moveBackwards();

    /**
     * Sets the context of this ContextQueue to some specific data collection.
     * @param list The data to set the context to. Must be an iterable list.
     */
    void setContext(List<T> list);

    /**
     * @return The data context for this ContextQueue
     */
    List<T> getContainer();

    /**
     *
     * @return The current position in the data context.
     */
    int currentPosition();

    /**
     *@param position, The position to move to in the data context.
     */
    void setPosition(int position);

    /**
     * Set the position in the data context back to 0.
     */
    void resetPosition();

}
