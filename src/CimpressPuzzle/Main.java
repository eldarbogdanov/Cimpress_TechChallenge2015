package CimpressPuzzle;

import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

public class Main {

    /**
     * @param args[0] - height of the puzzle
     * @param args[1] - width of the puzzle
     * @param args[2] - a string representation of the row-concatenated puzzle with 'X' marking cells to be covered
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        final int h = Integer.parseInt(args[0]);
        final int w = Integer.parseInt(args[1]);
        final boolean[][] free = new boolean[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                free[i][j] = args[2].charAt(i * w + j) == 'X';
            }
        }

        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + 9400;

        Grid grid = new Grid(free);

        Distributor distributor = new MultiThreadDistributorContour(4, 250);
        try {
            SolutionAggregator aggregatorContour = distributor.submit(grid, endTime);

            Solver solver = new BruteForceSolver(new GreedySolverOptimized());
            System.err.println(solver.solve(grid).size());

            Thread.sleep(endTime - System.currentTimeMillis());
            List<Square> solution = aggregatorContour.getCurrent();

            System.err.println(solution.size());
            System.out.println(jsonify(solution));
        } finally {
            distributor.shutdown();
        }
    }

    static String jsonify(final Iterable<Square> squares) {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();
        for (Square square : squares) {
            arrayBuilder.add(factory.createObjectBuilder().
                    add("X", square.j).
                    add("Y", square.i).
                    add("Size", square.size).build());
        }
        JsonArray squaresObject = arrayBuilder.build();

        JsonObject ret = factory.createObjectBuilder().
                add("squares", squaresObject).
                build();
        return ret.toString();
    }

}
