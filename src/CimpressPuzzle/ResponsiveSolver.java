package CimpressPuzzle;

public interface ResponsiveSolver extends Solver {
    void solveResponsively(Grid grid, SolutionAggregator solutionAggregator, long endByMillis, long callbackOffsetMillis, long callbackFrequencyMillis);
}
