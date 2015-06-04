package CimpressPuzzle;

import java.io.Serializable;
import java.util.List;

public interface SolutionAggregator extends Serializable {
    void accept(List<Square> squares);
    List<Square> getCurrent();
}
