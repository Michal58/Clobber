package StateComponents;

import Players.Player;

import java.awt.*;

public class GameOfClobber {
    public static final int STD_OUT_SEP_COL_COUNT = 50;
    public static final int ERR_OUT_SEP_COL_COUNT = 50;
    public static final int INDENT_SIZE = 10;
    public static final String SYS_ERR_INDENT = " ".repeat(INDENT_SIZE);

    private Player whitePlayer;
    private Player blackPlayer;

    private StateOfClobber currentState;
    private int currentTurn;
    private int gameResult;
    private int movesCount;

    public GameOfClobber(Player whitePlayer, Player blackPlayer, Dimension size) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;

        this.currentTurn = StateOfClobber.BLACK;        // Clobber game starts with black pieces
        this.gameResult = StateOfClobber.CONTINUE;
        this.movesCount = 0;

        this.currentState = new StateOfClobber(size.height, size.width);
    }

    public void updateGameResult() {
        this.gameResult = this.currentState.whatIsGameState(this.currentTurn);
    }

    public boolean isFinished(){
        return gameResult != StateOfClobber.CONTINUE;
    }
    public void displayFinishedGameResult(){
        System.out.println("=".repeat(STD_OUT_SEP_COL_COUNT));
        System.out.println("Game finished!");
        System.out.println("Ultimate board:\n");
        currentState.displayBoard();
        System.out.println();
        System.out.printf("Winner is: %s%n", currentState.getSign(gameResult));
        System.out.printf("Count of moves that game has taken: %s%n", movesCount);
        System.out.println("=".repeat(STD_OUT_SEP_COL_COUNT));

        System.err.println("*".repeat(ERR_OUT_SEP_COL_COUNT));
        System.err.println("White player:");
        System.err.printf("%sVisited nodes: %s%n", SYS_ERR_INDENT, whitePlayer.countOfVisitedNodes());
        System.err.printf("%sSearching time: %ss%n", SYS_ERR_INDENT, whitePlayer.secondsTimeOfSearching());
        System.err.println("Black player:");
        System.err.printf("%sVisited nodes: %s%n", SYS_ERR_INDENT, blackPlayer.countOfVisitedNodes());
        System.err.printf("%sSearching time: %ss%n", SYS_ERR_INDENT, blackPlayer.secondsTimeOfSearching());
        System.err.println("*".repeat(ERR_OUT_SEP_COL_COUNT));
    }
}
