package CimpressPuzzle;

public class Square {
    public final int i;
    public final int j;
    public final int size;

    Square(int i, int j, int size) {
        this.i = i;
        this.j = j;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Square square = (Square) o;

        if (i != square.i)
            return false;
        if (j != square.j)
            return false;
        return size == square.size;

    }

    @Override
    public int hashCode() {
        int result = i;
        result = 31 * result + j;
        result = 31 * result + size;
        return result;
    }

}
