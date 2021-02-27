package com.webcheckers.model.checkers;

import com.webcheckers.model.checkers.Piece;
import com.webcheckers.model.checkers.Position;
import com.webcheckers.model.checkers.Space;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import static org.junit.jupiter.api.Assertions.*;
@Tag("Model-tier")
public class SpaceTest {

    private Space initTestSpace(int i){
        if (i==1){
            return new Space(new Position(2,3), new Piece(CheckerPieceType.KING, CheckerPieceColor.WHITE), SpaceColor.BLACK);
        }
        if (i==0){
            return new Space(new Position(1, 2), null, SpaceColor.BLACK);
        }
        else
            return new Space(new Position(3,4), null, SpaceColor.WHITE);
    }

    @Test
    public void Make_Space() {
        Space sp = initTestSpace(1);
        assertNotNull(sp, "Returned null instead of a space");
    }

    @Test
    public void Space_Position() {
        Space sp = initTestSpace(1);
        Position p = sp.getCellId();
        assertTrue((p.getCell()==2 && p.getRow()==3), "Expected the position 2,3, received the position " + p.getCell() + ", " + p.getRow()+".");
        int i = sp.getCellIdx();
        assertTrue(i==2, "Expected to get cell 2, received the row " + i + ".");
    }

    @Test
    public void Space_Piece() {
        Space sp = initTestSpace(1);
        Piece p = sp.getPiece();
        assertNotNull(p, "Expected a piece, received null");
        sp = initTestSpace(0);
        p = sp.getPiece();
        assertNull(p, "Expected null, received a piece");
    }

    @Test
    public void Space_Color() {
        Space sp = initTestSpace(1);
        assertEquals(SpaceColor.BLACK, sp.getColor(),  "Expected BLACK but received " + sp.getColor() + ".");
        sp = initTestSpace(2);
        assertEquals(SpaceColor.WHITE, sp.getColor(),  "Expected WHITE but received " + sp.getColor() + ".");
    }

    @Test
    public void Space_Valid() {
        Space sp = initTestSpace(1);
        assertTrue(!sp.isValid(), "Expected false (Piece on the Space), returned true");
        sp = initTestSpace(0);
        assertTrue(sp.isValid(), "Expected true (Piece not on Space and BLACK), returned false");
        sp = initTestSpace(2);
        assertTrue(!sp.isValid(), "Expected false (Space is WHITE), returned true");
    }


}
