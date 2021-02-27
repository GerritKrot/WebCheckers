package com.webcheckers.model.checkers;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit test for Row
 * Last Revision: 10/19/2020
 * @author Payton Burak
 */
@Tag("Model Tier")
public class RowTest {
    @Test
    public void create_row_test(){
        ArrayList<Space> spaces = new ArrayList<>();
        Row testRow = new Row(1, spaces);
        assertNotNull(testRow, "Failed to instantiate a row");
    }

    @Test
    public void test_index() {
        ArrayList<Space> spaces = new ArrayList<>();
        Row testRow = new Row(1, spaces);
        assertEquals(testRow.getIndex(), 1, "accessor for index of rows does not work");
    }

    @Test
    public void test_no_contents(){
        ArrayList<Space> spaces = new ArrayList<>();
        Row testRow = new Row(1, spaces);
        Space space = null;
        try {
            space = testRow.getSpace(0);
        } catch (Exception e) {
            //Do nothing
        }
        assertNull(space, "arraylist is not properly instantiated in row class");
    }

    @Test
    public void test_iterator(){
        ArrayList<Space> spaces = new ArrayList<>();
        Row testRow = new Row(1, spaces);
        assertNotNull(testRow.iterator(), "iterator is not properly instantiated for row class");
    }
}
