package CimpressPuzzle;

/**
 * An abstraction of entity that computes the solution for a given grid,
 * with support for providing the best result it computed at any point of time.
 */
public interface ResponsiveSolver extends Solver {
    /**
     * @param grid - the puzzle to be solved
     * @param solutionAggregator - the object to report computation results to
     * @param endByMillis - the timestamp after which computation results won't matter to the caller
     * @param callbackOffsetMillis - the delay after which periodic callbacks to solutionAggregator will occur
     * @param callbackFrequencyMillis - the frequency of callbacks
     */
    void solveResponsively(Grid grid, SolutionAggregator solutionAggregator, long endByMillis, long callbackOffsetMillis, long callbackFrequencyMillis);
}
