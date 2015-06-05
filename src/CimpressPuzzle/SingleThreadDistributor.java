package CimpressPuzzle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadDistributor implements Distributor {

    public final static int OFFSPRINGS = 15;
    public final static int SURVIVORS = 300;

    private final ExecutorService exec;
    public SingleThreadDistributor() {
        exec = Executors.newSingleThreadExecutor();
    }

    @Override
    public SolutionAggregator submit(final Grid grid, final long endByMillis) {
        final SolutionAggregator solutionAggregator = new SolutionAggregatorSync();
        exec.submit(new Runnable() {
            @Override
            public void run() {
                ResponsiveSolver solver = new GeneticSolver(SURVIVORS,
                        new ContourSquareChooser(DirectionFactory.fromIndex(0), DirectionFactory.fromIndex(0), true),
                        new GreedySolverOptimized());
                solver.solveResponsively(grid, solutionAggregator, endByMillis, 0,
                        (endByMillis - System.currentTimeMillis()) / 50);
            }
        });
        return solutionAggregator;
    }

    @Override
    public void shutdown() {
        exec.shutdownNow();
    }
}
