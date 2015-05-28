package CimpressPuzzle;

public class BackwardDirection implements Direction {
    @Override
    public int getFirst(int dimensionSize) {
        return dimensionSize - 1;
    }

    @Override
    public int getAfterLast(int dimensionSize) {
        return -1;
    }

    @Override
    public int getIncrement() {
        return -1;
    }
}
