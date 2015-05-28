package CimpressPuzzle;

public class DirectionFactory {
    private static Direction forward = new ForwardDirection();
    private static Direction backward = new BackwardDirection();
    static Direction fromIndex(int index) {
        return index == 0 ? forward : backward;
    }
}
