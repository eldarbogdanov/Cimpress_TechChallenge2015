package CimpressPuzzle;

import java.util.ArrayList;
import java.util.List;

public class GreedySolver implements Solver {
    @Override
    public List<Square> solve(Grid grid) {
        int n = grid.height();
        int m = grid.width();
        ModifiableGrid myGrid = grid.modifiableCopy();
        List<Square> ret = new ArrayList<Square>();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                int size = 0;
                for(int sz = 1; ; sz++) {
                    if (!myGrid.fits(i, j, sz)) break;
                    size = sz;
                }
                if (size > 0) {
                    myGrid.paint(i, j, size);
                    ret.add(new Square(i, j, size));
                }
            }
        }
        return ret;
    }
}
