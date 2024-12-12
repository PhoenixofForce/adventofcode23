package dev.phoenixofforce.adventofcode.y2024.day12;

import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.solver.Position;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Component
public class Day12_2024 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        Set<Position> closed = new HashSet<>();

        long out = 0;
        for(int x = 0; x < input.width(); x++) {
            for(int y = 0; y < input.height(); y++) {
                Position current = new Position(x, y);
                if(closed.contains(current)) continue;

                Set<Position> currentBucket = createBucket(input, current);
                closed.addAll(currentBucket);
                out += calcValue(currentBucket);
            }
        }

        return out;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        Set<Position> closed = new HashSet<>();

        long out = 0;
        for(int x = 0; x < input.width(); x++) {
            for(int y = 0; y < input.height(); y++) {
                Position current = new Position(x, y);
                if(closed.contains(current)) continue;

                Set<Position> currentBucket = createBucket(input, current);
                closed.addAll(currentBucket);
                out += calcValueWithSides(currentBucket);
            }
        }

        return out;
    }

    private Set<Position> createBucket(PuzzleInput input, Position start) {
        Set<Position> bucket = new HashSet<>();
        List<Position> open = new ArrayList<>();
        open.add(start);

        while(!open.isEmpty()) {
            Position current = open.removeFirst();
            if(bucket.contains(current)) continue;
            bucket.add(current);

            for(Position next: Direction.getNeighbors4(Position::new, (int) current.getX(), (int) current.getY())) {
                if(open.contains(next) || bucket.contains(next)) continue;
                if(!input.inbounds(next) || input.getChar(next) != input.getChar(current)) {
                    continue;
                }

                open.add(next);
            }
        }
        return bucket;
    }

    private long calcValue(Set<Position> bucket) {
        long perimeter = 0;
        for(Position current: bucket) {
            for(Position next: Direction.getNeighbors4(Position::new, (int) current.getX(), (int) current.getY())) {
                if(!bucket.contains(next)) perimeter++;
            }
        }
        return perimeter * bucket.size();
    }

    private long calcValueWithSides(Set<Position> bucket) {
        long sides = 0;
        for(Position current: bucket) {

            List<Position> sidesWithOtherPlots = new ArrayList<>();
            List<Position> sidesOfPlot = Direction.getNeighbors4(Position::new, (int) current.getX(), (int) current.getY());
            for(Position next: sidesOfPlot) {
                if(!bucket.contains(next)) sidesWithOtherPlots.add(next);
            }

            Position top = new Position(current.getX(), current.getY() - 1);
            Position bottom = new Position(current.getX(), current.getY() + 1);
            Position left = new Position(current.getX() - 1, current.getY());
            Position right = new Position(current.getX() + 1, current.getY());
            Position topLeft = new Position(current.getX() - 1, current.getY() - 1);
            Position bottomRight = new Position(current.getX() + 1, current.getY() + 1);
            Position bottomLeft= new Position(current.getX() - 1, current.getY() + 1);
            Position topRight = new Position(current.getX() + 1, current.getY() - 1);

            //Check for outer corner
            long[] adjacentCountToCornerCount = {0, 0, 1, 2, 4};
            if(sidesWithOtherPlots.size() == 2 && sidesWithOtherPlots.contains(top) && sidesWithOtherPlots.contains(bottom)) sides += 0;
            else if(sidesWithOtherPlots.size() == 2 && sidesWithOtherPlots.contains(left) && sidesWithOtherPlots.contains(right)) sides += 0;
            else sides += adjacentCountToCornerCount[sidesWithOtherPlots.size()];

            //Check for inner corners
            if(bucket.contains(left) && bucket.contains(top) && !bucket.contains(topLeft)) sides++;
            if(bucket.contains(right) && bucket.contains(top) && !bucket.contains(topRight)) sides++;
            if(bucket.contains(left) && bucket.contains(bottom) && !bucket.contains(bottomLeft)) sides++;
            if(bucket.contains(right) && bucket.contains(bottom) && !bucket.contains(bottomRight)) sides++;
        }

        return sides * bucket.size();
    }
}
