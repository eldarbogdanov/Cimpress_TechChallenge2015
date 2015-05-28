package CimpressPuzzle;

import java.util.List;

public class GreedySolverOptimized implements Solver {
    @Override
    public List<Square> solve(Grid grid) {
        List<Square> ret = null;
        for(int horDir = 0; horDir < 2; horDir++) {
            for(int verDir = 0; verDir < 2; verDir++) {
                GreedySolver specificDirectionSolver = new GreedySolver(DirectionFactory.fromIndex(horDir), DirectionFactory.fromIndex(verDir));
                List<Square> cur = specificDirectionSolver.solve(grid);
                if (ret == null || ret.size() > cur.size()) {
                    ret = cur;
                }
            }
        }
        return ret;
    }
}
