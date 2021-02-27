package com.webcheckers.model.checkers;

import com.webcheckers.model.checkers.Piece;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
@Tag("Model-tier")
public class BoardViewTest {

    private BoardView testBoardView = new BoardView(true);
    private BoardView testBoardView2 = new BoardView(true);

    @Test
    public void Test1GettingPiece(){
        Piece testPiece = testBoardView.getPiece(new Position(0,1));
        assertNotNull(testPiece,"Fail: Piece returned back as null; no Piece found");
        testPiece = testBoardView.getPiece(new Position(0, 0));
        assertNull(testPiece,"Fail: Piece returned should've been null; no Piece should be found");
    }

    @Test
    public void Test2GettingColor(){
        Piece testPiece = testBoardView.getPiece(new Position(1,0));
        assertEquals(testPiece.getColor(), CheckerPieceColor.WHITE,
                "Fail: Piece's color doesn't match board orientation");
        testPiece = testBoardView.getPiece(new Position(0,5));
        assertEquals(testPiece.getColor(), CheckerPieceColor.RED,
                "Fail: Piece's color doesn't match board orientation");
    }

    @Test
    public void Test3CheckForWholeBoard() {
        int RedPieces = 0;
        int WhitePieces = 0;
        int NullSpaces = 0;
        for (int i = 0 ; i < 8 ; i++) {
            for (int j = 0 ; j < 8 ; j++) {
                Piece testPiece = testBoardView.getPiece(new Position(i,j));
                if (testPiece!=null) {
                    if (testPiece.getColor()== CheckerPieceColor.RED) {
                        RedPieces++;
                    }
                    if (testPiece.getColor()== CheckerPieceColor.WHITE) {
                        WhitePieces++;
                    }
                }
                else{
                    NullSpaces++;
                }
            }
        }
        assertEquals(RedPieces, 12, "Fail: Incorrect number of Red Pieces Implemented");
        assertEquals(WhitePieces, 12, "Fail: Incorrect number of White Pieces Implemented");
        assertEquals(NullSpaces, 40, "Fail: Incorrect number of Null Spaces found on board");
    }

    @Test
    public void Test4SetPiece(){
        Move testMove = new Move(new Position(0, 5),new Position(1,4));
        testBoardView.updateView(testMove);
        assertNotNull(testBoardView.getPiece(new Position(1,4)), "Fail: Normal Move Piece wasn't found");

        testMove = new Move(new Position(4, 5),new Position(6,3));
        testMove.setJump();
        testBoardView.updateView(testMove);
        assertNotNull(testBoardView.getPiece(new Position(6,3)), "Fail: Jump Move Piece wasn't Found");
    }
/*
    @Test
    public void Test5Validate(){
        Move testMove = null;
        Piece.CheckerPieceColor testColor = Piece.CheckerPieceColor.RED;
        assertFalse(testBoardView2.validate(testMove, testColor), "Fail: Null Move is passing true but shouldn't.");

        testMove = new Move(new Position(0,0), new Position(1,1));
        assertFalse(testBoardView2.validate(testMove, testColor),
                "Fail: Null Space Move is passing true but shouldn't.");

        testMove = new Move(new Position(5,0), new Position(6,1));
        assertFalse(testBoardView2.validate(testMove, testColor),
                "Fail: Single Move should not be allowed to move backwards.");

        testMove = new Move(new Position(5,2), new Position(3,4));
        assertFalse(testBoardView2.validate(testMove, testColor),
                "Fail: Jump Move forward shouldn't be allowed..");

    }*/
}
