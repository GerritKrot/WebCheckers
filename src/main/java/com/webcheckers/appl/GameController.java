package com.webcheckers.appl;

import com.webcheckers.Application;
import com.webcheckers.model.AIPlayer;
import com.webcheckers.model.Player;
import com.webcheckers.model.checkers.*;
import com.webcheckers.model.checkers.BoardView;
import com.webcheckers.model.checkers.Move;
import com.webcheckers.model.checkers.Piece;

import javax.xml.validation.Validator;
import java.util.Stack;
import java.util.UUID;

/**
 * This class contains the state and behavior associated with a GameController object,
 * which checks that moves made are valid
 * Last Revision: 10/19/2020
 * @author Payton Burak, Michael Canning, John Davidson, Gerrit Krot, Evan Ruttenberg
 */

public class GameController implements Comparable{

    /**
     * Red Player of the game
     */
    private Player redPlayer;

    /**
     * White Player of the game
     */
    private Player whitePlayer;

    /**
     * BoardView of the Red Player
     */
    private BoardView boardViewRed;

    /**
     * Board View of the White Player
     */
    private BoardView boardViewWhite;

    /**
     * Stack of Moves used to hold onto Player Move decisions
     */
    private Stack<Move> pendingMoves;

    /**
     * Stack of Moves used to execute Player Move decisions
     */
    private Stack<Move> turnMoves;

    /**
     * MoveValidator Object used to determine moves
     */
    private MoveValidator validator;

    /**
     * Holds rank of current game
     */
    private int gameRank;

    /**
     * Holds number of spectators watching current game
     */
    private int spectators = 0;

    /**
     * Unique ID for game
     */
    private final String gameId = UUID.randomUUID().toString();

    /**
     * Represents the target color
     */
    private CheckerPieceColor activeColor;

    /**
     * State of the game after forfeit or Win/Loss
     */
    public enum winLossStatus {
        WON,
        LOST
    }

    /**
     * Determines if current game is forfeit or not
     */
    private boolean forfeit = false;

    /**
     * Reference to Game Lobby
     */
    private Games games;

    /**
     * constructor initializes a GameController object
     * with set players
     * @param redBoard board for player 1
     * @param whiteBoard board for player 2
     */
 public GameController(BoardView redBoard, BoardView whiteBoard, MoveValidator validator, Games games) {
        boardViewRed = redBoard;
        boardViewWhite = whiteBoard;
        this.validator = validator;
        activeColor = CheckerPieceColor.RED;
        pendingMoves = new Stack<>();
        turnMoves = new Stack<>();
        this.games = games;
        games.addGame(this);

    }

    /**
     * Set the Given Players into the game and obtain the rank
     * @param redPlayer Red Player being added
     * @param whitePlayer WhitePlayer being added
     */
    public void setPlayers(Player redPlayer, Player whitePlayer){
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.gameRank = redPlayer.getRank() + whitePlayer.getRank();
    }

    /**
     * getBoard returns the BoardView object representing this game
     * @param player the color of the player who's board needs to be represented
     * @return the board of that player, oriented properly
     */
    public BoardView getBoard(Player player) {
        if(player.equals(redPlayer)) {
            return boardViewRed;
        } else
            return boardViewWhite;
    }

    /**
     * Return Red Player
     * @return RedPlayer
     */
    public Player getRedPlayer() {
        return redPlayer;
    }

    /**
     * Return White Player
     * @return WhitePlayer
     */
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * makeMove checks if a move that was made was valid
     * @return a boolean representing whether the move made was valid
     */
    public boolean checkAndSubmitTurn() {
        if (validator.getMoreJumps()){
            return false;
        }
        turnMoves.addAll(pendingMoves);

        if (turnMoves.size() > 1)
            for (Move move : turnMoves) {
                if (!move.isJump()) {
                    turnMoves.clear();
                    return false;
                }
            }
        if (activeColor == CheckerPieceColor.RED) {
            for (Move move : turnMoves) {
                getBoard(redPlayer).updateView(move);
            }
            activeColor = CheckerPieceColor.WHITE;
            validator.changeTurn(CheckerPieceColor.WHITE);
            for (Move move : turnMoves) {
                getBoard(whitePlayer).updateView(move.getOpponentsMove());
            }
        } else {
            for (Move move : turnMoves) {
                getBoard(whitePlayer).updateView(move);
            }
            activeColor = CheckerPieceColor.RED;
            validator.changeTurn(CheckerPieceColor.RED);
            for (Move move : turnMoves) {
                getBoard(redPlayer).updateView(move.getOpponentsMove());
            }
        }

        turnMoves.clear();
        pendingMoves.clear();
        if (whitePlayer.isAIPlayer()){
            AIPlayer aiPlayer = (AIPlayer) whitePlayer;
            if (whitePlayer instanceof HardAI) {
                aiPlayer = (HardAI) whitePlayer;
            }
            if(whitePlayer instanceof EasyAI){
                aiPlayer = (EasyAI) whitePlayer;
            }
            if(activeColor==CheckerPieceColor.WHITE){
                aiPlayer.performTurn();
            }
        }
        return true;
    }

