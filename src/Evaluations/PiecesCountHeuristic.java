package Evaluations;

import StateComponents.StateOfClobber;

// it returns always either 1/-1 or 0 (by turns) - it can be simplified later
public class PiecesCountHeuristic implements Evaluator{
    @Override
    public double assessState(StateOfClobber gameState, int ourColor) {
        int n = gameState.getHeight();
        int m = gameState.getWidth();

        int ourPiecesCount = 0;
        int theirPiecesCount = 0;

        int oppositeCode = gameState.getOpposingCode(ourColor);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int currentColor = gameState.getColor(i, j);
                if (currentColor == ourColor)
                    ourPiecesCount++;
                else if (currentColor == oppositeCode)
                    theirPiecesCount++;
            }
        }

        if (theirPiecesCount == 0)
            return MAX_EVAL;
        if (ourPiecesCount == 0)
            return -MAX_EVAL;

        return ourPiecesCount - theirPiecesCount;
    }
}
