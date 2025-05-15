import Players.DummyPlayer;

import java.awt.*;

public class Main {
    public static void testStates(){

    }

    public static void main(String[] args) {
        StateOfClobber st1 = new StateOfClobber(10,10);
        StateOfClobber st2 = st1.copy();
        System.out.println("----");
        System.out.println(st1);
        System.out.println(st2);

        st2.displayBoard();

        GameOfClobber cl = new GameOfClobber(new DummyPlayer(), new DummyPlayer(), new Dimension(10,10));
        cl.displayFinishedGameResult();
    }
}