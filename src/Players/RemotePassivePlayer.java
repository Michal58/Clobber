package Players;

import StateComponents.StateOfClobber;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RemotePassivePlayer implements Player{
    // line 1: board
    // END_OF_LINE

    public static String END_OF_LINE = "!";
    private String updateChannel;
    private String moveChannel;

    private StateOfClobber collectedState;

    public RemotePassivePlayer(
            String updateChannel,
            String moveChannel
    ) {
        this.updateChannel = updateChannel;
        this.moveChannel = moveChannel;

        this.collectedState = null;
    }

    @Override
    public long countOfVisitedNodes() {
        return -1;
    }

    @Override
    public double secondsTimeOfSearching() {
        return -1;
    }

    @Override
    public void initializeGame(int pieceColor) {
        // ignore
    }

    public void collectMoveFromFileAndClear() throws IOException, InterruptedException {
        List<String> lines = List.of();
        while (lines.isEmpty() || !lines.get(lines.size() - 1).equals(END_OF_LINE)) {
            lines = Files.readAllLines(Paths.get(moveChannel));
            Thread.sleep(50);
        }
        this.collectedState = StateOfClobber.boardFromLine(lines.get(0));

        // erase contents of file
        Files.writeString(Paths.get(moveChannel),"");
    }

    @Override
    public void updateWithMove(StateOfClobber updatedGameState) {
        String firstLine = updatedGameState.boardToLine();
        String secondLine = END_OF_LINE;
        try {
            Files.write(
                    Paths.get(updateChannel),
                    List.of(firstLine, secondLine)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StateOfClobber makeMove() {
        try {
            collectMoveFromFileAndClear();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this.collectedState;
    }
}
