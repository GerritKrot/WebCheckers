package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameController;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.checkers.Move;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.Objects;
import java.util.logging.Logger;

public class PostValidateRoute implements Route {

    private final Gson gson;
    private static final Logger LOG = Logger.getLogger(PostValidateRoute.class.getName());
    static final String SESSION_GAME_ATTR = "GAME";

    public PostValidateRoute(final Gson gson) {
        Objects.requireNonNull(gson);
        this.gson = gson;
        LOG.config("PostValidateRoute is initialized.");

    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        GameController myGame = httpSession.attribute(SESSION_GAME_ATTR);
        String move2CheckJSON = request.queryParams("actionData");
        Move move2Check = gson.fromJson(move2CheckJSON, Move.class);
        if (myGame.validateMove(move2Check)) {
            return Message.info("Valid move").getJSON();
        } else {
            return Message.error("Invalid move (Did you miss a jump?)").getJSON();
        }
    }
}
