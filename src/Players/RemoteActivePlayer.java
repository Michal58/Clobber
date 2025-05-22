package Players;

import StateComponents.StateOfClobber;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

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

    public static void collectMoveFromFileAndClear(String channel, Consumer<StateOfClobber> extractedLineConsumer) throws IOException, InterruptedException {
        List<String> lines = List.of();
        Path path = Paths.get(channel);
        while (lines.isEmpty() || !lines.get(lines.size() - 1).equals(END_OF_LINE)) {
            lines = Files.readAllLines(path);
            Thread.sleep(50);
        }
        extractedLineConsumer.accept(
                StateOfClobber.boardFromLine(lines.get(0))
        );

        // erase contents of file
        Files.writeString(path,"");
    }

    @Override
    public void updateWithMove(StateOfClobber updatedGameState) {
        try {
            collectMoveFromFileAndClear(
                    updateChannel,
                    line->this.stateFromUpdate=line
            );
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        actualPlayer.updateWithMove(stateFromUpdate);
    }

    public static void saveState(String channel, StateOfClobber state) {
        String firstLine = state.boardToLine();
        String secondLine = END_OF_LINE;
        try {
            Files.write(
                    Paths.get(channel),
                    List.of(firstLine, secondLine)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StateOfClobber makeMove() {
        StateOfClobber move = actualPlayer.makeMove();

        saveState(moveChannel,move);

        return move;
    }
}
