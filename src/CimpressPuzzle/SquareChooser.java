package CimpressPuzzle;

/**
 * The abstraction of strategy of choosing candidate squares
 * on each iteration of genetic algorithm.
 */
public interface SquareChooser {
    /**
     * @param grid current state of the puzzle
     * @return candidate squares to put next
     */
    Iterable<Square> provide(Grid grid);
}
