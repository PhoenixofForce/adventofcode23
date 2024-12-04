package dev.phoenixofforce.adventofcode.solver;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Dijkstra {

	public interface Accumulator<State> {
		long accumulate(State state, long sum);
	}

	public interface NeighborFinder<State> {
		Collection<State> getNeighbors(State state);
	}

	public interface Heuristic<State> {
		int heuristic(State t);
	}

	public interface EndFinder<State> {
		boolean isEnd(State t);
	}

	@Data
	public static class End<State> {
		private final State endElement;
		private final long distance;
		private final List<State> path;
	}

	private static final int ITERATIONS_PER_PRINT = 1000;

	public static <State> long findPath(State start, State end, NeighborFinder<State> nf, Accumulator<State> sc) {
		return findPathWithMultipleEndsAndHeuristic(start, t -> t.equals(end), nf, sc, t -> 0);
	}

	public static <State> long findPathWithMultipleEnds(State start, EndFinder<State> ef, NeighborFinder<State> nf, Accumulator<State> sc) {
		return findPathWithMultipleEndsAndHeuristic(start, ef, nf, sc, t -> 0);
	}

	public static <State> long findPathWithHeuristic(State start, State end, NeighborFinder<State> nf, Accumulator<State> sc, Heuristic<State> h) {
		return findPathWithMultipleEndsAndHeuristic(start, t -> t.equals(end), nf, sc, h);
	}

	public static <State> long findPathWithMultipleEndsAndHeuristic(State start, EndFinder<State> endFinder,
																NeighborFinder<State> neighborFinder, Accumulator<State> scoreCalculator, Heuristic<State> heuristic) {

		return findPathWithMultipleEndsAndHeuristicAndMaybeLast(start, endFinder, neighborFinder, scoreCalculator, heuristic, true).distance;
	}

	public static <State> End<State> findPathWithMultipleEndsAndHeuristicAndMaybeLast(State start, EndFinder<State> endFinder,
																NeighborFinder<State> neighborFinder, Accumulator<State> scoreCalculator, Heuristic<State> heuristic, boolean returnFirstFoundEnd) {

		Map<State, Long> distances = new HashMap<>();
		Map<State, State> previousElements = new HashMap<>();
		distances.put(start, 0L);

		Set<State> closed = new HashSet<>();
		PriorityQueue<State> open = new PriorityQueue<>(Comparator.comparingLong(a -> distances.containsKey(a)? distances.get(a) + heuristic.heuristic(a): Long.MAX_VALUE));
		open.add(start);

		int iterations = 0;
		State lastFoundEnd = null;
		while(!open.isEmpty()) {
			State currentState = open.remove();
			long currentScore = distances.get(currentState);

			if(ITERATIONS_PER_PRINT > 0 && iterations % ITERATIONS_PER_PRINT == 0)
				log.info("score: {}, open: {}, closed: {}", currentScore, open.size(), closed.size());

			if(endFinder.isEnd(currentState)) {
				lastFoundEnd = currentState;

				if(returnFirstFoundEnd) {
					return generateEndObject(currentState, previousElements, distances);
				}
			}

			for(State newState: neighborFinder.getNeighbors(currentState)) {
				long scoreOfNewState = scoreCalculator.accumulate(newState, currentScore);

				if(!closed.contains(newState) && scoreOfNewState < distances.getOrDefault(newState, Long.MAX_VALUE)) {
					distances.put(newState, scoreOfNewState);
					previousElements.put(newState, currentState);

					open.remove(newState);
					open.add(newState);
				}
			}

			closed.add(currentState);
			iterations++;
		}

		if(lastFoundEnd != null) {
			return generateEndObject(lastFoundEnd, previousElements, distances);
		}

		log.error("Didnt find end point");
		return new End<>(null, Long.MAX_VALUE, new ArrayList<>());
	}

	private static <State> End<State> generateEndObject(State pos, Map<State, State> pre, Map<State, Long> distances) {
		List<State> path = new ArrayList<>();
		State t = pos;
		while(t != null) {
			path.add(t);
			t = pre.get(t);
		}

		return new End<State>(pos, distances.get(pos), path.reversed());
	}
}