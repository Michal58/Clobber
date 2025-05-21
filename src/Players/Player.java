package Players;

import StateComponents.StateOfClobber;

public interface Player {
    record Move(int startRow, int startCol, int targetRow, int targetCol) {}
    long countOfVisitedNodes();
    double secondsTimeOfSearching();

    void initializeGame(int pieceColor);
    void updateWithMove(StateOfClobber updatedGameState);
    StateOfClobber makeMove();

}
