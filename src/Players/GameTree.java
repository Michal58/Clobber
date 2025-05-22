package Players;

import Evaluations.Evaluator;
import StateComponents.StateOfClobber;

import java.util.Optional;

public interface GameTree {
    GameNode getRoot();
    default void updateEnemyMove(StateOfClobber updatedState) {
        var root = getRoot();
        if (root.getBaseState().isSameAs(updatedState))
            throw new RuntimeException("Updated state is not changed");
        Optional<GameNode> nextState = root.getUpdatedState(updatedState);
        if (nextState.isEmpty())
            throw new RuntimeException("State wasn't expanded");

        setRootHard(nextState.get());
    }
    GameNode getMaxNode(Evaluator evaluator, int depth, Runnable onNodeVisit);
    void setRootHard(GameNode newRoot);
    default void updateWithMaxNode(GameNode maxState) {
        GameNode newRoot = getRoot()
                .getChildren()
                .stream()
                .filter(child->child == maxState)
                .findFirst()
                .orElseThrow();
        setRootHard(newRoot);
    }
}
