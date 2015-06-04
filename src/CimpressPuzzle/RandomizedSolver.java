package CimpressPuzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomizedSolver implements ResponsiveSolver {
    private final int offsprings;
    private final int survivors;
    private final long MAX_TIME_MILLIS = 10000;

    public RandomizedSolver(int offsprings, int survivors) {
        this.offsprings = offsprings;
        this.survivors = survivors;
    }

    private int weight(Square sq) {
        return sq.size;
    }

    private Square getRandomElement(List<Square> squares) {
        int weight = 0;
        for(int i = 0; i < squares.size(); i++) {
            weight += weight(squares.get(i));
        }

        int rnd = (int)(Math.random() * weight);
        for(int i = 0; i < squares.size(); i++) {
            rnd -= weight(squares.get(i));
            if (rnd <= 0) {
                return squares.get(i);
            }
        }
        return null;
    }

    private List<Square> getCorners(Grid grid, Direction horDir, Direction verDir) {
        List<Square> corners = new ArrayList<>();
        for(int i = verDir.getFirst(grid.height()); i != verDir.getAfterLast(grid.height()); i += verDir.getIncrement()) {
            for(int j = horDir.getFirst(grid.width()); j != horDir.getAfterLast(grid.width()); j += horDir.getIncrement()) {
                if (grid.fits(i, j, 1) && !grid.fits(i - verDir.getIncrement(), j, 1) && !grid.fits(i, j - horDir.getIncrement(), 1)) {
                    DirectedCell cell = new DirectedCell(i, j, horDir, verDir);
                    int size = cell.maximumFit(grid);
                    corners.add(new Square(cell.getTopmostCellI(size), cell.getLeftmostCellJ(size), size));
                }
            }
        }
        return corners;
    }

    private List<Square> getSquares(Grid grid, Direction horDir, Direction verDir) {
        GreedySolver solver = new GreedySolver(horDir, verDir);
        return solver.solve(grid);
    }

    private List<Square> getCandidates(Grid grid, Direction horDir, Direction verDir) {
        return getCorners(grid, horDir, verDir);
    }

    private List<Square> getCandidates(Grid grid) {
        List<Square> candidates = new ArrayList<>();
        candidates.addAll(getCandidates(grid, DirectionFactory.fromIndex(0), DirectionFactory.fromIndex(0)));
        if (candidates.isEmpty()) {
            return candidates;
        }
        candidates.addAll(getCandidates(grid, DirectionFactory.fromIndex(0), DirectionFactory.fromIndex(1)));
        candidates.addAll(getCandidates(grid, DirectionFactory.fromIndex(1), DirectionFactory.fromIndex(0)));
        candidates.addAll(getCandidates(grid, DirectionFactory.fromIndex(1), DirectionFactory.fromIndex(1)));
        return candidates;
    }

    static class State implements Comparable<State> {
        private final Grid grid;
        private final List<Square> completeSquares;
        private final List<Square> squares;

        State(Grid grid, List<Square> squares) {
            this.grid = grid;
            this.squares = Collections.unmodifiableList(new ArrayList<>(squares));
            this.completeSquares = complete(grid, squares);
        }

        public List<Square> completeSquares() {
            return completeSquares;
        }

        public Grid grid() {
            return grid;
        }

        private static List<Square> complete(Grid grid, List<Square> alreadyUsed) {
            Solver solver = new GreedySolverOptimized();
            List<Square> ret = new ArrayList<>(alreadyUsed);
            ret.addAll(solver.solve(grid));
            return Collections.unmodifiableList(ret);
        }

        State withSquare(Square square) {
            List<Square> newSquares = new ArrayList<>(squares);
            newSquares.add(square);
            ModifiableGrid newGrid = grid.modifiableCopy();
            newGrid.paint(square.i, square.j, square.size);
            return new State(newGrid, newSquares);
        }

        @Override
        public int compareTo(State o) {
            if (completeSquares.size() == o.completeSquares.size()) return 0;
            return completeSquares.size () < o.completeSquares.size() ? 1 : -1;
        }
    }

    private List<Square> solveInternal(Grid initialGrid, SolutionAggregator solutionAggregator, long endByMillis, long callbackOffset, long callbackFrequency) {
        PopulationController<State> population = new PopulationController(survivors);
        population.addState(new State(initialGrid, new ArrayList<Square>()));

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
                List<Square> candidates = getCandidates(state.grid());
                if (candidates.isEmpty()) continue;
                for(int offspring = 0; offspring < offsprings && offspring < candidates.size(); offspring++) {
                    // if we consider at least as many offsprings as there are candidates, give up randomness and try each
                    Square sq = offsprings >= candidates.size() ? candidates.get(offspring) : getRandomElement(candidates);
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
