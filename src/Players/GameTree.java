package Players;

import Evaluations.Evaluator;
import StateComponents.StateOfClobber;

public interface GameTree {
    GameNode getRoot();
    void updateEnemyMove(StateOfClobber updatedState);
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
