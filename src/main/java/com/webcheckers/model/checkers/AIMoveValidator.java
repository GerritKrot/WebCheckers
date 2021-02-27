package com.webcheckers.model.checkers;

import java.util.ArrayList;

public class AIMoveValidator extends MoveValidator{

    /**
     * Standard position for specifying no jump was recently made
     */
    private final Position noPos = new Position(-1,-1);

    /**
     * Holds the Position of the last jump made. Defaults to no position.
     */
    private Position lJump = noPos;

    /**
     * Holds the type of the piece being moved
     */
    private CheckerPieceType type = CheckerPieceType.SINGLE;
    private ArrayList<Position> jumps = new ArrayList<>();

    /**
     * constructor initializes a MoveValidator object with passed redBoard
     * and whiteBoard
     *
     * @param redBoard   the board from the view of the red player
     * @param whiteBoard the board from the view of the white player
     */
    public AIMoveValidator(BoardView redBoard, BoardView whiteBoard) {
        super(redBoard, whiteBoard);
    }

    /**
     * Determines if the given walk move is a valid move. Used for AI
     * decision-making.
     * @param move Move to verify.
     * @param board Board to compare move with.
     * @return Boolean of whether move is valid or not.
     */
    private boolean wVal(Move move, BoardView board) {
        if (move == null) {
            return false;
        }
        Position start = move.getStart();
        Position end = move.getEnd();
        if (!board.getSpace(end).isValid()) {
            return false;
        }
        if (board.getPiece(start)==null) {
            return false;
        }
        if (Math.abs(start.getCell() - end.getCell()) == 1) {
            if (Math.abs(start.getRow() - end.getRow()) == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Record the last jump position.
     * @param p Postion of last jump.
     */
    public void setlJump(Position p){
        lJump = p;
    }

    /**
     * Set the type of the piece.
     * @param type Type of piece to record
     */
    public void setType(CheckerPieceType type){
        this.type= type;
    }

    /**
     * Returns a list of moves based on the given color.
     * The list returned is prioritized by connecting jumps,
     * regular jumps, and then walk moves..
     * @param color Color of pieces to search for.
     * @return List of prioritized moves. Return null
     * in base case.
     */
    public ArrayList<Move> AIMoves(CheckerPieceColor color){
        ArrayList<Move> connectingJumps;
        ArrayList<Move> jumps;
        ArrayList<Move> walks;
        BoardView board;
        if(color == CheckerPieceColor.RED) {
            board = getRedBoard();
        }
        else{
            board = getWhiteBoard();
        }

        //Find Connecting Jump first
        connectingJumps = valMultiJumps(board, color);
        if(connectingJumps.size()>0){
            return connectingJumps;
        }

        //Find Jumps
        jumps = valJumps(board, color);
        if(jumps.size()>0){
            return jumps;
        }

        //Find Walks
        walks = valWalks(board, color);
        if(walks.size()>0){
            return walks;
        }
        return null;
    }

    /**
     * Assuming the last move made was a jump, check and return any other possible
     * connecting jumps that could be made.
     * @param board Current board of Player.
     * @param color Color of pieces to search for.
     * @return List of possible connecting jump moves
     */
    private ArrayList<Move> valMultiJumps(BoardView board, CheckerPieceColor color){
        ArrayList<Move> connectingJumps = new ArrayList<>();
        if (!noPos.equals(lJump)){
            Move[] moves = posMultiJumps(lJump, type);
            for(int k = 0; k < 4; k++) {
                if (jumpCheckVal(moves[k],board,color)) {
                    moves[k].setJump();
                    boolean canDo = true;
                    for (Position p : jumps) {
                        int mRow = (moves[k].getStart().getRow() + moves[k].getEnd().getRow())/2;
                        int mCell = (moves[k].getStart().getCell() + moves[k].getEnd().getCell())/2;
                        Position midPoint = new Position(mCell, mRow);
                        if (p.equals(midPoint)){
                            canDo = false;
                        }
                    }
                    if(canDo)
                        connectingJumps.add(moves[k]);
                }
            }
        }
        return connectingJumps;
    }

    /**
     * Check and return any possible jumps that could be made.
     * @param board Current board of Player.
     * @param color Color of pieces to search for.
     * @return List of possible jump moves
     */
    private ArrayList<Move> valJumps(BoardView board, CheckerPieceColor color){
        ArrayList<Move> jumps = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position p = new Position(i, j);
                Move[] moves = getPosJumps(p, board, color);
                for (int k = 0; k < 4; k++) {
                    if (jumpCheckVal(moves[k], board, color)) {
                        moves[k].setJump();
                        jumps.add(moves[k]);
                    }
                }
            }
        }
        return jumps;
    }

    /**
     * Check and return any possible walks that could be made.
     * @param board Current board of Player.
     * @param color Color of pieces to search for.
     * @return List of possible walk moves
     */
    private ArrayList<Move> valWalks(BoardView board, CheckerPieceColor color) {
        ArrayList<Move> walks = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position p = new Position(i,j);
                Move[] moves = getPosWalks(p, board, color);
                for (int k = 0; k < 4; k++) {
                    if (wVal(moves[k], board)) {
                        walks.add(moves[k]);
                    }
                }
            }
        }
        return walks;
    }

    /**
     * Add given midpoint position to jumps list
     * @param midpoint Given midpoint Position
     */
    public void addJump(Position midpoint) {
        jumps.add(midpoint);
    }
}
