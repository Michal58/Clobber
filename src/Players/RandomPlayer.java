package Players;

import StateComponents.StateOfClobber;
import Utils.Utils;

public class RandomPlayer implements Player {
    private int pieceColor;
    private StateOfClobber currentState;

    private int countOfVisitedNodes;
    private double secondsOnSearch;


    @Override
    public long countOfVisitedNodes() {
        return countOfVisitedNodes;
    }

    @Override
    public double secondsTimeOfSearching() {
        return secondsOnSearch;
    }

    @Override
    public void initializeGame(int pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public void updateWithMove(StateOfClobber updatedGameState) {
        this.currentState = updatedGameState;
    }

    @Override
    public StateOfClobber makeMove() {
        Utils.StopWatch stopWatch = new Utils.StopWatch();
        stopWatch.startTime();
        var states = currentState.generateAllPossibleStates(pieceColor);
        stopWatch.stopTime();
        secondsOnSearch += stopWatch.getTimeSeconds();
        countOfVisitedNodes += states.size();
        return states.get(Utils.GlobRandom.INSTANCE.random.nextInt(0,states.size()));
    }
}
