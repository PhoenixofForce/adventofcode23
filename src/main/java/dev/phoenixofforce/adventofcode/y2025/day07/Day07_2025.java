package dev.phoenixofforce.adventofcode.y2025.day07;

import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;

@Slf4j
@Component
public class Day07_2025 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        Position start = new Position(0, 0);
        for(int x = 0; x < input.width(); x++) {
            for(int y = 0; y < input.height(); y++) {
                if(input.getChar(x, y) == 'S') {
                    start = new Position(x, y);
                }
                if(start.getX() != 0 && start.getY() != 0) break;
            }
            if(start.getX() != 0 && start.getY() != 0) break;
        }

        int splits = 0;
        Set<Position> beams = new HashSet<>();
        beams.add(start);

        while(!beams.isEmpty()) {
            Set<Position> newBeams = new HashSet<>();

            for(Position beam: beams) {
                Position newPosition = beam.applyDirection(Direction.SOUTH);
                if(!input.inbounds(newPosition)) continue;
                else if(input.getChar(newPosition) == '.') newBeams.add(newPosition);
                else if(input.getChar(newPosition) == '^') {
                    splits++;
                    for(Direction d: List.of(Direction.EAST, Direction.WEST)) {
                        Position newSplitPosition = newPosition.applyDirection(d);
                        if(!input.inbounds(newSplitPosition)) continue;
                        newBeams.add(newSplitPosition);
                    }
                }
            }

            log.info("{}: {} -> {}", splits, beams.size(), newBeams.size());
            beams = newBeams;
        }

        return splits;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        Position start = new Position(0, 0);
        for(int x = 0; x < input.width(); x++) {
            for(int y = 0; y < input.height(); y++) {
                if(input.getChar(x, y) == 'S') {
                    start = new Position(x, y);
                }
                if(start.getX() != 0 && start.getY() != 0) break;
            }
            if(start.getX() != 0 && start.getY() != 0) break;
        }

        return countRealities(input, start);
    }

    private Map<Position, Long> cache = new HashMap<>();
    private long countRealities(PuzzleInput input, Position beam ) {
        if(cache.containsKey(beam)) return cache.get(beam);
        Position newPosition = beam.applyDirection(Direction.SOUTH);
        long realities = 0;

        if(!input.inbounds(newPosition)) realities = 1;
        else if(input.getChar(newPosition) == '.') realities = countRealities(input, newPosition);
        else if(input.getChar(newPosition) == '^') {
            realities = countRealities(input, newPosition.applyDirection(Direction.WEST)) + countRealities(input, newPosition.applyDirection(Direction.EAST));
        }

        cache.put(beam, realities);
        return realities;
    }



}
