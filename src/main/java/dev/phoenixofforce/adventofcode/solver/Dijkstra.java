package dev.phoenixofforce.adventofcode.solver;


import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Dijkstra {

	public interface Accumulator<T> {
		long accumulate(T t, long sum);
	}

	public interface NeighborFinder<T> {
		Collection<T> getNeighbors(T t);
	}

	public interface Heuristic<T> {
		int heuristic(T t);
	}

	public interface EndFinder<T> {
		boolean isEnd(T t);
	}

	private static final int ITERATIONS_PER_PRINT = 1000;

	public static <T> long findPath(T start, T end, NeighborFinder<T> nf, Accumulator<T> sc) {
		return findPathWithMultipleEndsAndHeuristic(start, t -> t.equals(end), nf, sc, t -> 0);
	}

	public static <T> long findPathWithMultipleEnds(T start, EndFinder<T> ef, NeighborFinder<T> nf, Accumulator<T> sc) {
		return findPathWithMultipleEndsAndHeuristic(start, ef, nf, sc, t -> 0);
	}

	public static <T> long findPathWithHeuristic(T start, T end, NeighborFinder<T> nf, Accumulator<T> sc, Heuristic<T> h) {
		return findPathWithMultipleEndsAndHeuristic(start, t -> t.equals(end), nf, sc, h);
	}

	public static <T> long findPathWithMultipleEndsAndHeuristic(T start, EndFinder<T> endFinder,
																NeighborFinder<T> neighborFinder, Accumulator<T> scoreCalculator, Heuristic<T> heuristic) {

		Map<T, Long> distances = new HashMap<>();
		Map<T, T> pre = new HashMap<>();

		distances.put(start, 0L);

		Set<T> closed = new HashSet<>();
		PriorityQueue<T> open = new PriorityQueue<>(Comparator.comparingLong(a -> distances.containsKey(a)? distances.get(a) + heuristic.heuristic(a): Integer.MAX_VALUE));
		distances.put(start, 0L);
		open.add(start);

		int iterations = 0;
		while(!open.isEmpty()) {
			T pos = open.remove();
			long currentScore = distances.get(pos);
			if(ITERATIONS_PER_PRINT > 0 && iterations % ITERATIONS_PER_PRINT == 0)
				log.info("score: {}, open: {}, closed: {}", currentScore, open.size(), closed.size());

			if(endFinder.isEnd(pos)) {
				T t = pos;
				while(t != null) {
					t = pre.get(t);
				}

				return distances.get(pos);
			}

			for(T n: neighborFinder.getNeighbors(pos)) {
				long newScore = scoreCalculator.accumulate(n, currentScore);

				if(!closed.contains(n) && newScore < distances.getOrDefault(n, Long.MAX_VALUE)) {
					distances.put(n, newScore);
					pre.put(n, pos);

					open.remove(n);
					open.add(n);
				}
			}

			closed.add(pos);
			iterations++;
		}

		log.error("Didnt find end point");
		return Long.MAX_VALUE;
	}
}