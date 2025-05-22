package StateComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StateOfClobber {
    public static final int INVALID_COLOR = -1;
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

    private void checkForEmpty(int rowIndex, int colIndex) {
        if (board[rowIndex][colIndex] == EMPTY)
            throw new RuntimeException("Empty field cannot be moved");
    }

    public boolean canPieceMoveUp(int rowIndex, int colIndex, int opposingCode) {
        checkForEmpty(rowIndex, colIndex);
        return rowIndex > 0 && board[rowIndex-1][colIndex] == opposingCode;
    }

    public boolean canPieceMoveDown(int rowIndex, int colIndex, int opposingCode) {
        checkForEmpty(rowIndex, colIndex);
        return rowIndex < board.length - 1 && board[rowIndex+1][colIndex] == opposingCode;
    }

    public boolean canPieceMoveLeft(int rowIndex, int colIndex, int opposingCode) {
        checkForEmpty(rowIndex, colIndex);
        return colIndex > 0 && board[rowIndex][colIndex-1] == opposingCode;
    }

    public boolean canPieceMoveRight(int rowIndex, int colIndex, int opposingCode) {
        checkForEmpty(rowIndex, colIndex);
        return colIndex < board[0].length - 1 && board[rowIndex][colIndex + 1] == opposingCode;
    }

    public boolean hasPieceAnyMove(int rowIndex, int colIndex, int opposingCode) {
        return canPieceMoveRight(rowIndex, colIndex, opposingCode)
                || canPieceMoveLeft(rowIndex, colIndex, opposingCode)
                || canPieceMoveDown(rowIndex, colIndex, opposingCode)
                || canPieceMoveUp(rowIndex, colIndex, opposingCode);
    }

    public boolean hasPieceAnyMove(int rowIndex, int colIndex) {
        int pieceCode = board[rowIndex][colIndex];
        if (pieceCode == EMPTY)
            throw new RuntimeException("Field is empty");
        return hasPieceAnyMove(rowIndex, colIndex, getOpposingCode(pieceCode));
    }

    public boolean isThereRightPieceTake(int rowPos, int colPos, int colorToTake) {
        return canPieceMoveRight(rowPos, colPos, colorToTake) && board[rowPos][colPos + 1] == colorToTake;
    }

    public boolean isThereLeftPieceTake(int rowPos, int colPos, int colorToTake) {
        return canPieceMoveLeft(rowPos, colPos, colorToTake) && board[rowPos][colPos - 1] == colorToTake;
    }

    public boolean isThereDownPieceTake(int rowPos, int colPos, int colorToTake) {
        return canPieceMoveDown(rowPos, colPos, colorToTake) && board[rowPos + 1][colPos] == colorToTake;
    }

    public boolean isThereUpPieceTake(int rowPos, int colPos, int colorToTake) {
        return canPieceMoveUp(rowPos, colPos, colorToTake) && board[rowPos - 1][colPos] == colorToTake;
    }

    public int howManyTakesPieceHas(int rowPos, int colPos) {
        if (board[rowPos][colPos] == EMPTY)
            throw new RuntimeException("Field is empty");
        int opposingCode = getOpposingCode(board[rowPos][colPos]);
        int takesCount = 0;
        if (isThereRightPieceTake(rowPos, colPos, opposingCode)) {
            takesCount++;
        }
        if (isThereLeftPieceTake(rowPos, colPos, opposingCode)) {
            takesCount++;
        }
        if (isThereDownPieceTake(rowPos, colPos, opposingCode)) {
            takesCount++;
        }
        if (isThereUpPieceTake(rowPos, colPos, opposingCode)) {
            takesCount++;
        }

        return takesCount;
    }

    public int whatIsGameState(int currentTurnColor) {
        if (currentTurnColor != WHITE && currentTurnColor != BLACK)
            throw new RuntimeException("No valid color for player");

        boolean shouldContinue = IntStream.range(0, board.length)
                .anyMatch(i->
                        IntStream.range(0,board[i].length)
                                .anyMatch(j->
                                        board[i][j] == currentTurnColor && hasPieceAnyMove(i, j, getOpposingCode(currentTurnColor))
                                )
                );
        if (shouldContinue)
            return CONTINUE;
        else
            return currentTurnColor == WHITE ? WIN_BLACK : WIN_WHITE;
    }

    public StateOfClobber generateUncheckedStateWithMove(int currentRow, int currentCol, int nextRow, int nextCol) {
        int playerCode = board[currentRow][currentCol];
        StateOfClobber nextState = copy();
        nextState.board[currentRow][currentCol] = EMPTY;
        nextState.board[nextRow][nextCol] = playerCode;
        return nextState;
    }

    private void addPossiblePlayerMoves(int i, int j, List<StateOfClobber> nextStates, int playerToMove, int opposingCode) {
        if (board[i][j] == playerToMove) {
            if (canPieceMoveRight(i, j, opposingCode)) {
                nextStates.add(
                        generateUncheckedStateWithMove(i, j, i, j + 1)
                );
            }
            if (canPieceMoveLeft(i, j, opposingCode)) {
                nextStates.add(
                        generateUncheckedStateWithMove(i, j, i, j - 1)
                );
            }
            if (canPieceMoveUp(i, j, opposingCode)) {
                nextStates.add(
                        generateUncheckedStateWithMove(i, j, i - 1, j)
                );
            }
            if (canPieceMoveDown(i, j, opposingCode)) {
                nextStates.add(
                        generateUncheckedStateWithMove(i, j, i + 1, j)
                );
            }
        }
    }

    public List<StateOfClobber> generateAllPossibleStates(int playerToMove) {
        List<StateOfClobber> nextStates = new ArrayList<>();
        final int opposingCode = getOpposingCode(playerToMove);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                addPossiblePlayerMoves(i, j, nextStates, playerToMove, opposingCode);
            }
        }

        return nextStates;
    }

    public Iterator<StateOfClobber> getStatesGenerator(int colorToMove) {
        return new Iterator<>() {
            private int mainI = 0;
            private int mainJ = 0;
            private int mainK = 0;
            private final int[][] DIFF = {
                    {0,1},
                    {0,-1},
                    {-1,0},
                    {1,0}
            };
            private StateOfClobber nextToReturn = findNext();
            @Override
            public boolean hasNext() {
                return nextToReturn != null;
            }

            private StateOfClobber findNext() {
                for (int i = mainI; i < board.length; i++) {
                    for (int j = mainJ; j < board[i].length; j++) {
                        if (board[i][j] == colorToMove) {
                            for (int k = mainK; k < DIFF.length; k++) {
                                int targetRow = i + DIFF[k][0];
                                int targetCol = j + DIFF[k][1];
                                if (targetRow >= 0 && targetRow < board.length && targetCol >= 0 && targetCol < board[i].length) {
                                    mainI = i;
                                    mainJ = j;
                                    mainK = k + 1;
                                    return copy().generateUncheckedStateWithMove(i, j, targetRow, targetCol);
                                }
                            }
                        }
                        mainK = 0;
                    }
                    mainJ=0;
                }
                return null;
            }

            @Override
            public StateOfClobber next() {
                if (!hasNext())
                    throw new RuntimeException("Has not next");
                var toReturn = nextToReturn;
                nextToReturn = findNext();
                return toReturn;
            }
        };
    }

    public int[][] getBoard() {
        return board;
    }

    public int getHeight() {
        return board.length;
    }

    public int getWidth() {
        return board[0].length;
    }

    public int getColor(int rowPos, int colPos) {
        return board[rowPos][colPos];
    }

    public int getDoublingCount(int i, int j) {
        int color = board[i][j];
        int count = 0;
        if (i > 0 && board[i-1][j] == color && hasPieceAnyMove(i-1,j)) {
            count++;
        }
        if (i < board.length-1 && board[i+1][j] == color && hasPieceAnyMove(i+1,j)) {
            count++;
        }
        if (j > 0 && board[i][j-1] == color && hasPieceAnyMove(i, j-1)){
            count++;
        }
        if (j < board[0].length-1 && board[i][j+1] == color && hasPieceAnyMove(i,j+1)) {
            count++;
        }
        return count;
    }

    public void clearField(int rowPos, int colPos) {
        board[rowPos][colPos] = EMPTY;
    }

    public boolean isSameAs(StateOfClobber other) {
        return IntStream.range(0, this.board.length)
                .allMatch(i->
                        Arrays.equals(this.board[i], other.board[i])
                );
    }


    public static final String COL_FILE_SEP = "1";
    public static final String ROW_FILE_SEP = "2";

    public static int signToColor(String sign){
        return switch (sign) {
            case SIGN_WHITE -> WHITE;
            case SIGN_BLACK -> BLACK;
            case SIGN_EMPTY -> EMPTY;
            default -> throw new RuntimeException("Not sign to color founded for: " + sign);
        };
    }

    public String boardToLine() {
        List<String> rowsAsString = new ArrayList<>();

        for (int[] row : board) {
            String[] symbols = Arrays.stream(row)
                    .mapToObj(this::getSign)
                    .toArray(String[]::new);
            rowsAsString.add(String.join(COL_FILE_SEP, symbols));
        }

        return String.join(ROW_FILE_SEP, rowsAsString);
    }
    public static StateOfClobber boardFromLine(String lineWithBoard) {
        String[] rows = lineWithBoard.split(ROW_FILE_SEP);
        int[][] clobberBoard = new int[rows.length][];

        int i=0;
        for (String row : rows) {
            String[] elements = row.split(COL_FILE_SEP);
            clobberBoard[i] = Arrays.stream(elements)
                    .mapToInt(StateOfClobber::signToColor)
                    .toArray();
            i++;
        }

        return new StateOfClobber(clobberBoard);
    }
}
