package dev.phoenixofforce.adventofcode.y2024.day06;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.solver.Position;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Day06_2024 implements Puzzle {

    private final Set<Position> visitedPoints = new HashSet<>();
    private Position startPosition;
    private Direction startDirection;

    @Override
    public Object solvePart1(PuzzleInput input) {

        Position position = null;
        Direction currentDirection = null;

        outer:
        for(int y = 0; y < input.height(); y++) {
            for(int x = 0; x < input.width(); x++) {
                char c = input.getChar(x, y);
                if(Direction.isDirection(c)) {
                    this.startPosition = new Position(x, y);
                    this.startDirection = Direction.getDirection4(c);

                    position = startPosition;
                    currentDirection = startDirection;

                    visitedPoints.add(position);
                    break outer;
                }
            }
        }

        if(position == null || currentDirection == null) return "";
        while(input.inbounds(position)) {
            Position newPosition = position.applyDirection(currentDirection);
            if(input.getCharOrElseDot(newPosition) == '#') {
                currentDirection = currentDirection.clockwise();
                continue;
            }

            visitedPoints.add(position);
            position = newPosition;
        }

        return visitedPoints.size();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        long i = 0;
        long out = 0;
        for(Position currentPosition: visitedPoints) {
            i++;
            if(startPosition.equals(currentPosition)) continue;
            System.out.println("Testing " + i + " / " + visitedPoints.size() + " - " + out);

            PuzzleInput newInput = input.clone();
            newInput.setChar(currentPosition, '#');
            if(hasInfiniteLoop(newInput, startPosition, startDirection)) {
                out++;
            }
        }
        return out;
    }


    private boolean hasInfiniteLoop(PuzzleInput input, Position position, Direction currentDirection) {
        List<Position> path = new ArrayList<>();
        while(input.inbounds(position)) {
            Position newPosition = position.applyDirection(currentDirection);
            if(input.getCharOrElseDot(newPosition) == '#') {
                currentDirection = currentDirection.clockwise();
                continue;
            }

            if(hasLoop(path, position)) {
                return true;
            }

            path.add(position);
            position = newPosition;
        }

        return false;
    }

    private boolean hasLoop(List<Position> path, Position position) {
        if(path.indexOf(position) == path.lastIndexOf(position)) {
            return false;
        }

        int lastIndex = path.lastIndexOf(position);
        int entriesSinceLastIndex = path.size() - lastIndex;

        if(lastIndex - entriesSinceLastIndex <= 0) {
            return false;
        }

        List<Position> firstPossibleLoop = path.subList(lastIndex, path.size());
        List<Position> secondPossibleLoop = path.subList(lastIndex - entriesSinceLastIndex, lastIndex);

        boolean allTheSame = true;
        for(int i = 0; i < firstPossibleLoop.size(); i++) {
            if(!firstPossibleLoop.get(i).equals(secondPossibleLoop.get(i))) {
                allTheSame = false;
                break;
            }
        }

        return allTheSame;
    }
}
