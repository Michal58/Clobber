import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StateOfClobber {
    public static final int EMPTY = 0;
    public static final int WHITE = 1;
    public static final int BLACK = 2;

    public static final String SIGN_EMPTY = "_";
    public static final String SIGN_WHITE = "W";
    public static final String SIGN_BLACK = "B";

    public static final int CONTINUE = 0;
    public static final int WIN_WHITE = 1;
    public static final int WIN_BLACK = 2;

    private final int[][] board;

    public StateOfClobber(int n, int m) {
        if (n == 0 || m == 0)
            throw new RuntimeException("All board parameters must be greater than 0");
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


    public void displayBoard() {
        Arrays.stream(this.board).forEach(row -> {
            var iter = Arrays.stream(row)
                    .boxed()
                    .map(this::getSign)
                    .collect(Collectors.joining(" "));
            System.out.println(iter);
        });
    }

    public int getOpposingCode(int currentCode) {
        return currentCode == WHITE ? BLACK : WHITE;
    }

    public boolean canPieceMove(int rowIndex, int colIndex, int opposingCode) {
        if (rowIndex > 0 && board[rowIndex-1][colIndex] == opposingCode) {
            return true;
        }
        if (rowIndex < board.length - 1 && board[rowIndex+1][colIndex] == opposingCode) {
            return true;
        }
        if (colIndex > 0 && board[rowIndex][colIndex-1] == opposingCode) {
            return true;
        }
        return colIndex < board[0].length - 1 && board[rowIndex][colIndex + 1] == opposingCode;
    }

    public boolean canPieceMove(int rowIndex, int colIndex) {
        int pieceCode = board[rowIndex][colIndex];
        if (pieceCode == EMPTY)
            throw new RuntimeException("Field is empty");
        return canPieceMove(rowIndex, colIndex, getOpposingCode(pieceCode));
    }

    public int whatIsGameState(int currentTurn) {
        if (currentTurn != WHITE && currentTurn != BLACK)
            throw new RuntimeException("No valid color for player");

        boolean shouldContinue = IntStream.range(0, board.length)
                .anyMatch(i->
                        IntStream.range(0,board[i].length)
                                .anyMatch(j->
                                        board[i][j] == currentTurn && canPieceMove(i, j, getOpposingCode(currentTurn))
                                )
                );
        if (shouldContinue)
            return CONTINUE;
        else
            return currentTurn == WHITE ? WIN_BLACK : WIN_WHITE;
    }
}
