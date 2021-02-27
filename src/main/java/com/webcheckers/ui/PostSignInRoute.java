package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class handles POST requests to the signin route. This recieves a user signin request and
 * passes it to PlayerLobby, and then based on the return value will either notify the user of an
 * error, or sets up the user's session.
 *
 * Last Revision: 09/29/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class PostSignInRoute implements Route {
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    static final String USERNAME_PARAM = "namefield";
    static final String SESSION_PLAYER_ATTR = "PLAYER";
    static final String INVALID_USERNAME = "The username you chose is invalid, it must be between 1 and 13 characters, cannot start with a space, and made up of spaces and alphanumeric characters";
    static final String USERNAME_TAKEN = "That username is already in use, choose another name";

    /**
     * Verifies that a username is at least 1 character, and that it is only alphanumeric
     *
     * @param username The username being validated
     * @return True if the username is valid, false if it is not.
     */
    private boolean validate_username(String username) {
        if(username.length() < 1 || username.length() > 13) {
            return false;
        }
        if(username.contains("AI_"))
            return false;
        if(username.charAt(0) == ' ' || username.charAt(username.length() - 1) == ' ') {
            return false;
        }
        return username.matches("^[a-zA-Z0-9 ]*$");
    }

    /**
     * The constructor for PostSignInRoute. Sets up the object with the template engine and player lobby object
     *
     * @param templateEngine The template engine being used to render the page
     * @param playerLobby The PlayerLobby object that handles the users currently using the system
     */
    public PostSignInRoute(final TemplateEngine templateEngine, PlayerLobby playerLobby) {
        Objects.requireNonNull(templateEngine);
        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
    }

    /**
     * The handler for POST requests to /signin. This takes a username, validates the formatting of it,
     * passes it to PlayerLobby and sets up the user session. When a user logs in successfully, this redirects
     * to the home page.
     *
     * @param request The POST request to /signin
     * @param response The response that is being constructed to return to the user
     * @return Rendered HTML to return to the user based on the result of the signin attempt
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Map<String, Object> vm = new HashMap<>();
        final Session session = request.session();

        final String nameStr = request.queryParams(USERNAME_PARAM);

        if(!validate_username(nameStr)) {
            Message error = Message.error(INVALID_USERNAME);
            vm.put("message", error);
            vm.put("title", GetSignInRoute.SIGNIN_PAGE_TITLE);
            return templateEngine.render(new ModelAndView(vm , "signin.ftl"));
        }
        Player player = playerLobby.addPlayer(nameStr);
        if(player == null) {
            Message error = Message.error(USERNAME_TAKEN);
            vm.put("message", error);
            vm.put("title", GetSignInRoute.SIGNIN_PAGE_TITLE);
            return templateEngine.render(new ModelAndView(vm , "signin.ftl"));
        } else {
            session.attribute(SESSION_PLAYER_ATTR, player);
            response.redirect("/");
            return "<html>Redirecting...</html>";
        }
    }
}
