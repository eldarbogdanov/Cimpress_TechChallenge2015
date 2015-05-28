package CimpressPuzzle;

import java.util.List;

public class BruteForceSolver implements Solver {
    private final Solver helper;
    public BruteForceSolver(Solver helper) {
        this.helper = helper;
    }

    @Override
    public List<Square> solve(Grid grid) {
        List<Square> ret = null;
        for(int i = 0; i < grid.height(); i++) {
            for(int j = 0; j < grid.width(); j++) {
                for(int size = 1; ; size++) {
                    if (!grid.fits(i, j, size)) break;
                    ModifiableGrid myGrid = grid.modifiableCopy();
                    myGrid.paint(i, j, size);
                    List<Square> solve = helper.solve(myGrid);
                    if (ret == null || ret.size() > solve.size()) {
                        ret = solve;
                        ret.add(new Square(i, j, size));
                    }
                }
            }
        }
        return ret;
    }
}
