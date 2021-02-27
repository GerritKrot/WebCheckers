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

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

/**
 * Unit test for GetSignInRoute
 * Last Revision: 10/12/2020
 * @author Michael Canning
 */
@Tag("UI-tier")
public class PostSignOutRouteTest {

    private Session session;
    private Request request;
    private Response response;
    private PlayerLobby playerLobby;
    private GameController game;
    private PostSignOutRoute postSignOutRoute;
    private Player player;

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
        this.postSignOutRoute = new PostSignOutRoute(playerLobby);
        when(request.session()).thenReturn(session);
    }

    /**
     * Test for a user signing out when not signed in
     */
    @Test
    public void signOutNotSignedIn() {
        String test_player_name = "player";
        when(request.session().attribute(PostSignOutRoute.SESSION_PLAYER_ATTR)).thenReturn(null);
        try{
            postSignOutRoute.handle(request, response);
        } catch(Exception exception) {
            fail();
        }
        verify(playerLobby, never()).removePlayer(test_player_name);
        verify(game, never()).resign(player);
        verify(session, never()).removeAttribute(PostSignOutRoute.SESSION_PLAYER_ATTR);
        verify(session, never()).removeAttribute(PostSignOutRoute.SESSION_GAME_ATTR);
    }

    /**
     * Test for a user signing out while in a game
     */
    @Test
    public void signOutInGame() {
        String test_player_name = "player";
        when(player.getName()).thenReturn(test_player_name);
        when(player.isPlayingGame()).thenReturn(true);
        when(request.session().attribute(PostSignOutRoute.SESSION_PLAYER_ATTR)).thenReturn(player);
        when(player.getGame()).thenReturn(game);
        try{
            postSignOutRoute.handle(request, response);
        } catch(Exception exception) {
            fail();
        }
        verify(playerLobby).removePlayer(test_player_name);
        verify(game).resign(player);
        verify(session).removeAttribute(PostSignOutRoute.SESSION_PLAYER_ATTR);
        verify(session).removeAttribute(PostSignOutRoute.SESSION_GAME_ATTR);
    }

    /**
     * Test for a user signing out when not in a game
     */
    @Test
    public void signOutNotInGame() {
        String test_player_name = "player";
        when(player.getName()).thenReturn(test_player_name);
        when(player.isPlayingGame()).thenReturn(false);
        when(request.session().attribute(PostSignOutRoute.SESSION_PLAYER_ATTR)).thenReturn(player);
        try{
            postSignOutRoute.handle(request, response);
        } catch(Exception exception) {
            fail();
        }
        verify(playerLobby).removePlayer(test_player_name);
        verify(session).removeAttribute(PostSignOutRoute.SESSION_PLAYER_ATTR);
        verify(session).removeAttribute(PostSignOutRoute.SESSION_GAME_ATTR);
        verify(game, never()).resign(player);
    }

}
