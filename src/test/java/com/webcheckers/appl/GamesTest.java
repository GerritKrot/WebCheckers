package com.webcheckers.appl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for Games
 * Last Revision: 11/02/2020
 * @author Michael Canning
 */
@Tag("Application-tier")
public class GamesTest {

    Games games;

    /**
     * Sets up mock objects for CuT as well as initializes the CuT
     */
    @BeforeEach
    public void setup() {
        games = new Games();
    }

    /**
     * Tests UUT when no games are in the object
     */
    @Test
    public void testNoGames() {
        assert(games.getGameCount() == 0);
    }

    /**
     * Tests adding a game to the UUT
     */
    @Test
    public void testAddGame() {
        GameController gc = mock(GameController.class);
        when(gc.getId()).thenReturn("TEST_ID");
        games.addGame(gc);
        assert(games.getGameCount() == 1);
    }

    /**
     * Tests removing a game from the UUT
     */
    @Test
    public void testRemoveGame() {
        GameController gc = mock(GameController.class);
        when(gc.getId()).thenReturn("TEST_ID");
        games.addGame(gc);
        assert(games.getGameCount() == 1);
        games.removeGame(gc);
        assert(games.getGameCount() == 0);
    }

    /**
     * Tests getting a game from the UUT
     */
    @Test
    public void testGetGame() {
        GameController gc = mock(GameController.class);
        when(gc.getId()).thenReturn("TEST_ID");
        games.addGame(gc);
        assert(games.getGame("TEST_ID") == gc);
        assert(games.getGame("NONE_EXISTING_ID") == null);
    }

    /**
     * Tests getting a list of games from the UUT
     */
    @Test
    public void testGetGameList() {
        GameController gc = mock(GameController.class);
        when(gc.getId()).thenReturn("TEST_ID");
        games.addGame(gc);
        assert(games.getGameList()[0] == gc);
    }

}
