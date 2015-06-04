package CimpressPuzzle;

public interface Distributor {
    void submit(Grid grid, long endByMillis, SolutionAggregator solutionAggregator);
    void shutdown();
}
