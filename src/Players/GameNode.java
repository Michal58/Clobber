package Players;

import Evaluations.Evaluator;
import StateComponents.StateOfClobber;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
    double evaluationValue;


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

    public void assignEvaluation(double evaluationValue) {
        this.evaluationValue = evaluationValue;
    }

    public void expand() {
        int oppositeColor = baseState.getOpposingCode(currentColorToMove);
        baseState.generateAllPossibleStates(currentColorToMove).forEach(state ->
                children.add(
                        new GameNode(state, oppositeColor, nodeType.opposite())
                )
        );
    }

    public void removeChildren(){
        this.children = null;
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

    public double evaluate(Evaluator evaluator, int depth){
        if (depth == 0) {
//            this.evaluationValue = evaluator.assessState();
        }

        if (this.children == null)
            expandAllChildren();

        children.forEach(child->child.evaluate(evaluator,depth-1));

        if (nodeType == NodeType.MAX) {

        }

        return 0.456789;
    }
}
