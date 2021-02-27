package com.webcheckers.ui;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * This class handles GET requests for the signin page.
 * Last Revision: 09/29/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */

public class GetSignInRoute implements Route {

    private final TemplateEngine templateEngine;
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    static final String PAGE_TITLE_ATTR = "title";
    static final String SIGNIN_PAGE_TITLE = "Sign-In";
    static final String SESSION_PLAYER_ATTR = "PLAYER";

    /**
     * Constructor for the GetSignIn route object.
     * @param templateEngine The template engine being used to render the .ftl file
     */
    public GetSignInRoute(final TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine);
        this.templateEngine = templateEngine;
    }

    /**
     * Handles a GET request for the /signin route. This returns the sign-in page if the user is not
     * signed in, otherwise it will return a redirect to the home page.
     *
     * @param request The request being made to the spark server
     * @param response The response that will be returned to the user
     * @return HTML code to render in the user's browser
     * @throws Exception Throws an exception if unable to render the page due to incorrect internal state
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("GetSignInRoute is invoked.");

        final Session httpSession = request.session(); //Get the current session

        // If the user is already logged in send them back home
        if(httpSession.attribute(SESSION_PLAYER_ATTR) != null) {
            response.redirect("/");
            return "<html>Redirecting...</html>";
        }
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(PAGE_TITLE_ATTR, SIGNIN_PAGE_TITLE);

        if(httpSession.attribute("message") != null) {
            vm.put("message", httpSession.attribute("message"));
            httpSession.removeAttribute("message");
        }

        // render the View
        return templateEngine.render(new ModelAndView(vm , "signin.ftl"));
    }
}
