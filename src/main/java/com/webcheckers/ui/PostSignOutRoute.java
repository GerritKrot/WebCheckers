package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

/**
 * This class handles POST requests to the signout route. A POST request is made with no variables
 * and then the user that made the post request has their "PLAYER" session variable cleared along with
 * their player object being removed from playerLobby.
 *
 * Last Revision: 09/29/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class PostSignOutRoute implements Route {

    private final PlayerLobby playerLobby;
    static final String SESSION_PLAYER_ATTR = "PLAYER";
    static final String SESSION_GAME_ATTR = "GAME";
    /**
     * Constructor for PostSignOutRoute, sets the template engine and player lobby being used to
     * handle the signout request
     * @param playerLobby The PlayerLobby object being used to store players currently online
     */
    public PostSignOutRoute(PlayerLobby playerLobby) {
        this.playerLobby = playerLobby;
    }

    /**
     * Handles POST requests to /signout. This does not take any arguments, but instead works using the
     * user's session. It removes the PLAYER session variable, and removes the player from PlayerLobby.
     *
     * If a player is in a game when they sign out they will be forfeited from their current game
     *
     * @param request The POST request sent to the server for /signout
     * @param response The response being built to return to the user
     * @return A redirect to /
     * @throws Exception Throws an exception if a user is not properly signed-in
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Session session = request.session();

        // If the user is not signed in, send them home
        if(session.attribute(SESSION_PLAYER_ATTR) == null) {
            response.redirect("/");
            return "<html>Redirecting...</html>";
        }

        Player player = session.attribute(SESSION_PLAYER_ATTR);
        if(player.isPlayingGame()) {
            player.getGame().resign(player);
        }
        playerLobby.removePlayer(player.getName());
        session.removeAttribute(SESSION_PLAYER_ATTR);
        session.removeAttribute(SESSION_GAME_ATTR);
        response.redirect("/");
        return "<html>Redirecting...</html>";
    }
}
