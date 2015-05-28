package CimpressPuzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class RandomizedSolver implements Solver {
    private final double endTime;
    private final int offsprings;
    private final int survivors;
    public RandomizedSolver(double endTime, int offsprings, int survivors) {
        this.endTime = endTime;
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

    static class PopulationController implements Iterable<State> {
        private final PriorityQueue<State> population;
        private final int limit;
        PopulationController(int limit) {
            this.limit = limit;
            this.population = new PriorityQueue<>();
        }

        void addState(State state) {
            population.add(state);
            if (population.size() > limit) {
                population.poll();
            }
        }

        @Override
        public Iterator<State> iterator() {
            return population.iterator();
        }
    }

    @Override
    public List<Square> solve(Grid initialGrid) {
        PopulationController population = new PopulationController(survivors);
        population.addState(new State(initialGrid, new ArrayList<Square>()));

        int count = 0;
        List<Square> ret = population.iterator().next().completeSquares();
        while(System.currentTimeMillis() < endTime) {
            PopulationController nextPopulation = new PopulationController(survivors);
            for(State state : population) {
                List<Square> candidates = getCandidates(state.grid());
                if (candidates.isEmpty()) continue;
                for(int offspring = 0; offspring < offsprings; offspring++) {
                    ++count;
                    Square sq = getRandomElement(candidates);
                    State curState = state.withSquare(sq);
                    nextPopulation.addState(curState);
                    if (curState.completeSquares().size() < ret.size()) {
                        ret = curState.completeSquares();
                    }
                }
            }
            population = nextPopulation;
        }
        System.err.println(count);
        return ret;
    }
}
