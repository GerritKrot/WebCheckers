package com.webcheckers.model.checkers;
/**
 * This class contains the state and behavior associated with a position on the checkers board
 * Last Revision: 10/19/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class Position {

    private int row;
    private int cell;

    /**
     * instantiate a position at given cell and row
     * @param cell the cell to be assigned to the position
     * @param row the row to be assigned to the position
     */
    public Position(int cell, int row) {
        this.cell = cell;
        this.row = row;
    }

    /**
     * obtain the row assigned to this position
     * @return the row assigned to this position object
     */
    public int getRow() {
        return row;
    }

    /**
     * obtain the cell assigned to this position
     * @return the cell assigned to this position object
     */
    public int getCell() {
        return cell;
    }

    /**
     * check if this position object is identical to another object
     * @param obj the object for comparison
     * @return whether the two objects are equivalent
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position other = (Position)obj;
            return other.row == row && other.cell == cell;
        }
        return false;
    }

    /**
     * compute the hash code of this position object using
     * a formula based on the unique cell and row
     * @return the hash code of this position object
     */
    @Override
    public int hashCode() {
        return Integer.parseInt(cell + Integer.toString(row));
    }
}
