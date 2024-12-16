package dev.phoenixofforce.adventofcode.y2024.day16;

import dev.phoenixofforce.adventofcode.solver.Dijkstra;
import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Day16_2024 implements Puzzle {

    private static long solutionPart1 = -1;

    @Data @AllArgsConstructor
    private static class State {
        private Position position;
        private Direction facing;

        @EqualsAndHashCode.Exclude
        private long score;

        @EqualsAndHashCode.Exclude
        private List<Position> path;
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        Position start = null;
        Position end = null;
        Map<Position, Boolean> isWall = new HashMap<>();

        for(int x = 0; x < input.width(); x++) {
            for(int y = 0; y < input.height(); y++) {
                Position position = new Position(x, y);
                char c = input.getChar(x, y);

                if(c == 'S') start = position;
                else if(c == 'E') end = position;

                isWall.put(position, c == '#');
            }
        }

        State startState = new State(start, Direction.EAST, 0, List.of());
        Position finalEnd = end;
        Dijkstra.End<State> endState = Dijkstra.from(startState)
            .to(possibleEnd -> possibleEnd.getPosition().equals(finalEnd))
            .generateNextSteps(state -> getNextMoves(isWall, state, solutionPart1))
            .withAccumulator((state, score) -> state.getScore())
            .getFirst();

        solutionPart1 = endState.getDistance();
        return solutionPart1;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        Position start = null;
        Position end = null;
        Map<Position, Boolean> isWall = new HashMap<>();

        for(int x = 0; x < input.width(); x++) {
            for(int y = 0; y < input.height(); y++) {
                Position position = new Position(x, y);
                char c = input.getChar(x, y);

                if(c == 'S') start = position;
                else if(c == 'E') end = position;

                isWall.put(position, c == '#');
            }
        }

        State startState = new State(start, Direction.EAST, 0, List.of());
        PriorityQueue<State> open = new PriorityQueue<>(Comparator.comparingLong(State::getScore));
        Map<State, Long> smallestScoreForPosition = new HashMap<>();
        open.add(startState);

        List<State> ends = new ArrayList<>();
        int i = 0;
        while(!open.isEmpty()) {
            State current = open.remove();

            if(i % 5000 == 0) System.out.println(open.size() + " items left, currentScore: " + current.getScore());
            i++;

            if(current.getScore() > solutionPart1) continue;
            if(current.getPosition().equals(end)) {
                ends.add(current);
                continue;
            }

            List<State> nextMoves = getNextMoves(isWall, current, solutionPart1);
            for(State next: nextMoves) {
                if(next.score <= smallestScoreForPosition.getOrDefault(next, Long.MAX_VALUE)) {
                    open.add(next);
                    smallestScoreForPosition.put(next, next.score);
                }

            }
        }

        Collection<Position> bestSpots = ends.stream()
            .flatMap(e -> e.getPath().stream())
            .collect(Collectors.toSet());

        bestSpots.add(end);

        for (int y = 0; y < input.height(); y++) {
            for(int x = 0; x < input.width(); x++) {
                char c = input.getChar(x, y);

                Position p = new Position(x, y);
                if(c == '#') System.out.print(c);
                else System.out.print(bestSpots.stream().anyMatch(e -> e.equals(p)) ? 'O': '.');
            }
            System.out.println();
        }

        return bestSpots.size();

    }

    private List<State> getNextMoves(Map<Position, Boolean> isWall, State currentState, long part1Solution) {
        List<Position> path = new ArrayList<>(currentState.path);
        path.add(currentState.getPosition());

        if(part1Solution >= 0 && currentState.getScore() > part1Solution) {
            return List.of();
        }

        List<State> nextStates = new ArrayList<>();

        Position nextPosition = currentState.getPosition().applyDirection(currentState.getFacing());
        if(!isWall.get(nextPosition)) {
            State nextState = new State(nextPosition, currentState.getFacing(), currentState.getScore() + 1, new ArrayList<>(path));
            if(!currentState.getPath().contains(nextState.getPosition())) {
                nextStates.add(nextState);
            }
        }

        Position nextClockwisePosition = currentState.getPosition().applyDirection(currentState.getFacing().clockwise());
        Position nextCounterclockwisePosition = currentState.getPosition().applyDirection(currentState.getFacing().counterclockwise());

        boolean isWallClockwise = isWall.get(nextClockwisePosition);
        boolean isWallCounterclockwise = isWall.get(nextCounterclockwisePosition);

        if(!isWallClockwise || isWallCounterclockwise) {
            State nextState = new State(currentState.getPosition(), currentState.getFacing().clockwise(), currentState.getScore() + 1000, new ArrayList<>(path));
            if(!currentState.getPath().contains(nextState.getPosition())) {
                nextStates.add(nextState);
            }
        }
        if(!isWallCounterclockwise) {
            State nextState = new State(currentState.getPosition(), currentState.getFacing().counterclockwise(), currentState.getScore() + 1000, new ArrayList<>(path));
            if(!currentState.getPath().contains(nextState.getPosition())) {
                nextStates.add(nextState);
            }
        }

        return nextStates;
    }

}
