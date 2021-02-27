package com.webcheckers.appl;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A collection of the games being played currently
 *
 * Last Revision: 11/02/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class Games {

    private ArrayList<GameController> games = new ArrayList<GameController>();

    /**
     * Returns the current number of games being played
     * @return
     */
    public int getGameCount() {
        synchronized (this) {
            return games.size();
        }
    }

    /**
     * Returns a game based on the supplied ID. If not game has that ID null is returned
     * @param id The ID of the game that is wanted
     * @return The GameController with the supplied ID
     */
    public GameController getGame(String id) {
        for(int i = 0; i < games.size(); i ++) {
            if(games.get(i).getId().equals(id)) {
                return games.get(i);
            }
        }
        return null;
    }

    /**
     * Adds a game to the current set of games being played
     * @param g The game to add to the game list
     */
    public void addGame(GameController g) {
        synchronized (this) {
            games.add(g);
        }
    }

    /**
     * Removes a game from the current game list
     *
     * @param g The game to be removed from the list
     */
    public void removeGame(GameController g) {
        synchronized (this) {
            games.remove(g);
        }
    }

    /**
     * Returns all of the games currently being played as an array
     * @return An array of all of the games currently being played
     */
    public GameController[] getGameList() {
        synchronized (this) {
            GameController[] gameList = new GameController[games.size()];
            for(int i = 0; i < games.size(); i ++) {
                gameList[i] = games.get(i);
            }
            Arrays.sort(gameList);
            return gameList;
        }
    }
}
