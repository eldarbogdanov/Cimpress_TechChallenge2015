package CimpressPuzzle;

import java.util.ArrayList;
import java.util.List;

public class RandomSquareChooser implements SquareChooser {
    private final int choices;
    public RandomSquareChooser(int choices) {
        this.choices = choices;
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

    @Override
    public Iterable<Square> provide(Grid grid) {
        List<Square> candidates = getCandidates(grid);
        if (candidates.size() <= choices) return candidates;
        List<Square> ret = new ArrayList<>();
        for (int choice = 0; choice < choices; choice++) {
            // if we consider at least as many offsprings as there are candidates, give up randomness and try each
            Square sq = getRandomElement(candidates);
            ret.add(sq);
        }
        return ret;
    }
}
