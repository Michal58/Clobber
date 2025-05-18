package Evaluations;

import StateComponents.StateOfClobber;

public class IsolationAdvantageHeuristic implements Evaluator{
    @Override
    public double assessState(StateOfClobber gameState, int ourColor) {
        int n = gameState.getHeight();
        int m = gameState.getWidth();

        int ourIsolatedPieces = 0;
        int theirIsolatedPieces = 0;

        int oppositeCode = gameState.getOpposingCode(ourColor);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int currentColor = gameState.getColor(i, j);
                if (currentColor == ourColor && !gameState.hasPieceAnyMove(i,j))
                    ourIsolatedPieces++;
                else if (currentColor == oppositeCode && !gameState.hasPieceAnyMove(i,j))
                    theirIsolatedPieces++;
            }
        }

        return theirIsolatedPieces - ourIsolatedPieces;
    }
}
