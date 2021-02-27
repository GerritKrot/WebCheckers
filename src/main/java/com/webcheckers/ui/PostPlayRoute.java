package com.webcheckers.ui;

import com.webcheckers.appl.GameController;
import com.webcheckers.appl.Games;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.model.checkers.BoardView;
import com.webcheckers.model.checkers.MoveValidator;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * This class handles POST requests to /play. This is used when a player starts a game with a another
 * player. If the game request is possible (both players are available) it will setup a game using
 * PlayerLobby.
 *
 * Last Revision: 09/29/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class PostPlayRoute implements Route {
    private final TemplateEngine templateEngine;

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    static final String PLAYER_PARAM = "selectedPlayer";
    static final String TITLE_ATTR = "title";
    static final String PLAYER_OBJECT_ATTR = "currentUser";
    static final String RED_PLAYER_ATTR = "redPlayer";
    static final String WHITE_PLAYER_ATTR = "whitePlayer";
    static final String SESSION_PLAYER_ATTR = "PLAYER";
    static final String SESSION_GAME_ATTR = "GAME";
    static final String PLAYER_UNAVAILABLE = "That user was not available for a game.";
    static final String NOT_LOGGED_IN = "You cannot start a game without being signed in!";
    private final PlayerLobby playerLobby;
    private final Games games;

    /**
     * The constructor for PostPlayRoute. Sets the template engine and PlayerLobby object
     *
     * @param templateEngine The template engine used to render the HTML
     * @param playerLobby The player lobby used to setup the game
     */
    public PostPlayRoute(final TemplateEngine templateEngine, PlayerLobby playerLobby, Games games) {
        this.playerLobby = playerLobby;
        Objects.requireNonNull(templateEngine);
        this.templateEngine = templateEngine;
        this.games = games;
        LOG.config("PostPlayRoute is initialized.");
    }

    /**
     * Handles POST requests to /play. Sets up a game for 2 players using playerLobby
     *
     * @param request The request sent including the other player that the game is being made with
     * @param response The response being constructed for the user
     * @return HTML response to the /play POST, either a redirect for a game.ftl page
     * @throws Exception Will throw an exception if the internal state is invalid
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostPlayRoute is invoked.");
        final Session httpSession = request.session();
        if(httpSession.attribute(SESSION_PLAYER_ATTR) != null) { //Check if player is logged in
            if(request.queryParams(PLAYER_PARAM) == null) {
                httpSession.attribute("message", Message.error("You need to select a player to play against!"));
                response.redirect("/");
                return "<html>Redirecting...</html>";
            }
            Player player = httpSession.attribute(SESSION_PLAYER_ATTR);
            final String selectedName = request.queryParams(PLAYER_PARAM);
            BoardView redBoard = new BoardView(true);
            BoardView whiteBoard = new BoardView(false);
            MoveValidator validator = new MoveValidator(redBoard, whiteBoard);
            GameController newGame = new GameController(redBoard, whiteBoard, validator, this.games);
            playerLobby.startAGame(player, selectedName, newGame);
            if (newGame != null) {
                httpSession.attribute(SESSION_GAME_ATTR, newGame);
            } else {
                httpSession.attribute("message", Message.error(PLAYER_UNAVAILABLE));
                response.redirect("/");
                return "<html>Redirecting...</html>";
            }
        } else {
            httpSession.attribute("message", Message.error(NOT_LOGGED_IN));
            response.redirect("/signin");
            return "<html>Redirecting...</html>";
        }

        response.redirect("/game");
        return "<html>Redirecting...</html>";
    }
}
