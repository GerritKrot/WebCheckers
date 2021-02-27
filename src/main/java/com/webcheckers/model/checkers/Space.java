package com.webcheckers.model.checkers;

/**
 * This class contains the state and behavior necessary to represent a space on
 * the checkers board
 * Last Revision: 10/19/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class Space{
    //cell index range 0 to 7
    //ROW, COLUMN REPRESENTATION
    /**
     * Holds the Point that represents the Space's coordinates on the board
     */
    private final Position position;

    /**
     * Holds a Piece object whenever a Piece is on the current space
     */
    private Piece piece;

    /**
     * Holds the color of the current Space (WHITE/BLACK)
     */
    private final SpaceColor color;

    /**
     * enum representing the colors a Space can be
     */


    /**
     * constructor initializes a space object with passed in
     * @param position a two dimensional representation of the space's location (row, column)
     * @param piece the piece to be assigned to this space
     * @param color the color to be assigned to this space
     */
    public Space (Position position, Piece piece, SpaceColor color) {
        this.position = position;
        this.piece = piece;
        this.color = color;
    }

    /**
     * obtains the location of the space as a to dimensional point
     * @return the coordinate associated with this space on the checkers grid
     */
    public Position getCellId() {
        return position;
    }

    /**
     *Returns the integer representing the Y-coordinate of the Space
     * @return Integer Y-Coordinate
     */
    public int getCellIdx() { return position.getCell(); }

    /**
     * obtains the piece associated with this space on the board
     * @return the piece located on the space (NULL if no piece)
     */
    public Piece getPiece() {
        return piece;
    }

    protected void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Determines whether a space is valid for a Piece movement
     * based on the Space's color and occupancy
     * @return boolean representing a Space's validity for placement
     */
    public boolean isValid() {
        return((color == SpaceColor.BLACK) && (piece == null));
    }

    /**
     * obtains the color enum associated with this space on the board
     * @return the color of the current space on the board
     */
    public SpaceColor getColor() {
        return color;
    }

}
