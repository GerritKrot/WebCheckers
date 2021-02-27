package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the ELORank class
 * Last Revision: 11/02/2020
 * @author Michael Canning
 */
@Tag("Model-tier")
public class ELORankTest {

    private ELORank uut;

    /**
     * Sets up mock objects for CuT as well as initializes the CuT
     */
    @BeforeEach
    public void setup() {
        uut = new ELORank();
    }

    /**
     * Tests for a base ELORank with no games played
     */
    @Test
    public void testNoGamesPlayedScore() {
        assert(uut.getPerformanceRating() == 0);
    }

    /**
     * Tests rank after a win
     */
    @Test
    public void testOneWin() {
        uut.addWin(1000);
        assert(uut.getPerformanceRating() > 1000);
    }

    /**
     * Tests rank after a loss
     */
    @Test
    public void testOneLoss() {
        uut.addLoss(1000);
        assert(uut.getPerformanceRating() < 1000);
        assert(uut.getPerformanceRating() > 0);
    }

    /**
     * Tests rank after a draw
     */
    @Test void testOneDraw() {
        uut.addDraw(1000);
        assert(uut.getPerformanceRating() == 1000);
    }

}
