package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.Arrays;
import java.util.HashMap;

import static com.webcheckers.model.Player.gameStatus.PLAYING_GAME;
/**
 * This class contains the state and behavior associated with the PlayerLobby
 * for the checkers game
 * Last Revision: 10/19/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class PlayerLobby {

    private final HashMap<String /*Username*/, Player> playerList;

    /**
     * instantiate a PlayerLobby with a map of players
     */
    public PlayerLobby() {
        playerList = new HashMap<>();
        HardAI hardAI = new HardAI();
        EasyAI easyAI = new EasyAI();
        playerList.put(hardAI.getName(), hardAI);
        playerList.put(easyAI.getName(), easyAI);
    }


    /**
     * @param username The Username of the player to check for
     * @return A boolean if the Player Exists or not
     */
    public boolean playerExists(String username) {
        return playerList.containsKey(username);
    }

    /**
     * @param username The username of the player to add
     * @return False if the player already exists, true if the player does.
     **/
    public Player addPlayer(String username) {
        if (playerExists(username)) {
            return null;
        }
        Player player = new Player(username);
        playerList.put(username, player);
        return player;
    }

    /**
     * @param username The username of the player to remove
     * @return False if the player did not exist, true if the player did
     */
    public boolean removePlayer(String username) {
        if (!playerExists(username)) {
            return false;
        }
        playerList.remove(username);
        return true;
    }

    /**
     * getPlayers returns the list of connected players
     *
     * @return An array of players: players NOT playing a game are at the top, players PLAYING a game are at the bottom
     */
    public Player[] getPlayers() {
        Player[] players = new Player[playerList.size()];
        int playerArrayForwardIndex = 0;
        int playerArrayBackwardIndex = playerList.size() - 1;
        for (Player value : playerList.values()) {
            if (!(value.isPlayingGame())) {
                players[playerArrayForwardIndex] = value;
                playerArrayForwardIndex++;
            } else {
                players[playerArrayBackwardIndex] = value;
                playerArrayBackwardIndex--;
            }
        }
        Arrays.sort(players);
        return players;
    }

    public Player getPlayer(String username) {
        return playerList.get(username);
    }

    /**
     * adds a player to a game
     *
     * @param selectedPlayer the player to be added
     * @param game     the game for the user to be added to
     * @return true or false indicating whether the player was able to join the game
     */
    private boolean addPlayerToGame(Player selectedPlayer, GameController game) {
            if (selectedPlayer.isPlayingGame()) {
                return false;
            } else {
                selectedPlayer.setStatus(PLAYING_GAME);
                selectedPlayer.setGame(game);
                return true;
            }
        }

    /**
     * isAvailable checks if a specified user is available to play
     *
     * @param username the name to be checked for availability
     * @return whether the player is available or not
     */
    public boolean isAvailable(String username) {

        if (playerExists(username)) {
            Player player = playerList.get(username);
            return !player.isPlayingGame();
        } else {
            return false;
        }
    }

    /**
     * starts a game between two players
     *
     * @param redPlayer   the player to be assigned white checkers pieces
     * @param whitePlayerName the player to be assigned black checkers pieces
     * @return a GameController object for this game
     */
    public boolean startAGame(Player redPlayer, String whitePlayerName, GameController newGame) {
        if (!isAvailable(whitePlayerName)) {
            return false;
        }
        Player whitePlayer = playerList.get(whitePlayerName);
        if (whitePlayer instanceof EasyAI) {
            whitePlayer = new EasyAI();
        } else if (whitePlayer instanceof HardAI) {
            whitePlayer = new HardAI();
        }
        newGame.setPlayers(redPlayer, whitePlayer);
        addPlayerToGame(redPlayer, newGame);
        addPlayerToGame(whitePlayer, newGame);
        return true;
    }
}