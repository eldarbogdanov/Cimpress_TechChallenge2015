package CimpressPuzzle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadDistributor implements Distributor {
    public final static int[] OFFSPRINGS = new int[]{10, 10, 15, 15};
    public final static int[] SURVIVORS = new int[]{150, 200, 150, 200};
    public final static int THREADS = 8;

    private final ExecutorService exec;
    public MultiThreadDistributor() {
        exec = Executors.newFixedThreadPool(THREADS);
    }

    @Override
    public void submit(final Grid grid, final long endByMillis, final SolutionAggregator solutionAggregator) {
        long callbackOffset = 0;
        final long callbackFrequencyMillis = (endByMillis - System.currentTimeMillis()) / 50;
        for(int it = 0; it < THREADS; it++) {
            callbackOffset += callbackFrequencyMillis / THREADS;
            final long callbackOffsetToUse = callbackOffset;
            final int offsprings = OFFSPRINGS[it / 2];
            final int survivors = SURVIVORS[it / 2];
            exec.submit(new Runnable() {
                @Override
                public void run() {
                    ResponsiveSolver solver = new RandomizedSolver(offsprings, survivors);
                    solver.solveResponsively(grid, solutionAggregator, endByMillis, callbackOffsetToUse, callbackFrequencyMillis);
                }
            });
        }
    }

    @Override
    public void shutdown() {
        exec.shutdownNow();
    }
}
