package Players;

import Evaluations.Evaluator;
import StateComponents.StateOfClobber;

import java.util.Optional;

public class TreeMinMax {
    private GameNode root;
    private int ourPlayerColor;
    private Evaluator currentEvaluator;

    public TreeMinMax(StateOfClobber startState, int ourPlayerColor) {
        this.ourPlayerColor = ourPlayerColor;
        this.root = new GameNode(startState, ourPlayerColor, GameNode.NodeType.MAX);
    }

    public void changeEvaluator(Evaluator evaluator) {
        this.currentEvaluator = evaluator;
        this.root.removeChildren();
    }

    public GameNode.NodeType getNodeType(int nodeColorToMove) {
        if (nodeColorToMove == ourPlayerColor)
            return GameNode.NodeType.MAX;
        else
            return GameNode.NodeType.MIN;
    }

    public GameNode getRoot() {
        return this.root;
    }

    public void updateEnemyMove(StateOfClobber updatedState) {
        if (root.getBaseState().isSameAs(updatedState))
            throw new RuntimeException("Updated state is not changed");
        Optional<GameNode> nextState = root.getUpdatedState(updatedState);
        if (nextState.isEmpty())
            throw new RuntimeException("State wasn't expanded");

        this.root = nextState.get();
    }

    public void setMaxNodeAsRoot(int depth) {
        GameNode currentNode = root;
        int currentLevel = 0;

        while (currentLevel <= depth) {
            if (currentNode.children == null) {
                currentNode.expandAllChildren();
            }
            if (currentLevel == depth) {

            }
        }

//        this.root = root.getMaxChildren(currentEvaluator, depth);
    }
}
