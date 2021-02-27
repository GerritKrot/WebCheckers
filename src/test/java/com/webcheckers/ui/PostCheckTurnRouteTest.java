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
 * Unit test for PostCheckTurnRoute
 * Last Revision: 10/19/2020
 * @author Michael Canning
 */
@Tag("UI-tier")
public class PostCheckTurnRouteTest {

    private Session session;
    private Request request;
    private Response response;
    private GameController game;
    private Player player;
    private PostCheckTurnRoute postCheckTurnRoute;

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
        this.postCheckTurnRoute = new PostCheckTurnRoute();
        when(request.session()).thenReturn(session);
    }

    /**
     * Test for when it is a user's turn
     */
    @Test
    public void is_users_turn() {
        when(session.attribute(PostCheckTurnRoute.SESSION_GAME_ATTR)).thenReturn(game);
        when(session.attribute(PostCheckTurnRoute.SESSION_PLAYER_ATTR)).thenReturn(player);
        when(game.checkPlayerActive(player)).thenReturn(true);
        String returned = "";
        try {
            returned = (String)postCheckTurnRoute.handle(request, response);
        } catch (Exception e) {
            fail();
        }
        assert(returned.contains("true"));
    }

    /**
     * Test for when it is not a user's turn
     */
    @Test
    public void is_not_users_turn() {
        when(session.attribute(PostCheckTurnRoute.SESSION_GAME_ATTR)).thenReturn(game);
        when(session.attribute(PostCheckTurnRoute.SESSION_PLAYER_ATTR)).thenReturn(player);
        when(game.checkPlayerActive(player)).thenReturn(false);
        String returned = "";
        try {
            returned = (String)postCheckTurnRoute.handle(request, response);
        } catch (Exception e) {
            fail();
        }
        assert(returned.contains("false"));
    }
}
