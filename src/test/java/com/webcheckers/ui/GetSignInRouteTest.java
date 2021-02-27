package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.GameController;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;



/**
 * Unit test for GetSignInRoute
 * Last Revision: 10/12/2020
 * @author Payton Burak
 */
@Tag("UI-tier")
public class GetSignInRouteTest {

    private Session session;
    private Request request;
    private Response response;
    private PlayerLobby playerLobby;
    private GameController game;
    private GetSignInRoute getSignInRoute;
    private Player player;
    private TemplateEngine templateEngine;

    /**
     * Sets up mock objects for CuT as well as initializes the CuT
     */
    @BeforeEach
    public void setup() {
        this.session = mock(Session.class);
        this.request = mock(Request.class);
        this.response = mock(Response.class);
        this.playerLobby = mock(PlayerLobby.class);
        this.game = mock(GameController.class);
        this.player = mock(Player.class);
        this.templateEngine = mock(TemplateEngine.class);
        this.getSignInRoute = new GetSignInRoute(templateEngine);
        when(request.session()).thenReturn(session);
    }

    @Test
    public void test_render() throws Exception {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("message")).thenReturn("Test Message");
        getSignInRoute.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute(GetSignInRoute.PAGE_TITLE_ATTR, GetSignInRoute.SIGNIN_PAGE_TITLE);
        testHelper.assertViewModelAttribute("message", "Test Message");
        testHelper.assertViewName("signin.ftl");
    }

    /**
     * Make the HTML text for a div element that holds a user message.
     */
    private static String makeMessageTag(final String text, final String type) {
        return String.format("<div id=\"message\" class=\"%s\">%s</div>", type, text);
    }

    @Test
    public void test_html_rendered(){
        final TemplateEngine engine = new FreeMarkerEngine();
        final Map<String, Object> vm = new HashMap<>();
        final ModelAndView modelAndView = new ModelAndView(vm, "signin.ftl");
        System.out.println(modelAndView);
        // setup View-Model for a new player
        vm.put(GetSignInRoute.PAGE_TITLE_ATTR, GetSignInRoute.SIGNIN_PAGE_TITLE);

        // Invoke test
        final String viewHtml = engine.render(modelAndView);

        // Analyze results
        // * look for Title elements
        assertTrue(viewHtml.contains("<title>Sign In | Sign-In</title>"), "Title head tag exists.");
        assertTrue(viewHtml.contains("<h1>Sign In | Sign-In</h1>"), "Title heading tag exists.");
    }
}
