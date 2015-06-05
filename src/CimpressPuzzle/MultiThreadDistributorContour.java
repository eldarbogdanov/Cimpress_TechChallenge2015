package CimpressPuzzle;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadDistributorContour implements Distributor {
    private final ExecutorService exec;
    private final int threads;
    private final int survivors;
    public MultiThreadDistributorContour(int threads, int survivors) {
        this.threads = threads;
        this.survivors = survivors;
        exec = Executors.newFixedThreadPool(threads);
    }

    @Override
    public SolutionAggregator submit(final Grid grid, final long endByMillis) {
        final long startTime = System.currentTimeMillis();
        final long callbackFrequencyMillis = (endByMillis - startTime) / 20;
        long callbackOffset = endByMillis - startTime - callbackFrequencyMillis * 5;
        final SolutionAggregator[] solutionAggregators = new SolutionAggregator[threads];
        for(int it = 0; it < threads; it++) {
            solutionAggregators[it] = new SolutionAggregatorSync();
            callbackOffset += callbackFrequencyMillis / threads;
            final long callbackOffsetToUse = callbackOffset;
            final SquareChooser squareChooser = new ContourSquareChooser(
                    DirectionFactory.fromIndex(it / 4),
                    DirectionFactory.fromIndex(it / 2 % 2),
                    it % 2 == 0);
            final SolutionAggregator curSolutionAggregator = solutionAggregators[it];
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
                for(int it = 0; it < threads; it++) {
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
