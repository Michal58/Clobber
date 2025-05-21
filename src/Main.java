import Evaluations.*;
import StateComponents.StateOfClobber;

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
        testComplexHeuristic();
    }
}