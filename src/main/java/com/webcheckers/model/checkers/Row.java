package com.webcheckers.model.checkers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * This class contains the state and behavior associated with a row on a checkers board
 * Last Revision: 10/19/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class Row implements Iterable<Space>{

    /**
     * Integer used to pinpoint position/index in board arrayList
     */
    private final int index;

    /**
     * ArrayList that holds an iterable list of Spaces
     */
    private final List<Space> spaces;

    /**
     * Creates a Row object that holds an index to represent position
     * in board list and holds an iterable list of Spaces
     * @param index Represents position in board list
     * @param spaces Holds list of iterable Spaces
     */
    public Row (int index, ArrayList<Space> spaces){
        this.index = index;
        this.spaces = spaces;
    }

    /**
     * Returns index of Row in board list
     * @return
     */
    public int getIndex(){
        return index;
    }

    /**
     * Returns a Space object from the Space list
     * @param i Index used to pinpoint desired Space
     * @return The Space from the list
     */
    public Space getSpace(int i) {
        return spaces.get(i);
    }

    /**
     * Returns iterator used to traverse the Space list
     * @return Iterator for the Space list
     */
    @Override
    public Iterator<Space> iterator() {
        return spaces.iterator();
    }
}
