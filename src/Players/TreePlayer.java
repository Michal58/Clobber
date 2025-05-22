package Players;

import Evaluations.Evaluator;
import Evaluations.TunableComplexHeuristic;
import Evaluations.WeightedCountOfMovesHeuristic;
import StateComponents.StateOfClobber;
import Utils.Utils;

import java.util.function.Function;
import static StateComponents.StateOfClobber.INVALID_COLOR;

public class TreePlayer implements Player{
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
    private double secondsTimeSearching;
    private int pieceColor;
    private int baseMaxDepth;
    private GameTree game;
    private StrategyAdaptation heuristicAdapter;
    private TreeType treeType;

    public enum TreeType {
        MIN_MAX,
        ALPHA_BETA
    }

    public static Function<Evaluator[], StrategyAdaptation> selectedStrategyAdaptation(int index) {
        return evaluators -> _ignored -> evaluators[index];
    }

    public static Function<Evaluator[], StrategyAdaptation> realStrategyAdaptationGetter() {
        return _ignored -> new StrategyAdaptationHeuristic();
    }

    public TreePlayer(int baseMaxDepth, TreeType treeType, Function<Evaluator[], StrategyAdaptation> ultimateAdaptationGetter){
        this.countOfVisitedNodes = 0;
        this.secondsTimeSearching = 0;
        this.pieceColor = INVALID_COLOR;
        this.baseMaxDepth = baseMaxDepth;
        this.heuristicAdapter = ultimateAdaptationGetter.apply(new Evaluator[]{
                ONLY_WEIGHTED_MOVES_HEURISTIC,
                BALANCED_STRATEGY,
                END_STRATEGY
        });
        this.treeType = treeType;
    }

    @Override
    public long countOfVisitedNodes() {
        return countOfVisitedNodes;
    }

    @Override
    public double secondsTimeOfSearching() {
        return secondsTimeSearching;
    }

    @Override
    public void initializeGame(int pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public void updateWithMove(StateOfClobber updatedGameState) {
        if (this.game == null)
            this.game = switch (this.treeType) {
                case MIN_MAX -> new TreeMinMax(updatedGameState, this.pieceColor);
                case ALPHA_BETA -> new TreeAlphaBeta(updatedGameState, this.pieceColor);
            };
        else
            this.game.updateEnemyMove(updatedGameState);
    }

    @Override
    public StateOfClobber makeMove() {
        Utils.Wrap<Integer> visitedNodes = new Utils.Wrap<>(0);
        Runnable onVisit = () -> visitedNodes.value++;
        Evaluator adaptedEvaluator = heuristicAdapter.getAdaptedHeuristic(game.getRoot().getBaseState());
        Utils.StopWatch stopWatch = new Utils.StopWatch();
        stopWatch.startTime();
        GameNode maxNode = game.getMaxNode(adaptedEvaluator, baseMaxDepth, onVisit);
        stopWatch.stopTime();
        game.updateWithMaxNode(maxNode);
        secondsTimeSearching += stopWatch.getTimeSeconds();
        countOfVisitedNodes += visitedNodes.value;
        return game.getRoot().getBaseState().copy();
    }
}
