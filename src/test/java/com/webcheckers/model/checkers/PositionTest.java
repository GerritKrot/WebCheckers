package com.webcheckers.model.checkers;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class PositionTest {

    @Test
    public void createPositionTest() {
        Position pos = new Position(3, 5);
        assertNotNull(pos, "Failed to create Position");
    }

    @Test
    public void getCellTest() {
        Position pos = new Position(2, 7);
        int cell = pos.getCell();
        assertEquals(cell, 2, "Returned " + cell + " Instead of 2");
    }

    @Test
    public void getRowTest() {
        Position pos = new Position(5, 1);
        int row = pos.getRow();
        assertEquals(row, 1, "Returned " + row + " instead of 1");
    }

    @Test
    public void equalityTest() {
        Position pos1 = new Position(3, 6);
        Position pos2 = new Position(3, 6);
        assertEquals(pos1, pos2, "Incorrect equality semantics for value object");
        assertEquals(pos1.hashCode(), pos2.hashCode(), "hashCode equals contract not upheld");
    }

    @Test
    public void notEqualTest() {
        Position pos1 = new Position(7, 2);
        Position pos2 = new Position(1, 4);
        assertNotEquals(pos1, pos2, "Unequal objects should not evaluate to equals");
        assertNotEquals(pos1.hashCode(), pos2.hashCode(), "Unequal objects should not have equal hashCodes");
    }

    @Test
    public void onlyEqualsPositionTest() {
        Position pos = new Position(4,2);
        GregorianCalendar notAPosition = new GregorianCalendar();
        assertNotEquals(pos, notAPosition, "A Position should not equal a GregorianCalender");
    }

}
