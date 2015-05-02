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
        if (row + size > free.length || col + size > free[0].length) return false;
        for(int i = row; i < row + size; i++)
            for(int j = col; j < col + size; j++) {
                if (!free[i][j]) return false;
            }
        return true;
    }

    public ModifiableGrid modifiableCopy() {
        boolean[][] newGrid = new boolean[free.length][free[0].length];
        for(int i = 0; i < free.length; i++)
            for(int j = 0; j < free[0].length; j++)
                newGrid[i][j] = free[i][j];
        return new ModifiableGrid(newGrid);
    }

}
