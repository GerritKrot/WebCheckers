package com.webcheckers.appl;

import com.webcheckers.model.Player;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Tag("Application-tier")
public class PlayerLobbyTest {

    private PlayerLobby testPlayerLobby = new PlayerLobby();

    @Test
    public void test_no_Players() {
        int len = testPlayerLobby.getPlayers().length;
        assertEquals(2, len, "PlayerLobby list must be empty (except for AI) but got a value of " + len + " instead.");
    }

    @Test
    public void test_has_Player() {
        testPlayerLobby.addPlayer("Frank");
        assertTrue(testPlayerLobby.playerExists("Frank"), "Player Frank could not be found in PlayerLobby.");
    }

    @Test
    public void test_Player_removed() {
        testPlayerLobby.addPlayer("Frank");
        testPlayerLobby.removePlayer("Frank");
        assertFalse(testPlayerLobby.playerExists("Frank"), "Player Frank is still in PlayerLobby but shouldn't be.");
    }

    @Test
    public void test_search_Player() {
        testPlayerLobby.addPlayer("Larry");
        testPlayerLobby.addPlayer("Bob");
        testPlayerLobby.addPlayer("Abby");
        Player testPlayer = testPlayerLobby.getPlayer("Abby");
        String name = testPlayer.getName();
        assertEquals("Abby", name, "Searched for Player Abby but got " + name + " instead.");
    }

    @Test
    public void test_PlayerList_length() {
        testPlayerLobby.addPlayer("Larry");
        testPlayerLobby.addPlayer("Bob");
        testPlayerLobby.addPlayer("Abby");
        int len = testPlayerLobby.getPlayers().length;
        assertEquals(5, len, "5 players should've been found but got " + len + " instead.");
    }

    @Test
    public void test_start_game() {
        testPlayerLobby.addPlayer("Larry");
        testPlayerLobby.addPlayer("Abby");
        Player testPlayer1 = testPlayerLobby.getPlayer("Larry");
        testPlayer1.setStatus(Player.gameStatus.PLAYING_GAME);
        //assertNotNull(testPlayerLobby.startAGame(testPlayer1, "Abby"), "A game should've started with Larry and Abby.");
    }

    @Test
    public void test_Player_available(){
        testPlayerLobby.addPlayer("Abby");
        assertTrue(testPlayerLobby.isAvailable("Abby"),"Player Abby should be available for a game");
    }
}
