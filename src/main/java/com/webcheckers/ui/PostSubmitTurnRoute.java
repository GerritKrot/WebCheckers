package com.webcheckers.ui;

import com.webcheckers.appl.GameController;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

public class PostSubmitTurnRoute implements Route {

    static final String SESSION_GAME_ATTR = "GAME";

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        GameController myGame = httpSession.attribute(SESSION_GAME_ATTR);
        if(myGame.checkAndSubmitTurn()) {
            return Message.info("Turn submitted successfully").getJSON();
        } else {
            return Message.error("Turn incomplete. You must make a move, and you must complete all multi-jumps").getJSON();
        }
    }
}
