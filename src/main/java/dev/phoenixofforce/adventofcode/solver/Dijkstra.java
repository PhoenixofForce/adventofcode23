package dev.phoenixofforce.adventofcode.solver;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Dijkstra<State> {

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

		@EqualsAndHashCode.Exclude
		private final long distance;

		@EqualsAndHashCode.Exclude
		private final List<State> path;

		public boolean isEmpty() {
			return endElement == null || distance == Long.MAX_VALUE || path.isEmpty();
		}
	}

	private int iterations_per_print = -1;

	private State start;
	private EndFinder<State> endFinder;
	private NeighborFinder<State> neighborFinder;
	private Accumulator<State> accumulator;
	private Heuristic<State> heuristic;

	private Dijkstra(State start) {
		this.start = start;
		this.accumulator = (e, score) -> score + 1;
		this.heuristic = e -> 0;
	}

	public static <State> Dijkstra<State> from(State start) {
		return new Dijkstra<>(start);
	}

	public Dijkstra<State> to(State end) {
		this.endFinder = e -> e.equals(end);
		return this;
	}

	public Dijkstra<State> to(EndFinder<State> endFinder) {
		this.endFinder = endFinder;
		return this;
	}

	public Dijkstra<State> generateNextSteps(NeighborFinder<State> neighborFinder) {
		this.neighborFinder = neighborFinder;
		return this;
	}

	public Dijkstra<State> withAccumulator(Accumulator<State> accumulator) {
		this.accumulator = accumulator;
		return this;
	}

	public Dijkstra<State> withHeuristic(Heuristic<State> heuristic) {
		this.heuristic = heuristic;
		return this;
	}

	public Dijkstra<State> withDebugPrintEveryIteration(int iterations_per_print) {
		this.iterations_per_print = iterations_per_print;
		return this;
	}

	public End<State> getFirst() {
		return findPath(false);
	}

	public End<State> getLast() {
		return findPath(true);
	}

	public List<End<State>> getAll() {
		return bfs();
	}


	private End<State> pathNotFound() {
		return new End<>(null, Long.MAX_VALUE, List.of());
	}

	private End<State> findPath(boolean returnLast) {

		if(start == null) throw new RuntimeException("Start must be set");
		if(endFinder == null) throw new RuntimeException("End must be set");
		if(neighborFinder == null) throw new RuntimeException("State Generator must be set");

		Map<State, Long> distances = new HashMap<>();
		Map<State, State> previousElements = new HashMap<>();
		distances.put(start, 0L);

		Set<State> closed = new HashSet<>();
		PriorityQueue<State> open = new PriorityQueue<>(Comparator.comparingLong(a -> distances.containsKey(a)? distances.get(a) + heuristic.heuristic(a): Long.MAX_VALUE));
		open.add(start);

		int iterations = 0;
		End<State> lastEnd = null;
		while(!open.isEmpty()) {
			State currentState = open.remove();
			long currentScore = distances.get(currentState);

			if(iterations_per_print > 0 && iterations % iterations_per_print == 0)
				log.info("score: {}, open: {}, closed: {}", currentScore, open.size(), closed.size());
			iterations++;

			if(endFinder.isEnd(currentState)) {
				lastEnd = generateEndObject(currentState, previousElements, distances);
				if(!returnLast) {
					return lastEnd;
				}
				continue;
			}

			for(State newState: neighborFinder.getNeighbors(currentState)) {
				long scoreOfNewState = accumulator.accumulate(newState, currentScore);

				if(closed.contains(newState) || scoreOfNewState >= distances.getOrDefault(newState, Long.MAX_VALUE)) {
					continue;
				}

				distances.put(newState, scoreOfNewState);
				previousElements.put(newState, currentState);

				open.remove(newState);
				open.add(newState);
			}

			closed.add(currentState);
		}

		if(lastEnd != null) return lastEnd;

		log.warn("No Path was found");
		return pathNotFound();
	}

	private List<End<State>> bfs() {
		End<State> startState = new End<>(start, 0, List.of(start));

		Map<State, Long> smallestScoreForPosition = new HashMap<>();
		PriorityQueue<End<State>> open = new PriorityQueue<>(Comparator.comparingLong(End::getDistance));
		open.add(startState);

		List<End<State>> ends = new ArrayList<>();

		int iterations = 0;
		while(!open.isEmpty()) {
			End<State> current = open.remove();

			if(iterations_per_print > 0 && iterations % iterations_per_print == 0)
				log.info("score: {}, open: {}", current.getDistance(), open.size());
			iterations++;

			if(endFinder.isEnd(current.getEndElement())) {
				ends.add(current);
				continue;
			}

			Collection<State> nextMoves = neighborFinder.getNeighbors(current.getEndElement());
			for(State next: nextMoves) {
				long scoreOfNewState = accumulator.accumulate(next, current.getDistance());

				if(current.getPath().contains(next)) continue;
				if(scoreOfNewState > smallestScoreForPosition.getOrDefault(next, Long.MAX_VALUE)) {
					continue;
				}

				List<State> path = new ArrayList<>(current.getPath());
				path.add(next);

				End<State> newState = new End<>(next, scoreOfNewState, path);
				open.add(newState);
				smallestScoreForPosition.put(next, newState.getDistance());
			}
		}

		return ends;
	}

	private static <State> End<State> generateEndObject(State pos, Map<State, State> pre, Map<State, Long> distances) {
		List<State> path = new ArrayList<>();
		State t = pos;
		while(t != null) {
			path.add(t);
			t = pre.get(t);
		}

		return new End<>(pos, distances.get(pos), path.reversed());
	}
}