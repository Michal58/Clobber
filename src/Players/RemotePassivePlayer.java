package Players;

import StateComponents.StateOfClobber;

import java.io.IOException;

public class RemotePassivePlayer implements Player{
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

    @Override
    public void updateWithMove(StateOfClobber updatedGameState) {
        RemoteActivePlayer.saveState(updateChannel,updatedGameState);
    }

    @Override
    public StateOfClobber makeMove() {
        try {
            RemoteActivePlayer.collectMoveFromFileAndClear(moveChannel,line->this.collectedState=line);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this.collectedState;
    }
}
