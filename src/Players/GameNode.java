package Players;

import Evaluations.Evaluator;
import StateComponents.StateOfClobber;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static Evaluations.Evaluator.MAX_EVAL;
import static StateComponents.StateOfClobber.CONTINUE;

public class GameNode {
    public enum NodeType {
        MIN,
        MAX;
        public NodeType opposite() {
            return this == MAX ? MIN : MAX;
        }
    }

    StateOfClobber baseState;
    Iterator<StateOfClobber> nextStatesGenerator;
    List<GameNode> children;
    NodeType nodeType;
    int currentColorToMove;
    boolean isFinished;
    private double operationalEvaluation;


    public GameNode(StateOfClobber baseState, int currentColorToMove, NodeType nodeType) {
        this.baseState = baseState;
        this.currentColorToMove = currentColorToMove;
        this.nodeType = nodeType;
        this.children = null;
    }

    public StateOfClobber getBaseState() {
        return baseState;
    }

    public Optional<GameNode> getUpdatedState(StateOfClobber updatedState) {
        GameNode matchingNode = null;

        Iterator<GameNode> childrenIterator = children.iterator();
        GameNode currentChild = null;

        while (matchingNode == null && childrenIterator.hasNext()) {
            currentChild = childrenIterator.next();
            if (currentChild.baseState.isSameAs(updatedState))
                matchingNode = currentChild;
        }

        return Optional.ofNullable(currentChild);
    }

    public void expand() {
        int oppositeColor = baseState.getOpposingCode(currentColorToMove);
        baseState.generateAllPossibleStates(currentColorToMove).forEach(state ->
                children.add(
                        new GameNode(state, oppositeColor, nodeType.opposite())
                )
        );
    }

    public void expandAllChildren() {
        int oppositeColor = baseState.getOpposingCode(currentColorToMove);
        this.children = baseState.generateAllPossibleStates(currentColorToMove)
                .stream()
                .map(nextState->
                        new GameNode(nextState,oppositeColor,nodeType.opposite())
                )
                .toList();
    }

    public List<GameNode> getChildren() {
        return this.children;
    }

    public GameNode minMaxEvaluate(Evaluator evaluator, int depth, int originalColor, Runnable onNodeVisit){
        onNodeVisit.run();

        if (isFinished || baseState.whatIsGameState(currentColorToMove) != CONTINUE) {
            isFinished = true;
            operationalEvaluation = originalColor == currentColorToMove ? -MAX_EVAL : MAX_EVAL;
            return this;
        }
        if (depth == 0) {
            operationalEvaluation = evaluator.assessState(
                    baseState,
                    currentColorToMove
            );
            return this;
        }

        if (this.children == null)
            expandAllChildren();

        Comparator<GameNode> nodesComparator = Comparator.comparingDouble(o -> o.operationalEvaluation);
        return children.stream()
                .map(child -> child.minMaxEvaluate(evaluator, depth - 1, originalColor, onNodeVisit))
                .reduce(
                        nodeType == NodeType.MAX ?
                                BinaryOperator.maxBy(nodesComparator)
                                : BinaryOperator.minBy(nodesComparator)
                )
                .orElseThrow();
    }
}
