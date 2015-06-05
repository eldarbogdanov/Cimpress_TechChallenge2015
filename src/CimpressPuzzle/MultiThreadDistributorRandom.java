package CimpressPuzzle;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadDistributorRandom implements Distributor {
    public final static int[] OFFSPRINGS = new int[]{10, 10, 15, 15};
    public final static int[] SURVIVORS = new int[]{150, 200, 150, 200};
    public final static int THREADS = 4;

    private final ExecutorService exec;
    public MultiThreadDistributorRandom() {
        exec = Executors.newFixedThreadPool(THREADS);
    }

    @Override
    public SolutionAggregator submit(final Grid grid, final long endByMillis) {
        final long startTime = System.currentTimeMillis();
        final long callbackFrequencyMillis = (endByMillis - startTime) / 20;
        long callbackOffset = endByMillis - startTime - callbackFrequencyMillis * 5;
        final SolutionAggregator[] solutionAggregators = new SolutionAggregator[THREADS];
        for(int it = 0; it < THREADS; it++) {
            solutionAggregators[it] = new SolutionAggregatorSync();
            callbackOffset += callbackFrequencyMillis / THREADS;
            final long callbackOffsetToUse = callbackOffset;
            final SquareChooser squareChooser = new RandomSquareChooser(OFFSPRINGS[it]);
            final SolutionAggregator curSolutionAggregator = solutionAggregators[it];
            final int survivors = SURVIVORS[it];
            exec.submit(new Runnable() {
                @Override
                public void run() {
                    ResponsiveSolver solver = new GeneticSolver(survivors, squareChooser, new GreedySolverOptimized());
                    solver.solveResponsively(grid, curSolutionAggregator, endByMillis, callbackOffsetToUse, callbackFrequencyMillis);
                }
            });
        }
        return new SolutionAggregator() {
            @Override
            public void accept(List<Square> squares) {
                // ignore
            }

            @Override
            public List<Square> getCurrent() {
                List<Square> ret = null;
                for(int it = 0; it < THREADS; it++) {
                    List<Square> ans = solutionAggregators[it].getCurrent();
                    if (ret == null || ret.size() > ans.size()) {
                        ret = ans;
                    }
                }
                return ret;
            }
        };
    }

    @Override
    public void shutdown() {
        exec.shutdownNow();
    }
}
