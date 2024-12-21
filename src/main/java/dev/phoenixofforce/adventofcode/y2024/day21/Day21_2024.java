package dev.phoenixofforce.adventofcode.y2024.day21;

import dev.phoenixofforce.adventofcode.solver.Dijkstra;
import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day21_2024 implements Puzzle {

    private static final char[][] NUMERIC_PAD = new char[][]{
        {'7', '8', '9'},
        {'4', '5', '6'},
        {'1', '2', '3'},
        {' ', '0', 'A'}
    };

    private static final char[][] DIRECTIONAL_PAD = new char[][]{
        {' ', '^', 'A'},
        {'<', 'v', '>'},
    };

    @Override
    public Object solvePart1(PuzzleInput input) {
        long out = 0;
        for(String line: input.getLines()) {
            out += translate(line, 3-2);
        }
        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        long out = 0;
        for(String line: input.getLines()) {
            out += translate(line, 26-2);
        }
        return out;
    }

    public long translate(String line, int maxIteration) {
        Map<Character, Position> fromMap = new HashMap<>();
        for(int y = 0; y < DIRECTIONAL_PAD.length; y++) {
            for (int x = 0; x < DIRECTIONAL_PAD[y].length; x++) {
                fromMap.put(DIRECTIONAL_PAD[y][x], new Position(x, y));
            }
        }

        List<String> instructions = numericPadToDirectionalPad(line);
        long out = Long.MAX_VALUE;
        for(String s: instructions) {
            long currentOut = 0;
            for(int i = 0; i < s.length(); i++) {
                char next = s.charAt(i);
                char previous = i == 0 ? 'A': s.charAt(i - 1);
                currentOut += translateDirectionalPadXTimes(fromMap, previous, next, 0, maxIteration);
            }
            out = Math.min(currentOut, out);
        }

        long numericPath = Long.parseLong(line.replaceAll("[^0-9]", ""));
        System.out.println(line + ": " + numericPath + " * " + out + " = " + (numericPath * out));
        return numericPath * out;
    }

    @Data @AllArgsConstructor
    private static class SecondCacheItem {
        private char c1, c2;
        private int iterationDifference;
    }

    private final Map<SecondCacheItem, Long> secondCache = new HashMap<>();
    public long translateDirectionalPadXTimes(Map<Character, Position> charToPos, char currentStart, char nextState, int currentIteration, int maxIteration) {
        SecondCacheItem item = new SecondCacheItem(currentStart, nextState, maxIteration - currentIteration);
        if(secondCache.containsKey(item)) return secondCache.get(item);

        Position first = charToPos.get(currentStart);
        Position second = charToPos.get(nextState);

        if(currentIteration == maxIteration) {
            // add one for 'A'
            return Math.abs(first.getX() - second.getX()) + Math.abs(first.getY() - second.getY()) + 1;
        }

        List<String> path = findAllPaths(DIRECTIONAL_PAD, first, second);
        long out = path.stream()
            .mapToLong(s -> {
                long currentOut = 0;
                for(int i = 0; i < s.length(); i++) {
                    char next = s.charAt(i);
                    char previous = i == 0 ? 'A': s.charAt(i - 1);
                    currentOut += translateDirectionalPadXTimes(charToPos, previous, next, currentIteration + 1, maxIteration);
                }
                return currentOut;
            }).min().orElse(0);

        secondCache.put(item, out);
        return out;
    }

    public List<String> numericPadToDirectionalPad(String toWrite) {
        Map<Character, Position> fromMap = new HashMap<>();
        for(int y = 0; y < NUMERIC_PAD.length; y++) {
            for (int x = 0; x < NUMERIC_PAD[y].length; x++) {
                fromMap.put(NUMERIC_PAD[y][x], new Position(x, y));
            }
        }

        List<String> out = new ArrayList<>();
        Position current = fromMap.get('A');
        for(char c: toWrite.toCharArray()) {
            Position newButton = fromMap.get(c);
            List<String> paths = findAllPaths(NUMERIC_PAD, current, newButton);

            current = newButton;
            if(out.isEmpty()) {
                out = paths;
                continue;
            }

            List<String> newOut = new ArrayList<>();
            for(String currentOut: out) {
                for(String path: paths) {
                    newOut.add(currentOut + path);
                }
            }
            out = newOut;
        }

        return filterToMinLength(out);
    }

    //>--| Navigation between buttons

    @Data @AllArgsConstructor
    private static class State {
        private Position position;
        private Direction direction;
    }

    @Data @AllArgsConstructor
    private static class CacheItem {
        private char firstMapItem;
        private Position from, to;
    }

    private static final Map<CacheItem, List<String>> cache = new HashMap<>();
    private List<String> findAllPaths(char[][] from, Position current, Position newButton) {
        CacheItem item = new CacheItem(from[0][0], current, newButton);
        if(cache.containsKey(item)) {
            return cache.get(item);
        }

        List<String> out = Dijkstra.from(new State(current, null))
            .to(e -> e.getPosition().equals(newButton))
            .generateNextSteps(state -> {
                List<State> nextStates = new ArrayList<>();
                for(Direction d: Direction.values()) {
                    Position newPosition = state.getPosition().applyDirection(d);
                    if(newPosition.getY() < 0 || newPosition.getY() >= from.length || newPosition.getX() < 0 ||
                        newPosition.getX() >= from[0].length ||
                        from[(int) newPosition.getY()][(int) newPosition.getX()] == ' ') continue;

                    nextStates.add(new State(newPosition, d));
                }
                return nextStates;
            })
            .getAll()
            .stream()
            .map(e -> pathToString(e) + "A")
            .toList();
        out = filterToMinLength(out);

        cache.put(item, out);
        return out;
    }

    private String pathToString(Dijkstra.End<State> path) {
        return path.getPath().stream()
            .map(State::getDirection)
            .filter(Objects::nonNull)
            .map(e -> switch(e) {
                case EAST -> ">";
                case WEST -> "<";
                case NORTH -> "^";
                case SOUTH -> "v";
            }).reduce("", (a,b) -> a + b);
    }

    private List<String> filterToMinLength(List<String> list) {
        int minLength = list.stream().mapToInt(String::length).min().orElse(0);
        return list.stream().filter(e -> e.length() == minLength).distinct().toList();
    }

}
