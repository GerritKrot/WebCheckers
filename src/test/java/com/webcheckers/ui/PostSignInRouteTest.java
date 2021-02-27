package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;


/**
 * Unit test for PostSignInRoute
 * Last Revision: 10/12/2020
 * @author Michael Canning
 */
@Tag("UI-tier")
public class PostSignInRouteTest {

    private Session session;
    private Request request;
    private Response response;
    private PlayerLobby playerLobby;
    private PostSignInRoute postSignInRoute;
    private TemplateEngine templateEngine;
    private Player player;
    private Map<String, Object> vm;

    /**
     * Sets up mock objects for CuT as well as initializes the CuT
     */
    @BeforeEach
    public void setup() {
        this.session = mock(Session.class);
        this.request = mock(Request.class);
        this.response = mock(Response.class);
        this.playerLobby = mock(PlayerLobby.class);
        this.templateEngine = mock(TemplateEngine.class);
        this.postSignInRoute = new PostSignInRoute(templateEngine, playerLobby);
        when(request.session()).thenReturn(session);
        this.player = mock(Player.class);
        this.vm = mock(HashMap.class);
    }

    /**
     * Tests to make sure non-alphanumeric usernames are not accepted
     */
    @Test
    public void non_alphanum_username() {
        String username = "D$$DB##F";
        when(request.queryParams(PostSignInRoute.USERNAME_PARAM)).thenReturn(username);
        try {
            postSignInRoute.handle(request, response);
        } catch (Exception exception) {
            fail();
        }
        verify(playerLobby, never()).addPlayer(username);
        verify(session, never()).attribute(PostSignInRoute.SESSION_PLAYER_ATTR, player);
        verify(response, never()).redirect("/");
    }

    /**
     * Tests to make sure usernames starting with spaces are not accepted
     */
    @Test
    public void start_with_space() {
        String username = " test";
        when(request.queryParams(PostSignInRoute.USERNAME_PARAM)).thenReturn(username);
        try {
            postSignInRoute.handle(request, response);
        } catch (Exception exception) {
            fail();
        }
        verify(playerLobby, never()).addPlayer(username);
        verify(session, never()).attribute(PostSignInRoute.SESSION_PLAYER_ATTR, player);
        verify(response, never()).redirect("/");
    }

    /**
     * Tests to make sure usernames ending with spaces are not accepted
     */
    @Test
    public void end_with_space() {
        String username = "test ";
        when(request.queryParams(PostSignInRoute.USERNAME_PARAM)).thenReturn(username);
        try {
            postSignInRoute.handle(request, response);
        } catch (Exception exception) {
            fail();
        }
        verify(playerLobby, never()).addPlayer(username);
        verify(session, never()).attribute(PostSignInRoute.SESSION_PLAYER_ATTR, player);
        verify(response, never()).redirect("/");
    }

    /**
     * Tests to make sure usernames over 13 characters are not accepted
     */
    @Test
    public void too_long_username() {
        String username = "12345678901234";
        when(request.queryParams(PostSignInRoute.USERNAME_PARAM)).thenReturn(username);
        try {
            postSignInRoute.handle(request, response);
        } catch (Exception exception) {
            fail();
        }
        verify(playerLobby, never()).addPlayer(username);
        verify(session, never()).attribute(PostSignInRoute.SESSION_PLAYER_ATTR, player);
        verify(response, never()).redirect("/");
    }

    /**
     * Test to verify that blank usernames are not accepted
     */
    @Test
    public void blank_username() {
        String username = "";
        when(request.queryParams(PostSignInRoute.USERNAME_PARAM)).thenReturn(username);
        try {
            postSignInRoute.handle(request, response);
        } catch (Exception exception) {
            fail();
        }
        verify(playerLobby, never()).addPlayer(username);
        verify(session, never()).attribute(PostSignInRoute.SESSION_PLAYER_ATTR, player);
        verify(response, never()).redirect("/");
    }

    /**
     * Test to verify that duplicate usernames are not accepted
     */
    @Test
    public void duplicate_username() {
        String username = "Test123";
        when(request.queryParams(PostSignInRoute.USERNAME_PARAM)).thenReturn(username);
        when(playerLobby.addPlayer(username)).thenReturn(null);
        try {
            postSignInRoute.handle(request, response);
        } catch (Exception exception) {
            fail();
        }
        verify(session, never()).attribute(PostSignInRoute.SESSION_PLAYER_ATTR, player);
        verify(response, never()).redirect("/");
    }

    /**
     * Test to verify that valid usernames that are not taken are accepted
     */
    @Test
    public void valid_username() {
        String username = "Test123";
        when(request.queryParams(PostSignInRoute.USERNAME_PARAM)).thenReturn(username);
        when(playerLobby.addPlayer(username)).thenReturn(player);
        try {
            postSignInRoute.handle(request, response);
        } catch (Exception exception) {
            fail();
        }
        verify(playerLobby).addPlayer(username);
        verify(session).attribute(PostSignInRoute.SESSION_PLAYER_ATTR, player);
        verify(response).redirect("/");
    }

}
