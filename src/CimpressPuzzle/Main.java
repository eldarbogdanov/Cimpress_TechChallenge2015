package CimpressPuzzle;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

public class Main {

    static String jsonify(final String id, final Iterable<Square> squares) {
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
                add("id", id).
                add("squares", squaresObject).
                build();
        return ret.toString();
    }

    public static void main(String[] args) {
        final int h = Integer.parseInt(args[0]);
        final int w = Integer.parseInt(args[1]);
        final boolean[][] free = new boolean[h][w];
        for(int i = 0; i < h; i++) {
            for(int j = 0; j < w; j++) {
                free[i][j] = args[2].charAt(i * w + j) == 'X';
            }
        }
        final String id = args[3];

        Solver solver = new GreedySolver();
        Grid grid = new Grid(free);

        System.out.println(jsonify(id, solver.solve(grid)));
    }
}
