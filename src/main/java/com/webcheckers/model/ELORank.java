package com.webcheckers.model;

/**
 * This represents the ELO rank of the player that owns it, and contains all of the calculations needed
 * for the ELO ranking system
 *
 * Last Revision: 11/02/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */
public class ELORank {

    private int totalOpponentRanking = 0;
    private int totalWins = 0;
    private int totalLosses = 0;
    private int totalDraws = 0;

    /**
     * Calculates the current ELO rank
     * @return The ELO rank
     */
    public int getPerformanceRating() {
        if (totalWins + totalLosses + totalDraws == 0) {
            return 0;
        } else {
            return (totalOpponentRanking + (400 * totalWins) + (-400 * totalLosses)) / (totalWins + totalLosses + totalDraws);
        }
    }

    /**
     * Adds a win the the ELO score and returns the current performance ranking
     * @param opponentRank The rank of the opponent won against
     * @return The new performance ranking
     */
    public int addWin(int opponentRank) {
        totalOpponentRanking += opponentRank;
        totalWins ++;
        return getPerformanceRating();
    }

    /**
     * Adds a loss the the ELO score and returns the current performance ranking
     * @param opponentRank The rank of the opponent lost against
     * @return The new performance ranking
     */
    public int addLoss(int opponentRank) {
        totalOpponentRanking += opponentRank;
        totalLosses ++;
        return getPerformanceRating();
    }

    /**
     * Adds a draw the the ELO score and returns the current performance ranking
     * @param opponentRank The rank of the opponent drawn against
     * @return The new performance ranking
     */
    public int addDraw(int opponentRank) {
        totalOpponentRanking += opponentRank;
        totalDraws ++;
        return getPerformanceRating();
    }

}
