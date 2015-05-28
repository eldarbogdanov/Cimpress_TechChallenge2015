package CimpressPuzzle;

public class ModifiableGrid extends Grid {
    public ModifiableGrid(boolean[][] free) {
        super(free);
    }

    public void paint(int row, int col, int size) {
        if (row + size > free.length || col + size > free[0].length || row < 0 || col < 0)
            throw new IllegalArgumentException("Attempted to put a square outside grid boundaries!");
        for(int i = row; i < row + size; i++)
            for(int j = col; j < col + size; j++) {
                if (!free[i][j]) throw new IllegalArgumentException("Attempted to put a square at occupied cell(s)!");
                free[i][j] = false;
            }
    }
}
