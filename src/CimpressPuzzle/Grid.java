package CimpressPuzzle;

public class Grid {
    protected final boolean[][] free;

    public Grid(boolean[][] free) {
        this.free = free;
    }

    public int height() {
        return free.length;
    }

    public int width() {
        return free[0].length;
    }

    public boolean fits(int row, int col, int size) {
        if (row < 0 || col < 0 || row + size > free.length || col + size > free[0].length) return false;
        for(int i = row; i < row + size; i++)
            for(int j = col; j < col + size; j++) {
                if (!free[i][j]) return false;
            }
        return true;
    }

    public int maximumFit(DirectedCell cell) {
        if (cell.i < 0 || cell.j < 0 || cell.i >= free.length || cell.j >= free[0].length) return 0;
        int sz = 0;
        while(true) {
            ++sz;
            int i0 = cell.getTopmostCellI(sz);
            int j0 = cell.getLeftmostCellJ(sz);
            int i1 = cell.getBottommostCellI(sz);
            int j1 = cell.getRightmostCellJ(sz);
            if (i0 < 0 || i1 >= free.length || j0 < 0 || j1 >= free[0].length) break;

            int newRow = cell.verDir.getIncrement() > 0 ? i1 : i0;
            int newCol = cell.horDir.getIncrement() > 0 ? j1 : j0;
            boolean isGood = true;
            for(int i = 0; i < sz; i++) {
                if (!free[newRow][j0 + i] || !free[i0 + i][newCol]) {
                    isGood = false;
                    break;
                }
            }
            if (!isGood) break;
        }
        return sz - 1;
    }

    public ModifiableGrid modifiableCopy() {
        boolean[][] newGrid = new boolean[free.length][free[0].length];
        for(int i = 0; i < free.length; i++)
            for(int j = 0; j < free[0].length; j++)
                newGrid[i][j] = free[i][j];
        return new ModifiableGrid(newGrid);
    }

}
