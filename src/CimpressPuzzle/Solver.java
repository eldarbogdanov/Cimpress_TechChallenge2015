package CimpressPuzzle;

import java.util.List;

/**
 * An abstraction of entity that computes a solution for a given grid.
 */
public interface Solver {
    List<Square> solve(Grid grid);
}
