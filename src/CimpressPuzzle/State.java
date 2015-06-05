package CimpressPuzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class State implements Comparable<State> {
    private final Grid grid;
    private final List<Square> completeSquares;
    private final List<Square> squares;
    private final Solver heuristic;

    private State(Grid grid, List<Square> squares, List<Square> completeSquares, Solver heuristic) {
        this.grid = grid;
        this.squares = squares;
        this.completeSquares = completeSquares;
        this.heuristic = heuristic;
    }

    public List<Square> completeSquares() {
        return completeSquares;
    }

    public Grid grid() {
        return grid;
    }

    private static List<Square> complete(Grid grid, List<Square> alreadyUsed, Solver heuristic) {
        List<Square> ret = new ArrayList<>(alreadyUsed);
        ret.addAll(heuristic.solve(grid));
        return Collections.unmodifiableList(ret);
    }

    State withSquare(Square square) {
        List<Square> newSquares = new ArrayList<>(squares);
        newSquares.add(square);
        ModifiableGrid newGrid = grid.modifiableCopy();
        newGrid.paint(square.i, square.j, square.size);
        List<Square> newCompleteSquares = completeSquares.contains(square) ? completeSquares : complete(newGrid, newSquares, heuristic);
        return new State(newGrid, newSquares, newCompleteSquares, heuristic);
    }

    static State of(Grid grid, Solver heuristic) {
        return new State(grid, new ArrayList<Square>(), complete(grid, new ArrayList<Square>(), heuristic), heuristic);
    }

    @Override
    public int compareTo(State o) {
        if (completeSquares.size() == o.completeSquares.size()) return 0;
        return completeSquares.size () < o.completeSquares.size() ? 1 : -1;
    }
}


