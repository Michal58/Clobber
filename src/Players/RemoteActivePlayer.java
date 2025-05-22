package Players;

import StateComponents.StateOfClobber;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RemoteActivePlayer implements Player{
    public static String END_OF_LINE = "!";
    private String updateChannel;
    private String moveChannel;

    private StateOfClobber stateFromUpdate;
    private Player actualPlayer;
    public RemoteActivePlayer(
            String updateChannel,
            String moveChannel,
            Player actualPlayer
    ) {
        this.updateChannel = updateChannel;
        this.moveChannel = moveChannel;
        this.actualPlayer = actualPlayer;

        this.stateFromUpdate = null;
    }

    @Override
    public long countOfVisitedNodes() {
        return actualPlayer.countOfVisitedNodes();
    }

    @Override
    public double secondsTimeOfSearching() {
        return actualPlayer.secondsTimeOfSearching();
    }

    @Override
    public void initializeGame(int pieceColor) {
        actualPlayer.initializeGame(pieceColor);
    }

    public void collectMoveFromFileAndClear() throws IOException, InterruptedException {
        List<String> lines = List.of();
        while (lines.isEmpty() || !lines.get(lines.size() - 1).equals(END_OF_LINE)) {
            lines = Files.readAllLines(Paths.get(updateChannel));
            Thread.sleep(50);
        }
        this.stateFromUpdate = StateOfClobber.boardFromLine(lines.get(0));

        // erase contents of file
        Files.writeString(Paths.get(updateChannel),"");
    }

    @Override
    public void updateWithMove(StateOfClobber updatedGameState) {
        try {
            collectMoveFromFileAndClear();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        actualPlayer.updateWithMove(stateFromUpdate);
    }

    @Override
    public StateOfClobber makeMove() {
        StateOfClobber move = actualPlayer.makeMove();

        String firstLine = move.boardToLine();
        String secondLine = END_OF_LINE;
        try {
            Files.write(
                    Paths.get(moveChannel),
                    List.of(firstLine, secondLine)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return move;
    }
}
