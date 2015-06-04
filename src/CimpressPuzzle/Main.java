package CimpressPuzzle;

import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

public class Main {

    static String jsonify(final Iterable<Square> squares) {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();
        for(Square square : squares) {
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

    public static void main(String[] args) throws InterruptedException {
        final int h = Integer.parseInt(args[0]);
        final int w = Integer.parseInt(args[1]);
        final boolean[][] free = new boolean[h][w];
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {
                free[i][j] = args[2].charAt(i * w + j) == 'X';
            }
        }

        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + 9400;

        Grid grid = new Grid(free);

        Distributor distributor = new MultiThreadDistributor();
        SolutionAggregator aggregator = new SolutionAggregatorSync();
        try {
            distributor.submit(grid, endTime, aggregator);

            Solver solver = new BruteForceSolver(new GreedySolverOptimized());
            List<Square> solution1 = solver.solve(grid);

            Thread.sleep(endTime - System.currentTimeMillis());
            List<Square> solution2 = aggregator.getCurrent();

            System.out.println(jsonify(solution1.size() < solution2.size() ? solution1 : solution2));
            System.err.println(solution1.size()); // brute
            System.err.println(solution2.size()); // heuristic
        } finally {
            distributor.shutdown();
        }

    }
}
