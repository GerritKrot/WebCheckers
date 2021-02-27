package com.webcheckers.ui;

import com.webcheckers.Application;
import com.webcheckers.appl.GameController;
import com.webcheckers.appl.Games;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
    static final String PLAYER_OBJECT_ATTR = "currentUser";
    static final String TITLE_ATTR = "title";
    static final String PLAYER_LIST_ATTR = "player_list";
    static final String PLAYER_COUNT_ATTR = "player_count";
    static final String CURRENT_GAME_LIST_ATTR = "games";
    static final String SESSION_PLAYER_ATTR = "PLAYER";
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final Games games;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetHomeRoute(final TemplateEngine templateEngine, PlayerLobby playerLobby, Games games) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = playerLobby;
        this.games = games;
        //
        LOG.config("GetHomeRoute is initialized.");
    }

    /**
     * Render the WebCheckers Home page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetHomeRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(TITLE_ATTR, "Welcome!");

        // display a user message in the Home page
        vm.put("message", WELCOME_MSG);

        final Session httpSession = request.session(); //Get the current session

        if (httpSession.attribute(SESSION_PLAYER_ATTR) != null) { //Check if player is logged in
            Player player = httpSession.attribute(SESSION_PLAYER_ATTR);
            vm.put(PLAYER_OBJECT_ATTR, player);
            // If the player is in a game, send them to the /play page
            if (player.isPlayingGame()) {
                response.redirect("/game");
                return "<html>Redirecting...</html>";
            }


            Player[] players = playerLobby.getPlayers();
            String[] playerNames;
            playerNames = new String[players.length - 1];
            int playerNamesIndex = 0;
            for (Player value : players) {
                if (value.getName() != null && !value.getName().equals(player.getName()) && !value.isPlayingGame()) {
                    playerNames[playerNamesIndex] = value.getName();
                    playerNamesIndex++;
                }
            }
            if (playerNamesIndex == 0 || playerNames[0] == null) {
                playerNames = new String[0];
            }
            vm.put(PLAYER_LIST_ATTR, playerNames);
        }
        vm.put(PLAYER_COUNT_ATTR, playerLobby.getPlayers().length);

        synchronized (games) {
            GameController[] gameObjs = games.getGameList();
            vm.put(CURRENT_GAME_LIST_ATTR, gameObjs);
        }

        if (httpSession.attribute("message") != null) {
            vm.put("message", httpSession.attribute("message"));
            httpSession.removeAttribute("message");
        }

        // render the View
        return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    }
}
