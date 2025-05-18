package Evaluations;

import StateComponents.StateOfClobber;

public class TunableComplexHeuristic implements Evaluator{
    private double abilityToMoveHeuristicWeight;
    private double isolatedPiecesHeuristicWeight;
    private double doublingPiecesHeuristicWeight;
    private double centralityHeuristicWeight;

    private PieceMoveAbilityHeuristic moveAbilityHeuristic;
    private IsolationAdvantageHeuristic isolationAdvantageHeuristic;
    private DoublingPositionHeuristic doublingPositionHeuristic;
    private CentralityHeuristic centralityHeuristic;


    public TunableComplexHeuristic(
            double abilityToMoveHeuristicWeight,
            double isolatedPiecesHeuristicWeight,
            double doublingPiecesHeuristicWeight,
            double centralityHeuristicWeight
    ) {
        this.abilityToMoveHeuristicWeight = abilityToMoveHeuristicWeight;
        this.isolatedPiecesHeuristicWeight = isolatedPiecesHeuristicWeight;
        this.doublingPiecesHeuristicWeight = doublingPiecesHeuristicWeight;
        this.centralityHeuristicWeight = centralityHeuristicWeight;

        this.moveAbilityHeuristic = new PieceMoveAbilityHeuristic();
        this.isolationAdvantageHeuristic = new IsolationAdvantageHeuristic();
        this.doublingPositionHeuristic = new DoublingPositionHeuristic();
        this.centralityHeuristic = new CentralityHeuristic();
    }

    @Override
    public double assessState(StateOfClobber gameState, int ourColor) {
        return abilityToMoveHeuristicWeight * moveAbilityHeuristic.assessState(gameState, ourColor) +
                isolatedPiecesHeuristicWeight * isolationAdvantageHeuristic.assessState(gameState, ourColor) +
                doublingPiecesHeuristicWeight * doublingPositionHeuristic.assessState(gameState, ourColor) +
                centralityHeuristicWeight * centralityHeuristic.assessState(gameState, ourColor);
    }
}
