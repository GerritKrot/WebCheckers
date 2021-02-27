package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameController;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.model.checkers.CheckerPieceColor;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * This class contains the necessary state and behavior to handle
 * GET requests for the game route to allows the player to stay in the game
 * and updates the game board
 * Last Revision: 9/29/2020
 *
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class GetGameRoute implements Route {
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    static final String PLAYER_OBJECT_ATTR = "currentUser";
    static final String GET_GAME = "Game";
    static final String RED_PLAYER_ATTR = "redPlayer";
    static final String WHITE_PLAYER_ATTR = "whitePlayer";
    static final String GAME_MODE = "viewMode";
    static final String SESSION_PLAYER_ATTR = "PLAYER";
    static final String SESSION_GAME_ATTR = "GAME";
    static final String ACTIVE_COLOR_ATTR = "activeColor";
    static final String SESSION_MESSAGE_ATTR = "message";

    /**
     * constructor initializes a template engine and a player lobby object to associate
     * with the GetGameRoute instance
     *
     * @param templateEngine the templateEngine to be assigned to this GetGameRoute object
     * @param playerLobby    the playerLobby to be assigned to this GetGameRoute object
     */
    public GetGameRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby) {
        Objects.requireNonNull(templateEngine);
        Objects.requireNonNull(playerLobby);
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
        LOG.config("GetGameRoute is initialized.");
    }

    /**
     * handle ensures that HTTP requests are handled properly
     *
     * @param request  request to be handled by the handle method
     * @param response he response to be sent from the request
     * @return the html string representing the request being handled
     * @throws Exception ignored
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("GetGameRoute is invoked.");

        final Session httpSession = request.session(); //Get the current session
        Map<String, Object> vm = new HashMap<>();
        // If the user is not signed in send them to the sign-in page, they should not
        // be here.
        if (httpSession.attribute(SESSION_PLAYER_ATTR) == null) {
            response.redirect("/");
            return "<html>Redirecting...</html>";
        } else {
            Player player = httpSession.attribute(SESSION_PLAYER_ATTR);

            if (httpSession.attribute(SESSION_GAME_ATTR) == null) {
                httpSession.attribute(SESSION_GAME_ATTR, player.getGame());
            }
            GameController myGame = httpSession.attribute(SESSION_GAME_ATTR);
            // If the other player forfeited send this user back home with a win
            Gson gson = new Gson();
            if (myGame.checkIfOtherPlayerResigned()) {
                //httpSession.attribute(SESSION_MESSAGE_ATTR, Message.info("The other player forfeited, so you won!"));
                //response.redirect("/");
                final Map<String, Object> modeOptions = new HashMap<>(2);
                modeOptions.put("isGameOver", true);
                modeOptions.put("gameOverMessage", "The other player forfeited, so you won!");
                vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
                player.setStatus(Player.gameStatus.NOT_PLAYING_GAME);
                player.setGame(null);
                httpSession.removeAttribute(SESSION_GAME_ATTR);
            }
            // Handle win loss condition
            if (myGame.checkGameStatus().equals("WHITE") && myGame.getWhitePlayer().equals(player)) {
                //httpSession.attribute(SESSION_MESSAGE_ATTR, Message.info("You won the game!"));
                //response.redirect("/");
                final Map<String, Object> modeOptions = new HashMap<>(2);
                modeOptions.put("isGameOver", true);
                modeOptions.put("gameOverMessage", "You won the game!");
                vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
                player.setStatus(Player.gameStatus.NOT_PLAYING_GAME);
                player.setGame(null);
                httpSession.removeAttribute(SESSION_GAME_ATTR);
                player.win(myGame.getRedPlayer().getRank());
                myGame.endGame();
            } else if (myGame.checkGameStatus().equals("RED") && myGame.getWhitePlayer().equals(player)) {
                //httpSession.attribute(SESSION_MESSAGE_ATTR, Message.info("You lost the game!"));
                //response.redirect("/");
                final Map<String, Object> modeOptions = new HashMap<>(2);
                modeOptions.put("isGameOver", true);
                modeOptions.put("gameOverMessage", "You lost the game!");
                vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
                player.setStatus(Player.gameStatus.NOT_PLAYING_GAME);
                player.setGame(null);
                httpSession.removeAttribute(SESSION_GAME_ATTR);
                player.loss(myGame.getRedPlayer().getRank());
                myGame.endGame();
            } else if (myGame.checkGameStatus().equals("RED") && myGame.getRedPlayer().equals(player)) {
                //httpSession.attribute(SESSION_MESSAGE_ATTR, Message.info("You won the game!"));
                //response.redirect("/");
                final Map<String, Object> modeOptions = new HashMap<>(2);
                modeOptions.put("isGameOver", true);
                modeOptions.put("gameOverMessage", "You won the game!");
                vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
                player.setStatus(Player.gameStatus.NOT_PLAYING_GAME);
                player.setGame(null);
                httpSession.removeAttribute(SESSION_GAME_ATTR);
                player.win(myGame.getWhitePlayer().getRank());
                myGame.endGame();
            } else if (myGame.checkGameStatus().equals("WHITE") && myGame.getRedPlayer().equals(player)) {
                //httpSession.attribute(SESSION_MESSAGE_ATTR, Message.info("You lost the game!"));
                //response.redirect("/");
                final Map<String, Object> modeOptions = new HashMap<>(2);
                modeOptions.put("isGameOver", true);
                modeOptions.put("gameOverMessage", "You lost the game!");
                vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
                player.setStatus(Player.gameStatus.NOT_PLAYING_GAME);
                player.setGame(null);
                httpSession.removeAttribute(SESSION_GAME_ATTR);
                player.loss(myGame.getWhitePlayer().getRank());
                myGame.endGame();
            }
            vm.put(PLAYER_OBJECT_ATTR, player);
            Player redPlayer = myGame.getRedPlayer();
            vm.put(RED_PLAYER_ATTR, redPlayer);
            vm.put(WHITE_PLAYER_ATTR, myGame.getWhitePlayer());
            if (myGame.checkPlayerActive(redPlayer)) {
                vm.put(ACTIVE_COLOR_ATTR, "RED");
                vm.put("board", myGame.getBoard(player));
            } else {
                vm.put(ACTIVE_COLOR_ATTR, "WHITE");
                vm.put("board", myGame.getBoard(player));
            }
            vm.put(GAME_MODE, "PLAY");

        }
        vm.put("title", GET_GAME);

        // render the View
        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
}
