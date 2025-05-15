import java.util.Arrays;
import java.util.stream.Collectors;

public class StateOfClobber {
    public static final int EMPTY = 0;
    public static final int WHITE = 1;
    public static final int BLACK = 2;

    public static final String SIGN_EMPTY = "_";
    public static final String SIGN_WHITE = "W";
    public static final String SIGN_BLACK = "B";

    private int[][] board;

    public StateOfClobber(int n, int m) {
        this.board = new int[n][m];
        fillBoard();
    }

    public void fillBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = WHITE + (j + i) % 2;
            }
        }
    }

    private StateOfClobber(int[][] boardToAssign) {
        this.board = boardToAssign;
    }

    public StateOfClobber copy() {
        int[][] arrayCopy = Arrays.stream(board)
                .map(int[]::clone)
                .toArray(int[][]::new);
        return new StateOfClobber(arrayCopy);
    }

    public String getSign(int code) {
        return switch (code) {
            case EMPTY -> SIGN_EMPTY;
            case WHITE -> SIGN_WHITE;
            case BLACK -> SIGN_BLACK;
            default -> throw new RuntimeException("Unexpected mapping");
        };
    }


    public void printBoard() {
        Arrays.stream(this.board).forEach(row -> {
            var iter = Arrays.stream(row)
                    .boxed()
                    .map(this::getSign)
                    .collect(Collectors.joining(" "));
            System.out.println(iter);
        });
    }
}
