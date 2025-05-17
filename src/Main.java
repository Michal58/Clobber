import Players.DummyPlayer;

import java.awt.*;

public class Main {
    public static void testStates(){
        StateOfClobber st1 = new StateOfClobber(5, 6);
        System.out.println("Gen first");
        var states = st1.generatePossibleStates(StateOfClobber.WHITE);
        states.forEach(s-> {
                    s.displayBoard();
                    System.out.println("------");
                }
        );
        System.out.println(states.size());
        System.out.println("=============");
        states = states.get(0).generatePossibleStates(StateOfClobber.BLACK);
        states.forEach(s-> {
                    s.displayBoard();
                    System.out.println("------");
                }
        );
        System.out.println(states.size());
    }

    public static void main(String[] args) {
//        StateOfClobber st1 = new StateOfClobber(5,6);
//        StateOfClobber st2 = st1.copy();
//        System.out.println("----");
//        System.out.println(st1);
//        System.out.println(st2);
//
//        st2.displayBoard();
//
//        GameOfClobber cl = new GameOfClobber(new DummyPlayer(), new DummyPlayer(), new Dimension(6,5));
//        cl.displayFinishedGameResult();

        testStates();
    }
}