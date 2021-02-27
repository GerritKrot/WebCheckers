package com.webcheckers.model.checkers;

import com.webcheckers.model.checkers.Piece;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import static org.junit.jupiter.api.Assertions.*;
@Tag("Model-tier")
public class PieceTest {

    @Test
    public void Make_Piece() {
        Piece p = new Piece(CheckerPieceType.KING, CheckerPieceColor.RED);
        assertNotNull(p, "Piece returned null instead of a piece");
    }

    @Test
    public void Piece_Types() {
        Piece p = new Piece(CheckerPieceType.KING, CheckerPieceColor.RED);
        CheckerPieceType t = p.getType();
        assertEquals(CheckerPieceType.KING, t, "Checker type was expected to be king, was instead "+ t + ".");
        p = new Piece(CheckerPieceType.SINGLE, CheckerPieceColor.RED);
        t = p.getType();
        assertEquals(CheckerPieceType.SINGLE, t, "Checker type was expected to be single, was instead "+ t + ".");
    }

    @Test
    public void Piece_Colors() {
        Piece p = new Piece(CheckerPieceType.SINGLE, CheckerPieceColor.RED);
        CheckerPieceColor c = p.getColor();
        assertEquals(CheckerPieceColor.RED, c, "Checker color was expected to be red, was instead "+ c + ".");
        p = new Piece(CheckerPieceType.SINGLE, CheckerPieceColor.WHITE);
        c = p.getColor();
        assertEquals(CheckerPieceColor.WHITE, c, "Checker color was expected to be white, was instead "+ c + ".");
    }

    @Test
    public void King_Checker(){
        Piece p = new Piece(CheckerPieceType.KING, CheckerPieceColor.RED);
        p.kingChecker();
        CheckerPieceType t = p.getType();
        assertEquals(CheckerPieceType.KING, t, "Checker type was expected to be king, was instead "+ t + ".");
    }

}
