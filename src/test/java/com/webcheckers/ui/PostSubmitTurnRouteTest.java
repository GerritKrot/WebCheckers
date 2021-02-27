package com.webcheckers.ui;

import com.webcheckers.appl.GameController;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test for PostSubmitTurnRoute
 * Last Revision: 10/19/2020
 * @author Michael Canning
 */
@Tag("UI-tier")
public class PostSubmitTurnRouteTest {

    private Session session;
    private Request request;
    private Response response;
    private GameController game;
    private Player player;
    private PostSubmitTurnRoute postSubmitTurnRoute;

    /**
     * Sets up mock objects for CuT as well as initializes the CuT
     */
    @BeforeEach
    public void setup() {
        this.session = mock(Session.class);
        this.request = mock(Request.class);
        this.response = mock(Response.class);
        this.game = mock(GameController.class);
        this.player = mock(Player.class);
        this.postSubmitTurnRoute = new PostSubmitTurnRoute();
        when(request.session()).thenReturn(session);
    }

    /**
     * Test for an invalid submitted turn
     */
    @Test
    public void invalid_turn() {
        when(session.attribute(PostSubmitTurnRoute.SESSION_GAME_ATTR)).thenReturn(game);
        when(game.checkAndSubmitTurn()).thenReturn(false);
        String returned = "";
        try {
            returned = (String)postSubmitTurnRoute.handle(request, response);
        } catch (Exception e) {
            fail();
        }
        assert returned.contains("ERROR");
    }

    /**
     * Test for a valid submitted turn
     */
    @Test
    public void valid_turn() {
        when(session.attribute(PostSubmitTurnRoute.SESSION_GAME_ATTR)).thenReturn(game);
        when(game.checkAndSubmitTurn()).thenReturn(true);
        String returned = "";
        try {
            returned = (String)postSubmitTurnRoute.handle(request, response);
        } catch (Exception e) {
            fail();
        }
        assert returned.contains("INFO");
    }
}
