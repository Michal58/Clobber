import Evaluations.*;
import Players.RandomPlayer;
import Players.StrategyAdaptationHeuristic;
import Players.TreePlayer;
import StateComponents.GameOfClobber;
import StateComponents.StateOfClobber;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Main {
    public static void testStates(){
        StateOfClobber st1 = new StateOfClobber(5, 6);
        System.out.println("Gen first");
        var states = st1.generateAllPossibleStates(StateOfClobber.WHITE);
        states.forEach(s-> {
                    s.displayBoard();
                    System.out.println("------");
                }
        );
        System.out.println(states.size());
        System.out.println("=============");
        states = states.get(0).generateAllPossibleStates(StateOfClobber.BLACK);
        states.forEach(s-> {
                    s.displayBoard();
                    System.out.println("------");
                }
        );
        System.out.println(states.size());
    }

    public static StateOfClobber performMoveSequence1(){
        StateOfClobber st1 = new StateOfClobber(3, 4);
        st1 = st1.generateUncheckedStateWithMove(2, 1, 2 , 0);
        st1.displayBoard();
        System.out.println("====");
        st1 = st1.generateUncheckedStateWithMove(1,1,1,2);
        st1.displayBoard();
        System.out.println("====");
        st1 = st1.generateUncheckedStateWithMove(0,2,0,3);
        st1.displayBoard();
        System.out.println("=====");
        st1 = st1.generateUncheckedStateWithMove(0,1,0,0);
        st1.displayBoard();
        System.out.println("=====");

        return st1;
    }

    public static StateOfClobber performMoveSequence2(){
        StateOfClobber st1 = new StateOfClobber(3,3);
        st1.clearField(1,0);
        st1 = st1.generateUncheckedStateWithMove(1,2,1,1);
        st1.displayBoard();
        System.out.println("=====");
        return st1;
    }

    public static void testPossibleMovesHeuristics(){
        StateOfClobber st1 = new StateOfClobber(3, 4);
        st1 = st1.generateUncheckedStateWithMove(2, 1, 2 , 0);
        st1.displayBoard();
        System.out.println("====");
        st1 = st1.generateUncheckedStateWithMove(1,1,1,2);
        st1.displayBoard();
        System.out.println("====");
        st1 = st1.generateUncheckedStateWithMove(0,2,0,3);
        st1.displayBoard();
        System.out.println("=====");
        st1 = st1.generateUncheckedStateWithMove(0,1,0,0);
        st1.displayBoard();
        System.out.println("=====");

        // outcome is always 0

        CountOfMovesHeuristic heu1 = new CountOfMovesHeuristic();
        System.out.println(heu1.assessState(st1, StateOfClobber.BLACK));
        System.out.println(heu1.assessState(st1, StateOfClobber.WHITE));

        // is game won

        System.out.println("=====");
        st1 = st1.generateUncheckedStateWithMove(2,3,1,3);
        st1 = st1.generateUncheckedStateWithMove(0,3,1,3);
        st1.displayBoard();
        System.out.println("=====");
        System.out.println(st1.whatIsGameState(StateOfClobber.BLACK) == StateOfClobber.WIN_WHITE);
    }

    public static void testPieceMoveAbilityHeuristic() {
        StateOfClobber st1 = performMoveSequence1();

        PieceMoveAbilityHeuristic heuristic = new PieceMoveAbilityHeuristic();
        System.out.println(heuristic.assessState(st1, StateOfClobber.BLACK));
        System.out.println(heuristic.assessState(st1, StateOfClobber.WHITE));
    }



    public static void testPiecesCountHeuristic() {
        StateOfClobber st1 = new StateOfClobber(3, 4);
        st1 = st1.generateUncheckedStateWithMove(2, 1, 2 , 0);
        st1.displayBoard();
        System.out.println("====");

        PiecesCountHeuristic heur = new PiecesCountHeuristic();
        System.out.println(heur.assessState(st1, StateOfClobber.BLACK));
        System.out.println(heur.assessState(st1, StateOfClobber.WHITE));
    }
    public static void testIsolationAdvantageHeuristic() {
        StateOfClobber st1 = performMoveSequence1();

        IsolationAdvantageHeuristic heuristic = new IsolationAdvantageHeuristic();
        System.out.println(heuristic.assessState(st1,StateOfClobber.BLACK));
        System.out.println(heuristic.assessState(st1,StateOfClobber.WHITE));
    }

    public static void testDoublingHeuristic() {
        StateOfClobber st1 = performMoveSequence2();
        DoublingPositionHeuristic heuristic = new DoublingPositionHeuristic();
        System.out.println(heuristic.assessState(st1,StateOfClobber.BLACK));
        System.out.println(heuristic.assessState(st1,StateOfClobber.WHITE));
    }

    public static void testCentralityHeuristic() {
        StateOfClobber st1 = performMoveSequence2();
        CentralityHeuristic heuristic = new CentralityHeuristic();
        System.out.println(heuristic.assessState(st1,StateOfClobber.BLACK));
        System.out.println(heuristic.assessState(st1,StateOfClobber.WHITE));
    }

    public static void testWeightedMovesCountHeuristic() {
        StateOfClobber st1 = performMoveSequence2();
        WeightedCountOfMovesHeuristic heuristic = new WeightedCountOfMovesHeuristic();
        System.out.println(heuristic.assessState(st1,StateOfClobber.BLACK));
        System.out.println(heuristic.assessState(st1,StateOfClobber.WHITE));
    }

    public static void testStateIterator() {
        int[][] toCheck = {
                {1,1},
                {1,2},
                {2,1},
                {2,2},
                {3,3},
                {10,10},
                {100,100},
                {200,100},
                {100,200}
        };

        for (int[] nAndM : toCheck) {
            StateOfClobber st = new StateOfClobber(nAndM[0], nAndM[1]);
            for (int color = 1; color < 3; color++) {
                System.out.println("start base");
                long start = System.currentTimeMillis();
                List<StateOfClobber> baseStates = st.generateAllPossibleStates(color);
                System.out.println((System.currentTimeMillis()-start)/1000.0);
                System.out.println("end base");
                start = System.currentTimeMillis();
                var it = st.getStatesGenerator(color);
                while (it.hasNext()) {
                    StateOfClobber nextGenerated = it.next();
                    if(!nextGenerated.isSameAs(baseStates.get(0)))
                        throw new RuntimeException("Not the same");
                    baseStates.remove(0);
                }
                if(!baseStates.isEmpty())
                    throw new RuntimeException("Not empty");
                System.out.println("Ok");
                System.out.println((System.currentTimeMillis()-start)/1000.0);
            }
        }
    }

    public static void testComplexHeuristic() {
        StateOfClobber st1 = performMoveSequence2();

        System.out.println("Ability");
        Evaluator heuristic = new PieceMoveAbilityHeuristic();
        System.out.println(heuristic.assessState(st1, StateOfClobber.BLACK));
        System.out.println(heuristic.assessState(st1, StateOfClobber.WHITE));

        System.out.println("Isolation");
        heuristic = new IsolationAdvantageHeuristic();
        System.out.println(heuristic.assessState(st1,StateOfClobber.BLACK));
        System.out.println(heuristic.assessState(st1,StateOfClobber.WHITE));

        System.out.println("Doubling");
        heuristic = new DoublingPositionHeuristic();
        System.out.println(heuristic.assessState(st1,StateOfClobber.BLACK));
        System.out.println(heuristic.assessState(st1,StateOfClobber.WHITE));

        System.out.println("Centrality");
        heuristic = new CentralityHeuristic();
        System.out.println(heuristic.assessState(st1,StateOfClobber.BLACK));
        System.out.println(heuristic.assessState(st1,StateOfClobber.WHITE));

        System.out.println("Complex");
        heuristic = new TunableComplexHeuristic(
                10,
                1,
                2,
                0.1,
                0.25
        );
        System.out.println(heuristic.assessState(st1,StateOfClobber.BLACK));
        System.out.println(heuristic.assessState(st1,StateOfClobber.WHITE));
    }


    public static void conductRandomGame() {
        var p1 = new RandomPlayer();
        var p2 = new RandomPlayer();
        GameOfClobber game = new GameOfClobber(p1, p2, new Dimension(3,3));
        game.conductGame(st-> {
            System.out.println("----");
            st.displayBoard();
            System.out.println("----");
        });
        game.displayFinishedGameResult();
    }

    public static void conductMinMaxGame() {
        int depth = 3;
        var p1 = new TreePlayer(depth, TreePlayer.TreeType.MIN_MAX, TreePlayer.selectedStrategyAdaptation(0));
        var p2 = new TreePlayer(depth, TreePlayer.TreeType.MIN_MAX, TreePlayer.selectedStrategyAdaptation(0));
        GameOfClobber game = new GameOfClobber(p1, p2, new Dimension(5,6));
        game.conductGame(st-> {
            System.out.println("----");
            st.displayBoard();
            System.out.println("----");
        });
        game.displayFinishedGameResult();
    }

    public static void conductAlphaBetaGame() {
        int depth = 3;
        var p1 = new TreePlayer(depth, TreePlayer.TreeType.ALPHA_BETA, TreePlayer.selectedStrategyAdaptation(0));
        var p2 = new TreePlayer(depth, TreePlayer.TreeType.ALPHA_BETA, TreePlayer.selectedStrategyAdaptation(0));
        GameOfClobber game = new GameOfClobber(p1, p2, new Dimension(5,6));
        game.conductGame(st-> {
            System.out.println("----");
            st.displayBoard();
            System.out.println("----");
        });
        game.displayFinishedGameResult();
    }


    public static void arrangeHeuristicComparison() throws InterruptedException{
        Map<Integer,String> heurMap = Map.of(
                0,"Only weighted moves",
                1,"Tuned heuristic",
                2,"End game tune"
        );

        // same heuristics comparison
        for (int i = 0; i < 3; i++) {
            int depth = 4;
            var p1 = new TreePlayer(depth, TreePlayer.TreeType.ALPHA_BETA, TreePlayer.selectedStrategyAdaptation(i));
            var p2 = new TreePlayer(depth, TreePlayer.TreeType.ALPHA_BETA, TreePlayer.selectedStrategyAdaptation(i));
            GameOfClobber game = new GameOfClobber(p1, p2, new Dimension(5,6));
            game.conductGame(st-> {
            });
            System.out.println("Two players same heuristic " + heurMap.get(i));
            game.displayFinishedGameResult();
            Thread.sleep(1000);
        }

        System.out.println("-".repeat(50) + "Adaptation phase as black");
        for (int i = 0; i < 3; i++) {
            int depth = 4;

            var p1 = new TreePlayer(depth, TreePlayer.TreeType.ALPHA_BETA, TreePlayer.selectedStrategyAdaptation(i));
            var p2 = new TreePlayer(depth, TreePlayer.TreeType.ALPHA_BETA, TreePlayer.realStrategyAdaptationGetter());
            GameOfClobber game = new GameOfClobber(p1, p2, new Dimension(5,6));
            game.conductGame(st-> {
            });
            System.out.println("White player " + heurMap.get(i));
            game.displayFinishedGameResult();
            Thread.sleep(1000);
        }

        System.out.println("-".repeat(50) + "End game tuned heuristic contest with others and itself (as white)");
        for (int i = 0; i < 3; i++) {
            int depth = 4;

            var p1 = new TreePlayer(depth, TreePlayer.TreeType.ALPHA_BETA, TreePlayer.selectedStrategyAdaptation(2));
            var p2 = new TreePlayer(depth, TreePlayer.TreeType.ALPHA_BETA, TreePlayer.selectedStrategyAdaptation(i));
            GameOfClobber game = new GameOfClobber(p1, p2, new Dimension(5, 6));
            game.conductGame(st -> {
            });
            game.displayFinishedGameResult();
            System.out.println("Enemy as: " + heurMap.get(i));
            Thread.sleep(1000);
        }


        System.out.println("-".repeat(50) + "Adaptation phase as white");
        for (int i = 0; i < 3; i++) {
            int depth = 4;

            var p1 = new TreePlayer(depth, TreePlayer.TreeType.ALPHA_BETA, TreePlayer.selectedStrategyAdaptation(i));
            var p2 = new TreePlayer(depth, TreePlayer.TreeType.ALPHA_BETA, TreePlayer.realStrategyAdaptationGetter());
            GameOfClobber game = new GameOfClobber(p2, p1, new Dimension(5,6));
            game.conductGame(st-> {
            });
            System.out.println("White player " + heurMap.get(i));
            game.displayFinishedGameResult();
            Thread.sleep(1000);
        }

        System.out.println("-".repeat(50) + "Two adaptations contest");

        int depth = 4;

        var p1 = new TreePlayer(depth, TreePlayer.TreeType.ALPHA_BETA, _toIgnore->new StrategyAdaptationHeuristic(0.25,0.75));
        var p2 = new TreePlayer(depth, TreePlayer.TreeType.ALPHA_BETA, _toIgnore->new StrategyAdaptationHeuristic(0.75,0.25));
        GameOfClobber game = new GameOfClobber(p1, p2, new Dimension(5,6));
        game.conductGame(st-> {
        });
        System.out.println("Black player: move ability focus");
        System.out.println("White player: move focus");
        game.displayFinishedGameResult();
        Thread.sleep(1000);
    }

    public static void main(String[] args) {
//        StateComponents.StateOfClobber st1 = new StateComponents.StateOfClobber(5,6);
//        StateComponents.StateOfClobber st2 = st1.copy();
//        System.out.println("----");
//        System.out.println(st1);
//        System.out.println(st2);
//
//        st2.displayBoard();
//
//        StateComponents.GameOfClobber cl = new StateComponents.GameOfClobber(new DummyPlayer(), new DummyPlayer(), new Dimension(6,5));
//        cl.displayFinishedGameResult();

//        testStates();
//        testPossibleMovesHeuristics();
//        testPieceMoveAbilityHeuristic();
//        testPiecesCountHeuristic();
//        testIsolationAdvantageHeuristic();
//        testDoublingHeuristic();
//        testCentralityHeuristic();
//        testWeightedMovesCountHeuristic();
//        testComplexHeuristic();
//        testStateIterator();
//        conductRandomGame();
//        conductMinMaxGame();
//        conductAlphaBetaGame();

        try {
            arrangeHeuristicComparison();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}