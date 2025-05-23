package Players;

import Evaluations.Evaluator;
import StateComponents.StateOfClobber;

import java.util.*;
import java.util.function.BinaryOperator;

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
    int finishState;
    public double operationalEvaluation;
    private GameNode operationalPredecessor;


    public GameNode(StateOfClobber baseState, int currentColorToMove, NodeType nodeType) {
        this.baseState = baseState;
        this.currentColorToMove = currentColorToMove;
        this.nodeType = nodeType;
        this.children = null;
        this.nextStatesGenerator = null;
        this.finishState = baseState.whatIsGameState(currentColorToMove);
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

        if (finishState != CONTINUE) {
            operationalEvaluation = originalColor == currentColorToMove ? -MAX_EVAL : MAX_EVAL;
            operationalPredecessor = null;
            return this;
        }
        if (depth == 0) {
            operationalEvaluation = evaluator.assessState(
                    baseState,
                    currentColorToMove
            );
            operationalPredecessor = null;
            return this;
        }

        if (children == null)
            expandAllChildren();

        Comparator<GameNode> nodesComparator = Comparator.comparingDouble(o -> o.operationalEvaluation);
        GameNode theBestChild = children.stream()
                .map(child -> child.minMaxEvaluate(evaluator, depth - 1, originalColor, onNodeVisit))
                .reduce(
                        nodeType == NodeType.MAX ?
                                BinaryOperator.maxBy(nodesComparator)
                                : BinaryOperator.minBy(nodesComparator)
                )
                .orElseThrow();
        operationalPredecessor = theBestChild;
        operationalEvaluation = theBestChild.operationalEvaluation;
        return this;
    }

    public GameNode alphaBetaEvaluate(double alpha, double beta, Evaluator evaluator, int depth, int originalColor, Runnable onNodeVisit) {
        onNodeVisit.run();

        if (finishState != CONTINUE) {
            operationalEvaluation = originalColor == currentColorToMove ? -MAX_EVAL : MAX_EVAL;
            operationalPredecessor = null;
            return this;
        }
        if (depth == 0) {
            operationalEvaluation = evaluator.assessState(
                    baseState,
                    currentColorToMove
            );
            operationalPredecessor = null;
            return this;
        }

        if (children == null)
            expandAllChildren();
        Iterator<GameNode> childrenIterator = children.iterator();

        if (nodeType == NodeType.MIN) {
            GameNode betaNode = null;
            while (childrenIterator.hasNext() && alpha < beta) {
                var currentChild = childrenIterator.next();
                var betaNodeCandidate = currentChild.alphaBetaEvaluate(alpha,beta,evaluator,depth-1,originalColor,onNodeVisit);
                double previousBeta = beta;
                beta = Math.min(beta, betaNodeCandidate.operationalEvaluation);
                if (previousBeta != beta) {
                    betaNode = betaNodeCandidate;
                }
            }
            operationalEvaluation = beta;
            operationalPredecessor = betaNode;
            return this;
        } else {
            GameNode alphaNode = null;
            while (childrenIterator.hasNext() && alpha < beta) {
                var currentChild = childrenIterator.next();
                var alphaNodeCandidate = currentChild.alphaBetaEvaluate(alpha,beta,evaluator,depth-1,originalColor,onNodeVisit);
                double previousAlpha = alpha;
                alpha = Math.max(alpha, alphaNodeCandidate.operationalEvaluation);
                if (previousAlpha != alpha) {
                    alphaNode = alphaNodeCandidate;
                }
            }
            operationalEvaluation = alpha;
            operationalPredecessor = alphaNode;
            return this;
        }
    }

    public GameNode getOperationalPredecessor() {
        return operationalPredecessor;
    }
}
