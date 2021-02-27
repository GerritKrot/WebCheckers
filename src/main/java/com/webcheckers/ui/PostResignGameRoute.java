package com.webcheckers.ui;

import com.webcheckers.appl.GameController;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

/**
 * This class handles POST requests to /resignGame. This includes functionality for
 * resigning a user from a game they are in.
 * Last Revision: 10/10/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class PostResignGameRoute implements Route {

    static final String SESSION_PLAYER_ATTR = "PLAYER";
    static final String SESSION_GAME_ATTR = "GAME";
    static final String SESSION_MESSAGE_ATTR = "message";
    static final String NOT_PLAYING_GAME_MESSAGE = "You are not playing a game.";
    static final String FORFEIT_MESSAGE = "You forfeited from the game.";
    static final String OTHER_PLAYER_FORFEITED = "The other player already forfeited, so you won!";
    private PlayerLobby lobby;

    /**
     * The constructor for PostResignGameRoute
     * @param lobby The lobby being used to track players
     */
    public PostResignGameRoute(PlayerLobby lobby) {
        this.lobby = lobby;
    }

    /**
     * Handles post reuqests for /resignGame
     * @param request The request being made by the user
     * @param response The response being constructed to return to the user
     * @return Returns a Message of INFO type if the resignation worked, or ERROR if it did not
     * @throws Exception Throws an exception if the state of the server is invalid
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        Player player = httpSession.attribute(SESSION_PLAYER_ATTR);
        if(!player.isPlayingGame()) {
            return Message.error(NOT_PLAYING_GAME_MESSAGE).getJSON();
        }
        GameController.winLossStatus result = player.getGame().resign(player);
        Message message = Message.info(this.FORFEIT_MESSAGE);
        if(result == GameController.winLossStatus.WON) {
            message = Message.info(this.OTHER_PLAYER_FORFEITED);
        }
        httpSession.attribute(SESSION_MESSAGE_ATTR, message);
        httpSession.removeAttribute(SESSION_GAME_ATTR);
        return message.getJSON();
    }
}
