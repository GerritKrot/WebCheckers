package com.webcheckers.model;

import com.webcheckers.appl.GameController;
import com.webcheckers.appl.Games;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.checkers.BoardView;
import com.webcheckers.model.checkers.MoveValidator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Tag("Model-tier")
public class PlayerTest {

    private Player testPlayer1 = new Player("Bob");
    private Player testPlayer2 = new Player("Sarah");
    private BoardView board_1 = mock(BoardView.class);
    private BoardView board_2 = mock(BoardView.class);
    private Games games = mock(Games.class);
    private MoveValidator validator = mock(MoveValidator.class);

    @Test
    public void test_PlayerName() {
        String name = testPlayer1.getName();
        assertEquals("Bob", name, "Player was expected to be named 'Bob' but was called " + name + " instead.");
    }

    @Test
    public void test_no_Game() {
        assertNull(testPlayer1.getGame(), "Player Bob isn't supposed to be assigned a game yet.");
    }

    @Test
    public void test_not_Playing() {
        assertFalse(testPlayer1.isPlayingGame(), "Player Bob's status shouldn't be playing a game yet.");
    }

    @Test
    public void test_is_Playing() {
        testPlayer1.setStatus(Player.gameStatus.PLAYING_GAME);
        assertTrue(testPlayer1.isPlayingGame(), "Player Bob's status should be playing a game.");
    }

    @Test
    public void test_has_Game() {
        GameController testGame = new GameController(board_1, board_2, validator, games);
        testPlayer1.setGame(testGame);
        //GameController testGame = new GameController(testPlayer1, testPlayer2, games);
        //testPlayer1.setGame(testGame);
        assertNotNull(testPlayer1.getGame(), "Player Bob should've been assigned a game.");
    }

    @Test
    public void test_same_Player() {
        assertTrue(testPlayer1.equals(testPlayer1), "Player Bob should be the same as Player Bob.");
    }

    @Test
    public void test_not_same_Player(){
        assertFalse(testPlayer1.equals(testPlayer2),"Player Bob shouldn't be the same as Player Sarah.");
    }
}
