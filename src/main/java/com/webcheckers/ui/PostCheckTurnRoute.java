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
 * This class contains the state and behavior associated with a PostCheckTurnRoute
 * object, which handles POST requests for the check turn route
 * Last Revision: 9/30/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class PostCheckTurnRoute implements Route {

    static final String SESSION_PLAYER_ATTR = "PLAYER";
    static final String SESSION_GAME_ATTR = "GAME";

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Session httpSession = request.session();
        Player player = httpSession.attribute(SESSION_PLAYER_ATTR);
        GameController myGame = httpSession.attribute(SESSION_GAME_ATTR);
        return Message.info(Boolean.toString(myGame.checkPlayerActive(player))).getJSON();
    }
}