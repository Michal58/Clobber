package Evaluations;

import StateComponents.StateOfClobber;

public class WeightedCountOfMovesHeuristic implements Evaluator{
    public static final double DEFAULT_ZERO_MOVES_WEIGHT = 0;
    public static final double DEFAULT_ONE_MOVES_WEIGHT = 1;
    public static final double DEFAULT_TWO_MOVES_WEIGHT = 1.5 * 2;
    public static final double DEFAULT_THREE_MOVES_WEIGHT = 2 * 3;
    public static final double DEFAULT_FOUR_MOVES_WEIGHT = 2.5 * 4;

    private double[] weights;

    public WeightedCountOfMovesHeuristic(
            double zeroMovesWeight,
            double oneMoveWeight,
            double twoMovesWeight,
            double threeMovesWeight,
            double fourMovesWeight
    ) {
        this.weights = new double[] {
                zeroMovesWeight,
                oneMoveWeight,
                twoMovesWeight,
                threeMovesWeight,
                fourMovesWeight
        };
    }

    public WeightedCountOfMovesHeuristic() {
        this(
                DEFAULT_ZERO_MOVES_WEIGHT,
                DEFAULT_ONE_MOVES_WEIGHT,
                DEFAULT_TWO_MOVES_WEIGHT,
                DEFAULT_THREE_MOVES_WEIGHT,
                DEFAULT_FOUR_MOVES_WEIGHT
        );
    }

    @Override
    public double assessState(StateOfClobber gameState, int ourColor) {
        int n = gameState.getHeight();
        int m = gameState.getWidth();

        double ourPossibleMovesScore = 0;
        double theirPossibleMovesScore = 0;
        int theirFlatPossibleMoves = 0;

        int oppositeCode = gameState.getOpposingCode(ourColor);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int currentColor = gameState.getColor(i, j);
                if (currentColor == oppositeCode) {
                    int theirCountOfTakes = gameState.howManyTakesPieceHas(i, j);
                    theirFlatPossibleMoves += theirCountOfTakes;
                    theirPossibleMovesScore += weights[theirCountOfTakes];
                } else if (currentColor == ourColor) {
                    ourPossibleMovesScore += weights[gameState.howManyTakesPieceHas(i, j)];
                }
            }
        }

        if (theirFlatPossibleMoves == 0)
            return MAX_EVAL;


        return ourPossibleMovesScore - theirPossibleMovesScore;
    }
}
