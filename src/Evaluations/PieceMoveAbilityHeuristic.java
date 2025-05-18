package Evaluations;

import StateComponents.StateOfClobber;

public class PieceMoveAbilityHeuristic implements Evaluator{
    @Override
    public double assessState(StateOfClobber gameState, int ourColor) {
        int n = gameState.getHeight();
        int m = gameState.getWidth();

        int ourPiecesWithMove = 0;
        int theirPiecesWithMove = 0;

        int oppositeCode = gameState.getOpposingCode(ourColor);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int currentColor = gameState.getColor(i, j);
                if (currentColor == oppositeCode) {
                    theirPiecesWithMove += gameState.hasPieceAnyMove(i, j) ? 1 : 0;
                } else if (currentColor == ourColor) {
                    ourPiecesWithMove += gameState.hasPieceAnyMove(i, j) ? 1 : 0;
                }
            }
        }

        if (theirPiecesWithMove == 0)
            return MAX_EVAL;
        if (ourPiecesWithMove == 0)
            return -MAX_EVAL;

        return ourPiecesWithMove - theirPiecesWithMove;
    }
}
