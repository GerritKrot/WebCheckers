package com.webcheckers.model.checkers;

/**
 * This class contains the state and behavior associated with a checkers piece
 * Last Revision: 10/19/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class Piece {
    /**
     * Holds the type of checker piece (SINGLE/KING)
     */
    private CheckerPieceType type;

    /**
     * Holds the color for the checker piece
     */
    private final CheckerPieceColor color;

    /**
     * enum for the type of checkers piece
     */


    /**
     * enum for the possible checker piece colors
     */


    /**
     * constructor initializes a checkers piece with an assigned type
     * and color
     * @param type the type of piece to be created
     * @param color the color assigned to the current instance
     */
    public Piece(CheckerPieceType type, CheckerPieceColor color){
        this.type = type;
        this.color = color;
    }

    /**
     * obtains the type associated with the current checkers piece
     * @return the type of the current piece instance
     */
    public CheckerPieceType getType(){
        return type;
    }

    /**
     * obtains the color associated with the current checkers piece
     * @return the color of the current checkers piece instance
     */
    public CheckerPieceColor getColor(){
        return color;
    }

    /**
     * makes a normal checkers piece a king type piece
     */
    public void kingChecker(){type = CheckerPieceType.KING;}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Piece) {
            Piece other = (Piece)obj;
            return other.color == color;
        }
        return false;
    }
}
