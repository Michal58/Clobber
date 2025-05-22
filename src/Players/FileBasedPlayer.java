package Players;

import StateComponents.StateOfClobber;

public class FileBasedPlayer implements Player{
    private Player acutalPlayer;

    public FileBasedPlayer(Player acutalPlayer) {
        this.acutalPlayer = acutalPlayer;
    }

    @Override
    public long countOfVisitedNodes() {
        return this.acutalPlayer.countOfVisitedNodes();
    }

    @Override
    public double secondsTimeOfSearching() {
        return this.acutalPlayer.secondsTimeOfSearching();
    }

    @Override
    public void initializeGame(int pieceColor) {
        this.acutalPlayer.initializeGame(pieceColor);
    }

    @Override
    public void updateWithMove(StateOfClobber updatedGameState) {

    }

    @Override
    public StateOfClobber makeMove() {
        return null;
    }
}
