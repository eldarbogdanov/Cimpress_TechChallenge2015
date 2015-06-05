package CimpressPuzzle;

/**
 * Distributor is responsible for executing the solution by invoking solvers
 * and potentially parallelizing workflow.
 */
public interface Distributor {
    /**
     * @param grid - the puzzle to solve
     * @param endByMillis - the timestamp after which the results won't matter to the caller
     * @return the object through which the caller can monitor the results asynchronously
     */
    SolutionAggregator submit(Grid grid, long endByMillis);

    /**
     * Cease all activity immediately.
     */
    void shutdown();
}
