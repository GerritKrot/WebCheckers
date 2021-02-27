package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.GameController;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

/**
 * Unit test for GetGameRoute
 * Last Revision: 10/19/2020
 * @author Michael Canning
 */
@Tag("UI-tier")
public class GetGameRouteTest {
    private Session session;
    private Request request;
    private Response response;
    private PlayerLobby playerLobby;
    private GameController game;
    private GetGameRoute getGameRoute;
    private Player player;
    private TemplateEngine templateEngine;
    private Player redPlayer;
    private Player whitePlayer;

    /**
     * Sets up mock objects for CuT as well as initializes the CuT
     */
    @BeforeEach
    public void setup() {
        this.session = mock(Session.class);
        this.request = mock(Request.class);
        this.response = mock(Response.class);
        this.playerLobby = mock(PlayerLobby.class);
        this.game = mock(GameController.class);
        this.player = mock(Player.class);
        this.redPlayer = mock(Player.class);
        this.whitePlayer = mock(Player.class);
        this.templateEngine = mock(TemplateEngine.class);
        this.getGameRoute = new GetGameRoute(templateEngine, playerLobby);
        when(request.session()).thenReturn(session);
    }

    /**
     * Test for a non-signed in user
     */
    @Test
    public void not_signed_in() {
        when(session.attribute(GetGameRoute.SESSION_PLAYER_ATTR)).thenReturn(null);
        try {
            getGameRoute.handle(request, response);
        } catch (Exception e) {
            fail();
        }
        verify(response).redirect("/");
    }

    /**
     * Test for a player joining a game where the other player forfeited
     */
    @Test
    public void other_player_resigned() {
        when(session.attribute(GetGameRoute.SESSION_PLAYER_ATTR)).thenReturn(player);
        when(session.attribute(GetGameRoute.SESSION_GAME_ATTR)).thenReturn(game);
        when(game.checkIfOtherPlayerResigned()).thenReturn(true);

        String return_val = "";

        try {
            return_val = (String)getGameRoute.handle(request, response);
        } catch (Exception e) {
            fail();
        }
        assert(return_val.contains("forfeited"));
        verify(response).redirect("/");
    }

    /**
     * Test for the user joining a game where the white player is the active player
     */
    @Test
    public void white_player_active() {
        when(session.attribute(GetGameRoute.SESSION_PLAYER_ATTR)).thenReturn(player);
        when(session.attribute(GetGameRoute.SESSION_GAME_ATTR)).thenReturn(game);
        when(game.checkIfOtherPlayerResigned()).thenReturn(false);
        when(game.getRedPlayer()).thenReturn(redPlayer);
        when(game.getWhitePlayer()).thenReturn(whitePlayer);
        when(game.checkPlayerActive(redPlayer)).thenReturn(true);
        when(game.checkPlayerActive(redPlayer)).thenReturn(false);
        try {
            getGameRoute.handle(request, response);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test for the user joining a game where the red player is the active player
     */
    @Test
    public void red_player_active() {
        when(session.attribute(GetGameRoute.SESSION_PLAYER_ATTR)).thenReturn(player);
        when(session.attribute(GetGameRoute.SESSION_GAME_ATTR)).thenReturn(game);
        when(game.checkIfOtherPlayerResigned()).thenReturn(false);
        when(game.getRedPlayer()).thenReturn(redPlayer);
        when(game.getWhitePlayer()).thenReturn(whitePlayer);
        when(game.checkPlayerActive(redPlayer)).thenReturn(false);
        when(game.checkPlayerActive(redPlayer)).thenReturn(true);
        try {
            getGameRoute.handle(request, response);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test for the user not having a game session variable set
     */
    @Test
    public void no_game_session_variable() {
        when(session.attribute(GetGameRoute.SESSION_PLAYER_ATTR)).thenReturn(player);
        when(session.attribute(GetGameRoute.SESSION_GAME_ATTR)).thenReturn(null);
        when(game.checkIfOtherPlayerResigned()).thenReturn(false);
        when(game.getRedPlayer()).thenReturn(redPlayer);
        when(game.getWhitePlayer()).thenReturn(whitePlayer);
        when(game.checkPlayerActive(redPlayer)).thenReturn(false);
        when(game.checkPlayerActive(redPlayer)).thenReturn(true);
        try {
            getGameRoute.handle(request, response);
        } catch (Exception e) {
            //Do nothing, this is expected to fail
        }
        verify(player).getGame();
    }

}
