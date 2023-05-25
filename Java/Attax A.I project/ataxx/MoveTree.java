package ataxx;

public class MoveTree {
    /**
     * WhoseColor is it.
     */
    private MoveTreeNode treeRoot;
    /**
     * WhoseColor is it.
     */
    private String winningMoveString = "";


    MoveTree(MoveNode rootBoard) {
        treeRoot = new MoveTreeNode(rootBoard, 0);
    }

    MoveTreeNode getTreeRoot() {
        return treeRoot;
    }

    String foundWinningMove() {
        return winningMoveString;
    }

    Move getNextMove() {
        Board theBoard;
        MoveNode theMoveNode;
        PieceColor myColor;
        int pieceCount;
        int maxCount = 0;
        int maxCountIndex = 0;
        for (int i = 0; i < treeRoot.nextMoveList.size(); i++) {
            theMoveNode = treeRoot.nextMoveList.get(i);
            theBoard = theMoveNode.getResultingBoard();
            myColor = theBoard.whoseMove();
            if (myColor == PieceColor.RED) {
                pieceCount = theBoard.redPieces();
            } else {
                pieceCount = theBoard.bluePieces();
            }
            if (pieceCount > maxCount) {
                maxCount = pieceCount;
                maxCountIndex = i;
            }
        }
        return treeRoot.nextMoveList.get(maxCountIndex).getMoveObject();
    }


    void updateMoveTree(String moveString) {
    }

    public void buildTree(int treeBreadth, int treeDepth) {
        treeRoot.nextMoveList = treeRoot.getRoot().createMoveList();
        treeRoot.createNextTreeLevel(1);
        for (int moveLevel = 2; moveLevel < treeDepth; moveLevel++) {
            if (treeRoot.nextMoveList.size() < treeBreadth) {
                treeRoot.createNextTreeLevel(moveLevel);
            }
        }
    }
}
