package dev.phoenixofforce.adventofcode.y2023.day10;

import dev.phoenixofforce.adventofcode.common.Puzzle;
import dev.phoenixofforce.adventofcode.common.PuzzleInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class Day10_2023 implements Puzzle {

    private static final char START_OVERRIDE = '|'; //get manually from input
    private final Set<Position> loop = new HashSet<>();

    @Data @AllArgsConstructor @EqualsAndHashCode
    static class Position {
        private int x, y;
        @EqualsAndHashCode.Exclude
        private int steps;
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        int startX = -1;
        int startY = -1;

        outer:
        for(int y = 0; y < input.getLines().size(); y++) {
            String line = input.getLines().get(y);
            for(int x = 0; x < line.length(); x++) {
                if(line.charAt(x) == 'S') {
                    startY = y;
                    startX = x;

                    input.getLines().set(y,  line.replace('S', START_OVERRIDE));
                    break outer;
                }
            }
        }

        Queue<Position> toLookAdd = new ArrayDeque<>();
        toLookAdd.add(new Position(startX, startY, 0));

        while (!toLookAdd.isEmpty()) {
            Position current = toLookAdd.poll();
            char c = getChar(input, current);

            if(c != ' ') {
                if(!loop.contains(current)) {
                    loop.add(current);
                }
                else {
                    Position other = loop.stream().filter(e -> e.equals(current)).findFirst().get();
                    if(other.steps <= current.steps) continue;
                    other.steps = current.steps;
                }

                Position adjacent1 = null;
                Position adjacent2 = null;

                if(c == '|') {
                    adjacent1 = new Position(current.getX(), current.getY() + 1, current.getSteps() + 1);
                    adjacent2 = new Position(current.getX(), current.getY() - 1, current.getSteps() + 1);
                } else if(c == '-') {
                    adjacent1 = new Position(current.getX() + 1, current.getY(), current.getSteps() + 1);
                    adjacent2 = new Position(current.getX() - 1, current.getY(), current.getSteps() + 1);
                } else if(c == 'L') {
                    adjacent1 = new Position(current.getX(), current.getY() - 1, current.getSteps() + 1);
                    adjacent2 = new Position(current.getX() + 1, current.getY(), current.getSteps() + 1);
                } else if(c == 'J') {
                    adjacent1 = new Position(current.getX(), current.getY() - 1, current.getSteps() + 1);
                    adjacent2 = new Position(current.getX() - 1, current.getY(), current.getSteps() + 1);
                } else if(c == 'F') {
                    adjacent1 = new Position(current.getX(), current.getY() + 1, current.getSteps() + 1);
                    adjacent2 = new Position(current.getX() + 1, current.getY(), current.getSteps() + 1);
                } else if(c == '7') {
                    adjacent1 = new Position(current.getX() - 1, current.getY(), current.getSteps() + 1);
                    adjacent2 = new Position(current.getX(), current.getY() + 1, current.getSteps() + 1);
                }
                toLookAdd.add(adjacent1);
                toLookAdd.add(adjacent2);
            }
        }

        return loop.stream().mapToInt(Position::getSteps).max().getAsInt();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        int counter = 0;
        boolean insideLoop = false;

        for(int y = 0; y < input.getLines().size(); y++) {
            String line = input.getLines().get(y);
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                Position current = new Position(x, y, 0);

                if(loop.contains(current)) {
                    if(List.of('|', 'J', 'L').contains(c)) insideLoop = !insideLoop;
                }

                if (insideLoop && !loop.contains(current)) {
                    counter++;
                }
            }
        }

        return counter;
    }

    private char getChar(PuzzleInput input, Position p) {
        if(p.getY() < 0 || p.getY() >= input.getLines().size()) return ' ';
        if(p.getX() < 0 || p.getX() >= input.getLines().get(p.getY()).length()) return ' ';
        return input.getLines().get(p.getY()).charAt(p.getX());
     }

}
