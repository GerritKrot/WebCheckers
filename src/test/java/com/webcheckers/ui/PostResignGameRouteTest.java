package com.webcheckers.ui;

import com.webcheckers.appl.GameController;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@Tag("UI-tier")
/**
 * Unit test for PostResignGameRoute
 * Last Revision: 10/12/2020
 * @author Michael Canning
 */
public class PostResignGameRouteTest {
    private Session session;
    private Request request;
    private Response response;
    private PlayerLobby playerLobby;
    private Player player;
    private GameController game;
    private PostResignGameRoute postResignGameRoute;

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
        this.postResignGameRoute = new PostResignGameRoute(playerLobby);
        when(request.session()).thenReturn(session);
    }

    /**
     * Test for player resigning when not in a game
     */
    @Test
    public void resignNotInGame() {
        when(player.isPlayingGame()).thenReturn(false);
        when(session.attribute(PostResignGameRoute.SESSION_PLAYER_ATTR)).thenReturn(player);
        String result = "";
        try {
            result = (String) postResignGameRoute.handle(request, response);
        } catch (Exception e) {
            fail();
        }
        assert(result.contains(PostResignGameRoute.NOT_PLAYING_GAME_MESSAGE));
        verify(player, never()).getGame();
    }

    /**
     * Test for player resigning after the other player has already resigned
     */
    @Test
    public void resignOtherPlayerResigned() {
        GameController.winLossStatus result_status = GameController.winLossStatus.WON;
        when(player.isPlayingGame()).thenReturn(true);
        when(player.getGame()).thenReturn(game);
        when(session.attribute(PostResignGameRoute.SESSION_PLAYER_ATTR)).thenReturn(player);
        when(game.resign(player)).thenReturn(result_status);
        String result = "";
        try {
            result = (String) postResignGameRoute.handle(request, response);
        } catch (Exception e) {
            fail();
        }
        assert(result.contains(PostResignGameRoute.OTHER_PLAYER_FORFEITED));
        verify(game).resign(player);
    }

    /**
     * Test for a normal and successful resignation
     */
    @Test
    public void resignNormal() {
        GameController.winLossStatus result_status = GameController.winLossStatus.LOST;
        when(player.isPlayingGame()).thenReturn(true);
        when(player.getGame()).thenReturn(game);
        when(session.attribute(PostResignGameRoute.SESSION_PLAYER_ATTR)).thenReturn(player);
        when(game.resign(player)).thenReturn(result_status);
        String result = "";
        try {
            result = (String) postResignGameRoute.handle(request, response);
        } catch (Exception e) {
            fail();
        }
        assert(result.contains(PostResignGameRoute.FORFEIT_MESSAGE));
        verify(game).resign(player);
    }

}
