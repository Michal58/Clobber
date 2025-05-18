package Evaluations;

import StateComponents.StateOfClobber;

import java.util.List;
import java.util.function.BiFunction;

public interface Evaluator {
    int MAX_EVAL = 1000000;

    // later can be used to improve computations
//    default void looper(int height, int width, List<BiFunction<Integer,Integer,Void>> executes) {
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                int finalI = i;
//                int finalJ = j;
//                executes.forEach(e->e.apply(finalI, finalJ));
//            }
//        }
//    }
    double assessState(StateOfClobber gameState, int ourColor);

    // later can be used with looper
//    int startAssessingStateWithLoop(StateOfClobber gameState, int ourColor);
//    int endAssessingStateWithLoop();
}
