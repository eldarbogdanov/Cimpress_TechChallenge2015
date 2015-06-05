package CimpressPuzzle;

import java.util.ArrayList;
import java.util.List;

public class ContourSquareChooser implements SquareChooser {
    private final Direction horDir;
    private final Direction verDir;
    private final boolean horFirst;
    public ContourSquareChooser(Direction horDir, Direction verDir, boolean horFirst) {
        this.horDir = horDir;
        this.verDir = verDir;
        this.horFirst = horFirst;
    }

    @Override
    public Iterable<Square> provide(Grid grid) {
        int n = grid.height();
        int m = grid.width();
        if (horFirst) {
            for (int i = verDir.getFirst(n); i != verDir.getAfterLast(n); i += verDir.getIncrement()) {
                for (int j = horDir.getFirst(m); j != horDir.getAfterLast(m); j += horDir.getIncrement()) {
                    if (!grid.fits(i, j, 1))
                        continue;
                    return getSquares(i, j, grid);
                }
            }
        } else {
            for (int j = horDir.getFirst(m); j != horDir.getAfterLast(m); j += horDir.getIncrement()) {
                for (int i = verDir.getFirst(n); i != verDir.getAfterLast(n); i += verDir.getIncrement()) {
                    if (!grid.fits(i, j, 1))
                        continue;
                    return getSquares(i, j, grid);
                }
            }
        }
        return new ArrayList<>();
    }

    private List<Square> getSquares(int i, int j, Grid grid) {
        List<Square> ret = new ArrayList<>();
        DirectedCell cell = new DirectedCell(i, j, horDir, verDir);
        int maxSize = grid.maximumFit(cell);
        for(int sz = 1; sz <= maxSize; sz++) {
            ret.add(new Square(cell.getTopmostCellI(sz), cell.getLeftmostCellJ(sz), sz));
        }
        return ret;
    }
}
