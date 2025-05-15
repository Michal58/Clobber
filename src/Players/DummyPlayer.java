package Players;

public class DummyPlayer implements Player {
    @Override
    public long countOfVisitedNodes() {
        return 1000;
    }

    @Override
    public long secondsTimeOfSearching() {
        return 100;
    }
}
