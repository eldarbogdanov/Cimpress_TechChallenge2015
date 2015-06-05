package CimpressPuzzle;

import java.util.List;

public class GeneticSolver implements ResponsiveSolver {
    private final int survivors;
    private final SquareChooser squareChooser;
    private final Solver heuristic;

    private static final long MAX_TIME_MILLIS = 10000;

    public GeneticSolver(int survivors, SquareChooser squareChooser, Solver heuristic) {
        this.survivors = survivors;
        this.squareChooser = squareChooser;
        this.heuristic = heuristic;
    }

    private List<Square> solveInternal(Grid initialGrid, SolutionAggregator solutionAggregator, long endByMillis, long callbackOffset, long callbackFrequency) {
        PopulationController<State> population = new PopulationController(survivors);
        population.addState(State.of(initialGrid, heuristic));

        long nextCallback = System.currentTimeMillis() + callbackOffset + callbackFrequency;
        List<Square> ret = population.iterator().next().completeSquares();
        while(true) {
            long time = System.currentTimeMillis();
            if (time >= endByMillis) break;
            if (time >= nextCallback) {
                solutionAggregator.accept(ret);
                nextCallback += callbackFrequency;
            }
            PopulationController<State> nextPopulation = new PopulationController(survivors);
            for(State state : population) {
                for(Square sq : squareChooser.provide(state.grid())) {
                    State curState = state.withSquare(sq);
                    nextPopulation.addState(curState);
                    if (curState.completeSquares().size() < ret.size()) {
                        ret = curState.completeSquares();
                    }
                }
            }
            population = nextPopulation;
        }
        solutionAggregator.accept(ret);
        return ret;
    }

    @Override
    public List<Square> solve(Grid grid) {
        return solveInternal(
                grid,
                new SolutionAggregator() {
                    @Override
                    public void accept(List<Square> squares) {
                    }

                    @Override
                    public List<Square> getCurrent() {
                        return null;
                    }
                },
                MAX_TIME_MILLIS,
                0,
                MAX_TIME_MILLIS
        );
    }


    @Override
    public void solveResponsively(Grid grid, SolutionAggregator solutionAggregator, long endByMillis, long callbackOffset,
            long callbackFrequency) {
        solveInternal(grid, solutionAggregator, endByMillis, callbackOffset, callbackFrequency);
    }
}
