package CimpressPuzzle;

import java.util.ArrayList;
import java.util.List;

public class SolutionAggregatorSync implements SolutionAggregator {
    private List<Square> best;
    public SolutionAggregatorSync() {
        best = null;
    }
    public synchronized void accept(List<Square> squares) {
        if (best == null || best.size() > squares.size()) {
            best = new ArrayList<>(squares);
        }
    }

    @Override
    public synchronized List<Square> getCurrent() {
        return best;
    }
}
