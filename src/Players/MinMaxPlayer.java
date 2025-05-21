package Players;

import Evaluations.Evaluator;
import Evaluations.TunableComplexHeuristic;
import Evaluations.WeightedCountOfMovesHeuristic;
import StateComponents.StateOfClobber;

import static StateComponents.StateOfClobber.INVALID_COLOR;

public class MinMaxPlayer implements Player{
    public final Evaluator ONLY_WEIGHTED_MOVES_HEURISTIC = new WeightedCountOfMovesHeuristic(
            0,
            1.5,
            3,
            4,
            7
    );
    public final Evaluator BALANCED_STRATEGY = new TunableComplexHeuristic(
            10,
            1,
            2,
            0.1,
            0.25
    );

    public final Evaluator END_STRATEGY = new TunableComplexHeuristic(
            5,
            0.2,
            10,
            0.01,
            10
    );

    private long countOfVisitedNodes;
    private long nanoTimeSearching;
    private int pieceColor;
    private int baseMaxDepth;
    private TreeMinMax game;

    public MinMaxPlayer(int baseMaxDepth){
        this.countOfVisitedNodes = 0;
        this.nanoTimeSearching = 0;
        this.pieceColor = INVALID_COLOR;
        this.baseMaxDepth = baseMaxDepth;
    }

    @Override
    public long countOfVisitedNodes() {
        return countOfVisitedNodes;
    }

    @Override
    public double secondsTimeOfSearching() {
        return nanoTimeSearching / Math.pow(10,9);
    }

    @Override
    public void initializeGame(int pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public void updateWithMove(StateOfClobber updatedGameState) {
        if (this.game == null)
            this.game = new TreeMinMax(updatedGameState, this.pieceColor);
        else
            this.game.updateEnemyMove(updatedGameState);
    }

    @Override
    public StateOfClobber makeMove() {
        long start = System.nanoTime();
        this.game.setMaxNodeAsRoot(baseMaxDepth);
        nanoTimeSearching += (System.nanoTime() - start);
        return this.game.getRoot().getBaseState().copy();
    }
}
