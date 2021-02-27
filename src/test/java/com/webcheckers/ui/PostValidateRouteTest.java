package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.appl.GameController;
import com.webcheckers.model.checkers.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

/**
 * Unit test for PostValidateRoute
 * Last Revision: 10/19/2020
 * @author Michael Canning
 */
@Tag("UI-tier")
public class PostValidateRouteTest {

    private Session session;
    private Request request;
    private Response response;
    private GameController game;
    private PostValidateRoute postValidateRoute;
    private Move move;
    private Gson gson;

    /**
     * Sets up mock objects for CuT as well as initializes the CuT
     */
    @BeforeEach
    public void setup() {
        this.session = mock(Session.class);
        this.request = mock(Request.class);
        this.response = mock(Response.class);
        this.game = mock(GameController.class);
        this.gson = new Gson();
        postValidateRoute = new PostValidateRoute(gson);
        when(request.session()).thenReturn(session);
    }

    @Test
    public void test_init() {
        this.move = mock(Move.class);
        this.gson = new Gson();
        when(request.session()).thenReturn(session);
        postValidateRoute = new PostValidateRoute(gson);
    }

    /**
     * Test for return for a valid move
     */
    @Test
    public void valid_move() {
        String test_string = "";
        when(request.session().attribute(PostValidateRoute.SESSION_GAME_ATTR)).thenReturn(game);
        when(request.queryParams("actionData")).thenReturn(test_string);
        when(game.validateMove(any())).thenReturn(true);
        try {
            String result = (String)postValidateRoute.handle(request, response);
            assert(result.contains("INFO"));
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test for return for a invalid move
     */
    @Test
    public void invalid_move() {
        String test_string = "";
        when(request.session().attribute(PostValidateRoute.SESSION_GAME_ATTR)).thenReturn(game);
        when(request.queryParams("actionData")).thenReturn(test_string);
        when(game.validateMove(any())).thenReturn(false);
        try {
            String result = (String)postValidateRoute.handle(request, response);
            assert(result.contains("ERROR"));
        } catch (Exception e) {
            fail();
        }
    }
}
