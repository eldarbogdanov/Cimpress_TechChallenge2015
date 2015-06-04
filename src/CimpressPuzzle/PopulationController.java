package CimpressPuzzle;

import java.util.Iterator;
import java.util.PriorityQueue;

public class PopulationController<T extends Comparable<T>> implements Iterable<T> {
    private final PriorityQueue<T> population;
    private final int limit;
    PopulationController(int limit) {
        this.limit = limit;
        this.population = new PriorityQueue<>();
    }

    void addState(T state) {
        population.add(state);
        if (population.size() > limit) {
            population.poll();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return population.iterator();
    }
}

