package CimpressPuzzle;

public class ForwardDirection implements Direction {
    @Override
    public int getFirst(int dimensionSize) {
        return 0;
    }

    @Override
    public int getAfterLast(int dimensionSize) {
        return dimensionSize;
    }

    @Override
    public int getIncrement() {
        return 1;
    }
}
