
package ataxx;


import java.util.ArrayList;

/** MoveTreeNode.
 *  @author Wajahat Khan.
 */

public class MoveTreeNode {
    /** Fixing Javadoc comment.*/
    private MoveNode rootBoard;
    /** Fixing Javadoc comment.*/
    private int moveLevel;
    /** Fixing Javadoc comment.*/
    private int nextMoveCount;
    /** Fixing Javadoc comment.*/
    protected ArrayList<MoveNode> nextMoveList;
    /** Fixing Javadoc comment.*/
    private ArrayList<MoveTreeNode> nextTreeLevel;


    MoveTreeNode(MoveNode board, int moveLevel1) {
        this.rootBoard = board;
        this.moveLevel = moveLevel1;
        nextTreeLevel = new ArrayList<>();
        nextMoveList = rootBoard.createMoveList();
    }

    void createNextTreeLevel(int moveLevel1) {
        for (MoveNode theMoveNode : nextMoveList) {
            MoveNode newRootNode = new MoveNode(theMoveNode.
                    getResultingBoard(), theMoveNode.whoseMove.opposite());
            nextTreeLevel.add(new MoveTreeNode(newRootNode, moveLevel));
        }
    }

    public MoveNode getRoot() {
        return rootBoard;
    }

    public PieceColor getWhoseMove() {
        return rootBoard.getThisPlayer();
    }

    void addMoveNode(MoveNode theMove) {
        nextMoveList.add(theMove);
    }
}