    /**
     * checks if a player is active
     * @param player the player to be checked
     * @return whether the player is currently active or not
     */
    public boolean checkPlayerActive(Player player) {
        if (activeColor == CheckerPieceColor.RED){
            return (player.equals(redPlayer));
        }
        return (player.equals(whitePlayer));
    }

    /***
     * Determines whether a move is valid or not based on Move's
     * coordinates
     * @param move An attempted Player Move based on 2 coordinates
     * @return boolean of whether the Move coordinates are valid spot
     * on the board or not
     */
    public boolean validateMove(Move move) {
            if (validator.validate(move)) {
                pendingMoves.add(move);
                return true;
            }
        return false;
    }

    /**
     * undoes a move before a player would submit their turn
     * @return whether the backup was successful or not
     */
    public boolean backup() {
        if (!pendingMoves.empty()) {
            Move m = pendingMoves.pop();
            validator.backup(m);
            return true;
        }
        return false;
    }

    /**
     * Resigns a player from the game and returns whether they won or loss
     * @param player The player resigning
     * @return Returns WIN if the other player already resigned and LOSS otherwise
     */
    public winLossStatus resign(Player player) {
        if (forfeit) {
            player.setGame(null);
            player.setStatus(Player.gameStatus.NOT_PLAYING_GAME);
            if(player == redPlayer) {
                player.win(whitePlayer.getRank());
            } else {
                player.win(redPlayer.getRank());
            }
            return winLossStatus.WON;
        } else {
            forfeit = true;
            player.setGame(null);
            player.setStatus(Player.gameStatus.NOT_PLAYING_GAME);
            if (this.redPlayer == player) {
                this.activeColor = CheckerPieceColor.WHITE;
            } else {
                this.activeColor = CheckerPieceColor.RED;
            }
            endGame();
            if(player == redPlayer) {
                player.loss(whitePlayer.getRank());
            } else {
                player.loss(redPlayer.getRank());
            }
            return winLossStatus.LOST;
        }
    }

    /**
     * Returns the status of the game in reference to win/loss
     * @return "NONE" if there is no winner, "RED" if red won, "WHITE" if white won
     */
    public String checkGameStatus() {

        if(boardViewRed.check_winner() == null || boardViewWhite.check_winner() == null) {
            return "NONE";
        }
        if(boardViewRed.check_winner().equals(CheckerPieceColor.RED)) {
            return "RED";
        } else if(boardViewRed.check_winner().equals(CheckerPieceColor.WHITE)) {
            return "WHITE";
        }else if(boardViewWhite.check_winner().equals(CheckerPieceColor.RED)) {
            return "RED";
        }else if(boardViewWhite.check_winner().equals(CheckerPieceColor.WHITE)) {
            return "WHITE";
        } else {
            return "NONE";
        }
    }

    /**
     * Checks to see if the other user resigned from the game
     * @return True if the other player resigned, False otherwise
     */
    public boolean checkIfOtherPlayerResigned() {
        return forfeit;
    }

    /**
     * Returns the current game of the game
     * @return the current game of the game
     */
    public int getRank() {
        return gameRank;
    }

    /**
     * Removes the game from the master game list. This is called when the game is over.
     */
    public void endGame() {
        this.games.removeGame(this);
    }

    /**
     * Adds a spectator to the game
     */
    public void startSpectate() {
        synchronized (this) {
            spectators++;
        }
    }

    /**
     * Removes a spectator to the game
     */
    public void endSpectate() {
        synchronized (this) {
            spectators--;
        }
    }

    /**
     * Returns the count of the spectators currently watching the game
     * @return the count of the spectators currently watching the game
     */
    public int getSpectatorCount() {
        synchronized (this) {
            return spectators;
        }
    }

    /**
     * Returns the ID of the game
     * @return the ID of the game
     */
    public String getId() {
        return this.gameId;
    }

    /**
     * Compares two GameControllers based on the combined ranking of the players playing
     *
     * @param o The game controller being compared
     * @return 1 if the other controller is ranked higher, -1 if it is less, 0 if they are the same
     */
    @Override
    public int compareTo(Object o) {
        assert(o instanceof GameController);
        GameController otherGame = (GameController)o;
        if(otherGame.getRank() > this.getRank()) {
            return 1;
        } else if(otherGame.getRank() < this.getRank()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Prints the number of spectators and the players in the current game as a string
     * @return the number of spectators and the players in the current game as a string
     */
    @Override
    public String toString() {
        return getRedPlayer().getName() + " vs. " + getWhitePlayer().getName();
    }
}