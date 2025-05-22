package Players;

import Evaluations.Evaluator;
import StateComponents.StateOfClobber;


import static Evaluations.Evaluator.MAX_EVAL;

public class TreeAlphaBeta implements GameTree{
    private GameNode root;
    private final int ourPlayerColor;

    public TreeAlphaBeta(StateOfClobber startState, int ourPlayerColor) {
        this.ourPlayerColor = ourPlayerColor;
        this.root = new GameNode(startState, ourPlayerColor, GameNode.NodeType.MAX);
    }

    @Override
    public GameNode getRoot() {
        return this.root;
    }

    @Override
    public GameNode getMaxNode(Evaluator evaluator, int depth, Runnable onNodeVisit){
        return this.root.alphaBetaEvaluate(
                -MAX_EVAL - 1,
                MAX_EVAL + 1,
                evaluator,
                depth,
                ourPlayerColor,
                onNodeVisit
        ).getOperationalPredecessor();
    }

    @Override
    public void setRootHard(GameNode newRoot) {
        this.root = newRoot;
    }
}
