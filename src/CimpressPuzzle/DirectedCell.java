package CimpressPuzzle;

public class DirectedCell {
    final int i;
    final int j;
    final Direction horDir;
    final Direction verDir;

    public DirectedCell(int i, int j, Direction horDir, Direction verDir) {
        this.i = i;
        this.j = j;
        this.horDir = horDir;
        this.verDir = verDir;
    }

    public int getTopmostCellI(int sz) {
        return i + (verDir.getIncrement() == 1 ? 0 : verDir.getIncrement() * (sz - 1));
    }

    public int getLeftmostCellJ(int sz) {
        return j + (horDir.getIncrement() == 1 ? 0 : horDir.getIncrement() * (sz - 1));
    }

    public int getBottommostCellI(int sz) {
        return getTopmostCellI(sz) + sz - 1;
    }

    public int getRightmostCellJ(int sz) {
        return getLeftmostCellJ(sz) + sz - 1;
    }

    public int maximumFit(Grid grid) {
        return grid.maximumFit(this);
    }
}
