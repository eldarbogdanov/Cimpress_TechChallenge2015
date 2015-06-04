package CimpressPuzzle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadDistributor implements Distributor {

    public final static int OFFSPRINGS = 15;
    public final static int SURVIVORS = 150;

    private final ExecutorService exec;
    public SingleThreadDistributor() {
        exec = Executors.newSingleThreadExecutor();
    }

    @Override
    public void submit(final Grid grid, final long endByMillis, final SolutionAggregator solutionAggregator) {
        exec.submit(new Runnable() {
            @Override
            public void run() {
                ResponsiveSolver solver = new RandomizedSolver(OFFSPRINGS, SURVIVORS);
                solver.solveResponsively(grid, solutionAggregator, endByMillis, 0,
                        (endByMillis - System.currentTimeMillis()) / 20);
            }
        });
    }

    @Override
    public void shutdown() {
        exec.shutdownNow();
    }
}
