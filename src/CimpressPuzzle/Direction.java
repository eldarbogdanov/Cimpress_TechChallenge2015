package CimpressPuzzle;

public interface Direction {
    int getFirst(int dimensionSize);
    int getAfterLast(int dimensionSize);
    int getIncrement();
}
