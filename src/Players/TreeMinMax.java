package Players;

import Evaluations.Evaluator;
import StateComponents.StateOfClobber;

import java.util.Optional;
import java.util.function.Consumer;

public class TreeMinMax implements GameTree{
    private GameNode root;
    private final int ourPlayerColor;

    public TreeMinMax(StateOfClobber startState, int ourPlayerColor) {
        this.ourPlayerColor = ourPlayerColor;
        this.root = new GameNode(startState, ourPlayerColor, GameNode.NodeType.MAX);
    }

    public GameNode.NodeType getNodeType(int nodeColorToMove) {
        if (nodeColorToMove == ourPlayerColor)
            return GameNode.NodeType.MAX;
        else
            return GameNode.NodeType.MIN;
    }

    @Override
    public GameNode getRoot() {
        return this.root;
    }

    @Override
    public void updateEnemyMove(StateOfClobber updatedState) {
        if (root.getBaseState().isSameAs(updatedState))
            throw new RuntimeException("Updated state is not changed");
        Optional<GameNode> nextState = root.getUpdatedState(updatedState);
        if (nextState.isEmpty())
            throw new RuntimeException("State wasn't expanded");

        this.root = nextState.get();
    }

    @Override
    public GameNode getMaxNode(Evaluator evaluator, int depth, Runnable onNodeVisit){
        return this.root.minMaxEvaluate(evaluator, depth, this.ourPlayerColor, onNodeVisit);
    }

    @Override
    public void setRootHard(GameNode newRoot) {
        this.root = newRoot;
    }
}
