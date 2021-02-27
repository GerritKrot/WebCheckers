package com.webcheckers.ui;

import com.webcheckers.appl.GameController;
import com.webcheckers.appl.PlayerLobby;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

public class PostBackupRoute implements Route {

    static final String SESSION_GAME_ATTR = "GAME";

    /**
     * Handles a POST request from the /backup route. This returns the game state prior to
     * the not submitted move
     * @param request the request that was made to the spark server
     * @param response the response that will be returned to the user
     * @return the object representing the handling of this request
     * @throws Exception ignored
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        GameController myGame = httpSession.attribute(SESSION_GAME_ATTR);
        if(myGame.backup()) {
            return "{\"type\": \"INFO\", \"message\": \"" + "Last move removed" + "\"}";
        } else {
            return "{\"type\": \"ERROR\", \"message\": \"" + "Last move not removed" + "\"}";
        }
    }
}
