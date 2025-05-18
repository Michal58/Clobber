package Evaluations;

import StateComponents.StateOfClobber;

public class DoublingPositionHeuristic implements Evaluator{
    @Override
    public double assessState(StateOfClobber gameState, int ourColor) {
        int n = gameState.getHeight();
        int m = gameState.getWidth();

        int ourDoublingPieces = 0;
        int theirDoublingPieces = 0;

        int oppositeCode = gameState.getOpposingCode(ourColor);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int currentColor = gameState.getColor(i, j);
                if (currentColor == ourColor)
                    ourDoublingPieces += gameState.getDoublingCount(i,j);
                else if (currentColor == oppositeCode)
                    theirDoublingPieces += gameState.getDoublingCount(i,j);
            }
        }

        return ourDoublingPieces - theirDoublingPieces;
    }
}
