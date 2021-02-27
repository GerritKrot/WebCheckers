package com.webcheckers.model;

import com.webcheckers.appl.GameController;

/**
 * This class contains the state and behavior associated with a Player of the webcheckers game
 * Last Revision: 10/19/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class Player implements Comparable {

    private final ELORank elo = new ELORank();

    /**
     *String representing the Player's name
     */
    private final String name;

    /**
     * Determines what status a Player is currently in
     */
    private gameStatus status;
    private GameController currentGame;
    protected boolean isAI;

    /**
     * gameStatus contains a few discrete options for statuses
     * which a player can be in
     */
    public enum gameStatus {
        PLAYING_GAME,
        NOT_PLAYING_GAME()
    }

    /**
     * Returns the game the player is currently playing if any
     * @return the game the player is currently playing if any
     */
    public GameController getGame() {
        return currentGame;
    }

    /**
     * Sets the game that the player is playing
     * @param game the game that the player is playing
     */
    public void setGame(GameController game) {
        currentGame = game;
    }

    /**
     * Gets the rank of the player based on the ELO ranking system
     * @return The player's rank
     */
    public int getRank() {
        return elo.getPerformanceRating();
    }

    /**
     * Adds a win to the player
     * @param otherPlayerRank The rank of the player won against
     * @return The new rank of the player
     */
    public int win(int otherPlayerRank) {
        return elo.addWin(otherPlayerRank);
    }

    /**
     * Adds a loss to the player
     * @param otherPlayerRank The rank of the player lost against
     * @return The new rank of the player
     */
    public int loss(int otherPlayerRank) {
        return elo.addLoss(otherPlayerRank);
    }

    /**
     * Adds a draw to the player
     * @param otherPlayerRank The rank of the player drawn against
     * @return The new rank of the player
     */
    public int draw(int otherPlayerRank) {
        return elo.addDraw(otherPlayerRank);
    }

    public boolean isAIPlayer() {
        return isAI;
    }


    /**
     * construct a player object with unique name
     * @param name unique name assigned to this player object
     */
    public Player (String name){
        this.name = name;
        this.status = gameStatus.NOT_PLAYING_GAME;
        isAI = false;
    }


    /**
     * obtains the player's unique name identifier
     * @return the name of this player object instance
     */
    public String getName(){
        return name;
    }

    /**
     * obtains the current status of the player
     * @return the current player's status
     */
    public boolean isPlayingGame (){
        return status == gameStatus.PLAYING_GAME;
    }

    /**
     * sets the status of the current player
     * @param status the status to be assigned to the current player
     */
    public void setStatus(gameStatus status){
        this.status = status;
    }

    public gameStatus getStatus() {
        return status;
    }

    /**
     * check if this player is the same as another object
     * @param obj the object to be compared to
     * @return whether the to objects are the same or not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            Player other = (Player)obj;
            return other.name.equals(name);
        }
        return false;
    }

    /**
     * computes and returns the hash code for this player object
     * based on the unique name identifier of the player
     * @return the hash code for this player object
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Compares 2 players based on their ELO ranking
     * @param o The player to compare to the current player
     * @return 1 if the other player is ranked higher, -1 if it is less, 0 if they are the same
     */
    @Override
    public int compareTo(Object o) {
        assert(o instanceof Player);
        Player otherPlayer = (Player)o;
        if(otherPlayer.getRank() > this.getRank()) {
            return 1;
        } else if(otherPlayer.getRank() < this.getRank()) {
            return -1;
        } else {
            return 0;
        }
    }
}
