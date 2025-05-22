package Players;

import Evaluations.Evaluator;
import StateComponents.StateOfClobber;

public class TreeMinMax implements GameTree{
    private GameNode root;
    private final int ourPlayerColor;

    public TreeMinMax(StateOfClobber startState, int ourPlayerColor) {
        this.ourPlayerColor = ourPlayerColor;
        this.root = new GameNode(startState, ourPlayerColor, GameNode.NodeType.MAX);
    }

    @Override
    public GameNode getRoot() {
        return this.root;
    }

    @Override
    public GameNode getMaxNode(Evaluator evaluator, int depth, Runnable onNodeVisit){
        return this.root
                .minMaxEvaluate(evaluator, depth, this.ourPlayerColor, onNodeVisit)
                .getOperationalPredecessor();
    }

    @Override
    public void setRootHard(GameNode newRoot) {
        this.root = newRoot;
    }
}
