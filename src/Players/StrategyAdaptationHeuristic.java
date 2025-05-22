package Players;

import Evaluations.Evaluator;
import Evaluations.TunableComplexHeuristic;
import StateComponents.StateOfClobber;

import java.util.stream.IntStream;

public class StrategyAdaptationHeuristic implements StrategyAdaptation {

    public static final double[][] BASE_WEIGHTS = {
            {5, 5},
            {0.2, 0.8},
            {2, 8},
            {0.05, 0.15},
            {0.25, 9.75}
    };


    public interface AdaptationWeightsHeuristic {
        double[] evaluate(StateOfClobber gameState);
    }

    public static class PiecesCountAdaptation implements AdaptationWeightsHeuristic {
        @Override
        public double[] evaluate(StateOfClobber gameState) {
            int n = gameState.getHeight();
            int m = gameState.getWidth();

            double piecesCount = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (gameState.getColor(i, j) != StateOfClobber.EMPTY) {
                        piecesCount++;
                    }
                }
            }

            double heuristicCoefficient = piecesCount/(n*m);

            return new double[] {
                    BASE_WEIGHTS[0][0] + heuristicCoefficient * BASE_WEIGHTS[0][1],
                    BASE_WEIGHTS[1][0] + heuristicCoefficient * BASE_WEIGHTS[1][1],
                    BASE_WEIGHTS[2][0] + BASE_WEIGHTS[2][1] * (1 - heuristicCoefficient),
                    BASE_WEIGHTS[3][0] + heuristicCoefficient * BASE_WEIGHTS[3][1],
                    BASE_WEIGHTS[4][0] + BASE_WEIGHTS[4][1] * (1 - heuristicCoefficient)
            };
        }
    }

    public static class PiecesMovesAdaptation implements AdaptationWeightsHeuristic {

        @Override
        public double[] evaluate(StateOfClobber gameState) {
            int n = gameState.getHeight();
            int m = gameState.getWidth();

            double movesCount = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (gameState.getColor(i, j) != StateOfClobber.EMPTY) {
                        movesCount += gameState.howManyTakesPieceHas(i, j);
                    }
                }
            }

            double heuristicCoefficient = movesCount/(n*m*4);

            return new double[] {
                    BASE_WEIGHTS[0][0] + heuristicCoefficient * BASE_WEIGHTS[0][1],
                    BASE_WEIGHTS[1][0] + heuristicCoefficient * BASE_WEIGHTS[1][1],
                    BASE_WEIGHTS[2][0] + BASE_WEIGHTS[2][1] * (1 - heuristicCoefficient),
                    BASE_WEIGHTS[3][0] + heuristicCoefficient * BASE_WEIGHTS[3][1],
                    BASE_WEIGHTS[4][0] + BASE_WEIGHTS[4][1] * (1 - heuristicCoefficient)
            };
        }
    }

    private double[] adaptationMetaWeights;

    public static final double DEFAULT_ADAPTATION_WEIGHT_1 = 0.75;
    public static final double DEFAULT_ADAPTATION_WEIGHT_2 = 0.25;

    public StrategyAdaptationHeuristic() {
        this.adaptationMetaWeights = new double[] {
                DEFAULT_ADAPTATION_WEIGHT_1,
                DEFAULT_ADAPTATION_WEIGHT_2
        };
    }

    @Override
    public Evaluator getAdaptedHeuristic(StateOfClobber currentState) {
        double[][] adaptationHeuristicWeights = new double[2][];

        AdaptationWeightsHeuristic[] adaptationHeuristics = {
                new PiecesCountAdaptation(),
                new PiecesMovesAdaptation()
        };
        adaptationHeuristicWeights[0] = adaptationHeuristics[0].evaluate(currentState);
        adaptationHeuristicWeights[1] = adaptationHeuristics[1].evaluate(currentState);

        Double[] ultimateWeights = IntStream.range(0, BASE_WEIGHTS.length)
                .boxed()
                .map(i->
                        (adaptationMetaWeights[0] * adaptationHeuristicWeights[0][i] + adaptationMetaWeights[1] * adaptationHeuristicWeights[1][i]) /
                        (adaptationMetaWeights[0] + adaptationMetaWeights[1])
                )
                .toArray(Double[]::new);

        return new TunableComplexHeuristic(
                ultimateWeights[0],
                ultimateWeights[1],
                ultimateWeights[2],
                ultimateWeights[3],
                ultimateWeights[4]
        );
    }
}
