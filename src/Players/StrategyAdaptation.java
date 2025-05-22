package Players;

import Evaluations.Evaluator;
import StateComponents.StateOfClobber;

public interface StrategyAdaptation {
    Evaluator getAdaptedHeuristic(StateOfClobber currentState);
}
