package Evaluations;

import StateComponents.StateOfClobber;

//
// evaluation always return 0 - takes of one color can be represented as collection {b->w},
// but if move from a to b is feasible then w->b is also feasible - the difference is always 0
//

public class CountOfMovesHeuristic implements Evaluator {
    @Override
    public double assessState(StateOfClobber gameState, int ourColor) {
        int n = gameState.getHeight();
        int m = gameState.getWidth();

        int ourPossibleMoves = 0;
        int theirPossibleMoves = 0;

        int oppositeCode = gameState.getOpposingCode(ourColor);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int currentColor = gameState.getColor(i, j);
                if (currentColor == oppositeCode) {
                    theirPossibleMoves += gameState.howManyTakesPieceHas(i, j);
                } else if (currentColor == ourColor) {
                    ourPossibleMoves += gameState.howManyTakesPieceHas(i, j);
                }
            }
        }

        if (theirPossibleMoves == 0)
            return MAX_EVAL;


        return ourPossibleMoves - theirPossibleMoves;
    }
}
