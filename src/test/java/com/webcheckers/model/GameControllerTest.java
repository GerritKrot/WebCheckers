package com.webcheckers.model;

import com.webcheckers.appl.GameController;
import com.webcheckers.appl.Games;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.checkers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.xml.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * a class to test the functionality of the GameController class
 * Last Revision: 10/12/2020
 * @author Payton Burak
 */
@Tag("Model-tier")
public class GameControllerTest {

    private Player player1 = new Player("Player 1");
    private Player player2 = new Player("Player 2");
    BoardView redBoard = new BoardView(true) ;
    BoardView whiteBoard = new BoardView(false);
    private Games games = mock(Games.class);
    GameController gameController;

    @BeforeEach
    private void setup() {
        gameController = new GameController(redBoard, whiteBoard, new MoveValidator(redBoard,whiteBoard), games);
        gameController.setPlayers(player1, player2);
    }
  
    @Test
    public void test_no_board_player1(){
        assertNotNull(gameController.getBoard(player1), "Board accessing was unsuccessful for player 1");
    }

    @Test
    public void test_no_board_player2(){
        assertNotNull(gameController.getBoard(player2), "Board accessing was unsuccessful for player 2");
    }

    @Test
    public void test_no_red_player(){
        assertNotNull(gameController.getRedPlayer(), "The red player was not properly passed into the GameController");
    }

    @Test
    public void test_no_white_player(){
        assertNotNull(gameController.getWhitePlayer(), "The white player was not properly passed into the GameController");
    }

    @Test
    public void test_get_red_player(){
        Player testPlayer = gameController.getRedPlayer();
        assertEquals(testPlayer.getName(), player1.getName(), "The expected red player was " + player1.getName() +
                " but got " + testPlayer.getName() + " instead");
    }

    @Test
    public void test_get_white_player(){
        Player testPlayer = gameController.getWhitePlayer();
        assertEquals(testPlayer.getName(), player2.getName(), "The expected white player was " + player2.getName() +
                " but got " + testPlayer.getName() + " instead");
    }


    @Test
    public void test_resign(){
        Player test = new Player("test");
        GameController.winLossStatus result = gameController.resign(test);
        assertEquals(result, GameController.winLossStatus.LOST, "Incorrect win lost status");
        assertNull(test.getGame());
        assertEquals(test.getStatus(), Player.gameStatus.NOT_PLAYING_GAME);
    }

    @Test
    public void test_check_player_active(){
        Player red = new Player("red");
        Player white = new Player("white");
        BoardView redBoard = new BoardView(true) ;
        BoardView whiteBoard = new BoardView(false);
        gameController = new GameController(redBoard, whiteBoard, new MoveValidator(redBoard,whiteBoard), games);
        gameController.setPlayers(red, white);
        boolean result = gameController.checkPlayerActive(red);
        boolean result_fail = gameController.checkPlayerActive(white);
        assertTrue(result);
        assertFalse(result_fail);
    }

    @Test
    public void test_player_resigned(){
        boolean result = gameController.checkIfOtherPlayerResigned();
        assertFalse(result);
    }
}
