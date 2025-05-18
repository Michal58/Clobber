package Evaluations;

import StateComponents.StateOfClobber;

public class CentralityHeuristic implements Evaluator{
    @Override
    public double assessState(StateOfClobber gameState, int ourColor) {
        int n = gameState.getHeight();
        int m = gameState.getWidth();

        double ourCentrality = 0;
        double theirCentrality = 0;

        int oppositeCode = gameState.getOpposingCode(ourColor);

        double yMiddle = n / 2.0;
        double xMiddle = m / 2.0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                double currentXCentrality = -Math.abs(j-xMiddle);
                double currentYCentrality = -Math.abs(i-yMiddle);
                double distance = currentXCentrality + currentYCentrality;

                int currentColor = gameState.getColor(i, j);
                if (currentColor == ourColor)
                    ourCentrality += distance;
                else if (currentColor == oppositeCode)
                    theirCentrality += distance;
            }
        }

        return ourCentrality - theirCentrality;
    }
}
