package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameController;
import com.webcheckers.appl.Games;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.model.checkers.BoardView;
import com.webcheckers.model.checkers.Move;
import com.webcheckers.model.checkers.MoveValidator;
import com.webcheckers.model.checkers.Position;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class PostBackupRouteTest {

    static final String SESSION_GAME_ATTR = "GAME";

    Request mockQuest;
    Session mockSession;
    Response mockSponse;
    Player testPlayer = new Player("Tester");
    Player testedPlayer = new Player("Tested");
    Games games = mock(Games.class);
    Gson gson = new Gson();
    private BoardView board_1 = mock(BoardView.class);
    private BoardView board_2 = mock(BoardView.class);
    private MoveValidator validator = mock(MoveValidator.class);

    public PostBackupRouteTest() {
        mockQuest = mock(Request.class);
        mockSession = mock(Session.class);
        mockSponse = mock(Response.class);
        when(mockQuest.session()).thenReturn(mockSession);
    }
/*
    @Test
    public void NewGameBackupTest() {
        when(mockSession.attribute(SESSION_GAME_ATTR)).thenReturn(new GameController(board_1, board_2, validator, games));
        PostBackupRoute routeToTest = new PostBackupRoute();
        try {
            Object returned = routeToTest.handle(mockQuest, mockSponse);
            assertTrue(returned instanceof String, "Response did not return a message string");
            String messageString = (String)returned;
            Message returnMessage = gson.fromJson(messageString, Message.class);
            assertNotNull(returnMessage, "Handle did not return a valid message string");
            assertEquals(returnMessage.getType(), Message.Type.ERROR, "Backing up without making a move should return error");
        } catch (Exception e) {
            fail("Method with no exception causes caused exception");
        }
    }

    @Test
    public void gameWithMoveBackupTest() {
        GameController myGame = new GameController(board_1, board_2, validator, games);
        Move testMove = new Move(new Position(0, 1), new Position(1,0));
        when(myGame.validateMove(any())).thenReturn(true);
        myGame.validateMove(testMove);
        when(mockSession.attribute(SESSION_GAME_ATTR)).thenReturn(myGame);
        PostBackupRoute routeToTest = new PostBackupRoute();
        try {
            Object returned = routeToTest.handle(mockQuest, mockSponse);
            assertTrue(returned instanceof String, "Response did not return a message string");
            String messageString = (String)returned;
            Message returnMessage = gson.fromJson(messageString, Message.class);
            assertNotNull(returnMessage, "Handle did not return a valid message string");
            assertEquals(returnMessage.getType(), Message.Type.INFO, "Backing up with making a move should return info");
        } catch (Exception e) {
            fail("Method with no exception causes caused exception");
        }
    }*/
}
