package CimpressPuzzle;

import java.util.ArrayList;
import java.util.List;

public class GreedySolver implements Solver {
    private final Direction horDir;
    private final Direction verDir;
    public GreedySolver(Direction horDir, Direction verDir) {
        this.horDir = horDir;
        this.verDir = verDir;
    }

    @Override
    public List<Square> solve(Grid grid) {
        int n = grid.height();
        int m = grid.width();
        List<Square> ret = new ArrayList<Square>();
        ModifiableGrid myGrid = grid.modifiableCopy();
        for(int i = verDir.getFirst(n); i != verDir.getAfterLast(n); i += verDir.getIncrement()) {
            for(int j = horDir.getFirst(m); j != horDir.getAfterLast(m); j += horDir.getIncrement()) {
                DirectedCell cell = new DirectedCell(i, j, horDir, verDir);
                int size = cell.maximumFit(myGrid);
                if (size > 0) {
                    myGrid.paint(cell.getTopmostCellI(size), cell.getLeftmostCellJ(size), size);
                    ret.add(new Square(cell.getTopmostCellI(size), cell.getLeftmostCellJ(size), size));
                }
            }
        }
        return ret;
    }
}
