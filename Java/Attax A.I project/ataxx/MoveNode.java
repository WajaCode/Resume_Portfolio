package ataxx;
import java.util.ArrayList;



public class MoveNode {
    /** Fixing Javadoc comment.*/
    protected PieceColor whoseMove;
    /** Fixing Javadoc comment.*/
    private Board currentBoard;
    /** Fixing Javadoc comment.*/
    private String moveString;
    /** Fixing Javadoc comment.*/
    private Board resultingBoard;
    /** Fixing Javadoc comment.*/
    private int offensiveScore;
    /** Fixing Javadoc comment.*/
    private int defensiveScore;
    /** Fixing Javadoc comment.*/
    private boolean isWinningMove;

    MoveNode(Board theBoard, PieceColor theColor) {
        this.whoseMove = theColor;
        this.moveString = "";
        this.currentBoard = theBoard;
        resultingBoard = new Board(currentBoard);
    }

    MoveNode(Board theBoard, PieceColor theColor, String moveString1) {
        this.whoseMove = theColor;
        this.moveString = moveString1;
        currentBoard = theBoard;
        resultingBoard = new Board(currentBoard);
        resultingBoard.fakeMakeMove(Move.move(moveString));
        setBoardMetrics();
    }

    boolean isWinningMove() {
        return (resultingBoard.getWinner() == PieceColor.EMPTY);
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public PieceColor getThisPlayer() {
        return whoseMove;
    }

    void makeResultingBoard(String moveString1) {
        resultingBoard = new Board(currentBoard);
        resultingBoard.fakeMakeMove(Move.move(moveString1));
    }
    void calculateBoardMetric1() {
    }

    ArrayList<MoveNode> allLegalMovesList() {
        currentBoard.legalList(currentBoard.chipList(whoseMove));
        return null;
    }

    ArrayList<MoveNode> createMoveList() {
        ArrayList<MoveNode> theMoveList = new ArrayList<>();
        for (String theMove: resultingBoard.
                legalList(resultingBoard.chipList(whoseMove))) {
            theMoveList.add(new MoveNode(resultingBoard, whoseMove, theMove));
        }
        return theMoveList;
    }

    void setBoardMetrics() {
        offensiveScore = resultingBoard.chipList(whoseMove).
                size() - currentBoard.chipList(whoseMove).size();
        defensiveScore = currentBoard.chipList(whoseMove.opposite()).
                size() - resultingBoard.chipList(whoseMove.opposite()).size();
        isWinningMove = isWinningMove();
    }

    Move getMoveObject() {
        return Move.move(moveString);
    }

    public Board getResultingBoard() {
        return resultingBoard;
    }

}
