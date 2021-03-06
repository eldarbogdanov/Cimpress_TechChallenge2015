package CimpressPuzzle;

import java.util.ArrayList;
import java.util.List;

public class GreedySolver implements Solver {
    private final Direction horDir;
    private final Direction verDir;
    private final boolean horFirst;
    public GreedySolver(Direction horDir, Direction verDir, boolean horFirst) {
        this.horDir = horDir;
        this.verDir = verDir;
        this.horFirst = horFirst;
    }

    @Override
    public List<Square> solve(Grid grid) {
        int n = grid.height();
        int m = grid.width();
        List<Square> ret = new ArrayList<Square>();
        ModifiableGrid myGrid = grid.modifiableCopy();
        if (horFirst) {
            for (int i = verDir.getFirst(n); i != verDir.getAfterLast(n); i += verDir.getIncrement()) {
                for (int j = horDir.getFirst(m); j != horDir.getAfterLast(m); j += horDir.getIncrement()) {
                    work(i, j, ret, myGrid);
                }
            }
        } else {
            for (int j = horDir.getFirst(m); j != horDir.getAfterLast(m); j += horDir.getIncrement()) {
                for (int i = verDir.getFirst(n); i != verDir.getAfterLast(n); i += verDir.getIncrement()) {
                    work(i, j, ret, myGrid);
                }
            }
        }
        return ret;
    }

    private void work(int i, int j, List<Square> ret, ModifiableGrid myGrid) {
        DirectedCell cell = new DirectedCell(i, j, horDir, verDir);
        int size = cell.maximumFit(myGrid);
        if (size > 0) {
            myGrid.paint(cell.getTopmostCellI(size), cell.getLeftmostCellJ(size), size);
            ret.add(new Square(cell.getTopmostCellI(size), cell.getLeftmostCellJ(size), size));
        }
    }
}
