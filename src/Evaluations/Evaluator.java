package Evaluations;

import StateComponents.StateOfClobber;

import java.util.List;
import java.util.function.BiFunction;

public interface Evaluator {
    int MAX_EVAL = 1000000;
    double assessState(StateOfClobber gameState, int ourColor);
}
